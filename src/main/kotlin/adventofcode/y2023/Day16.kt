package adventofcode.y2023 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.DirectedCoord
import adventofcode.Facing
import adventofcode.mapCoord
import adventofcode.readFile

fun main() {
    val grid = readFile("src/main/resources/y2023/day16.txt").mapCoord()
    println("part1=" + energise(DirectedCoord(Facing.RIGHT, Coord(0, 0)), grid))

    val maxCol = grid.maxOf { it.key.col }
    val maxRow = grid.maxOf { it.key.row }
    val verts = (0..maxCol).flatMap { listOf(DirectedCoord(Facing.DOWN, Coord(0, it)), DirectedCoord(Facing.UP, Coord(maxRow, it))) }
    val horizs = (0..maxRow).flatMap { listOf(DirectedCoord(Facing.RIGHT, Coord(it, 0)), DirectedCoord(Facing.LEFT, Coord(it, maxCol))) }
    val part2 = (horizs + verts).map { energise(it, grid) }.max()
    println("part2=$part2")
}

private fun energise(start: DirectedCoord, grid: Map<Coord, Char>): Int {
    val energised = mutableSetOf(start.coord)
    var positions = listOf(start)
    val seen = mutableSetOf<DirectedCoord>()
    while (positions.isNotEmpty()) {
        positions = positions.flatMap { p ->
            grid[p.coord]?.let { char ->
                energised += p.coord
                when {
                    char == '.' -> listOf(p)
                    char == '-' && p.facing.isHoriz() -> listOf(p)
                    char == '|' && p.facing.isVert() -> listOf(p)
                    char in listOf('-', '|') -> listOf(p.left(), p.right())
                    char == '\\' && p.facing.isHoriz() -> listOf(p.right())
                    char == '/' && p.facing.isVert() -> listOf(p.right())
                    else -> listOf(p.left())
                }.map { it.forward() }
            } ?: emptyList()
        }.filter { it !in seen }.also { seen += it }
    }
    return energised.size
}
