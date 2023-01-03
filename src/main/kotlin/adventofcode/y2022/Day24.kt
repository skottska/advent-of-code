package adventofcode.y2022 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day24.txt")

    val start = Coord(0, lines.first().indexOf('.'))
    val end = Coord(lines.size - 1, lines.last().indexOf('.'))
    val max = Coord(lines.size - 1, lines.first().length - 1)
    var blizzards = lines.mapIndexed { row, line ->
        line.mapIndexedNotNull { col: Int, c: Char ->
            val coord = Coord(row, col)
            when (c) {
                '^' -> BlizzardCol(pos = coord, move = -1, limit = 1, reset = max.row - 1)
                'v' -> BlizzardCol(pos = coord, move = 1, limit = max.row - 1, reset = 1)
                '<' -> BlizzardRow(pos = coord, move = -1, limit = 1, reset = max.col - 1)
                '>' -> BlizzardRow(pos = coord, move = 1, limit = max.col - 1, reset = 1)
                else -> null
            }
        }
    }.flatten()

    var part1Complete = false
    val seenOutgoing: MutableSet<Pair<Coord, List<Blizzard>>> = mutableSetOf()
    val seenReturning: MutableSet<Pair<Coord, List<Blizzard>>> = mutableSetOf()
    val seenOutgoing2: MutableSet<Pair<Coord, List<Blizzard>>> = mutableSetOf()
    var outgoing = setOf(start)
    var returning = mutableSetOf<Coord>()
    var outgoing2 = mutableSetOf<Coord>()
    var moves = 0
    while (true) {
        if (!part1Complete && outgoing.contains(end)) { println("part1=$moves"); part1Complete = true }
        if (outgoing2.contains(end)) { println("part2=$moves"); return }
        moves++
        blizzards = blizzards.map { it.move() }
        val blizzardCoords = blizzards.map { it.pos }
        if (returning.contains(start)) outgoing2.add(start)

        outgoing2 = iterateMove(outgoing2, start, end, max, listOf(seenOutgoing2), blizzards, blizzardCoords)
        outgoing2.forEach { seenOutgoing2.add(it to blizzards) }

        if (outgoing.contains(end)) returning.add(end)
        returning = iterateMove(returning, start, end, max, listOf(seenOutgoing2, seenReturning), blizzards, blizzardCoords)
        returning.forEach { seenReturning.add(it to blizzards) }

        outgoing = iterateMove(outgoing, start, end, max, listOf(seenOutgoing2, seenOutgoing, seenReturning), blizzards, blizzardCoords)
        outgoing.forEach { seenOutgoing.add(it to blizzards) }
    }
}

private fun iterateMove(positions: Set<Coord>, start: Coord, end: Coord, max: Coord, seen: List<Set<Pair<Coord, List<Blizzard>>>>, blizzards: List<Blizzard>, blizzardCoords: List<Coord>) =
    positions.asSequence().map {
        listOf(
            it.copy(row = it.row + 1),
            it.copy(col = it.col + 1),
            it.copy(row = it.row - 1),
            it.copy(col = it.col - 1),
            it
        )
    }.flatten().filter {
        (it == start || it == end || inBounds(it, max)) && !blizzardCoords.contains(it)
    }.filter {
        seen.none { s -> s.contains(it to blizzards) }
    }.toMutableSet()

private fun inBounds(pos: Coord, max: Coord) = pos.row < max.row && pos.row > 0 && pos.col < max.col && pos.col > 0

private interface Blizzard { val pos: Coord; fun move(): Blizzard }
private data class BlizzardRow(override val pos: Coord, val move: Int, val limit: Int, val reset: Int) : Blizzard {
    override fun move() = when (pos.col) {
        limit -> copy(pos = pos.copy(col = reset))
        else -> copy(pos = pos.copy(col = pos.col + move))
    }
}
private data class BlizzardCol(override val pos: Coord, val move: Int, val limit: Int, val reset: Int) : Blizzard {
    override fun move() = when (pos.row) {
        limit -> copy(pos = pos.copy(row = reset))
        else -> copy(pos = pos.copy(row = pos.row + move))
    }
}
