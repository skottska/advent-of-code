package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2019/day24.txt").mapIndexed { row, line -> line.mapIndexed { col, c -> Coord(row, col) to c } }.flatten().toMap()
    part1(lines)

    val middle = Coord(2, 2)
    val emptyLayout = (0..4).map { row -> (0..4).map { col -> Coord(row, col) } }.flatten().filter { it != middle }
    var erinCoords = lines.filter { it.key != middle }.map { ErisCoord(it.key, 0) to it.value }.toMap().toMutableMap()
    (1..200).forEach { _ ->
        val minLevel = erinCoords.minOf { it.key.level }
        if (erinCoords.filter { it.key.level == minLevel }.any { it.value == '#' }) erinCoords += emptyLayout.map { ErisCoord(it, minLevel - 1) to '.' }
        val maxLevel = erinCoords.maxOf { it.key.level }
        if (erinCoords.filter { it.key.level == maxLevel }.any { it.value == '#' }) erinCoords += emptyLayout.map { ErisCoord(it, maxLevel + 1) to '.' }

        erinCoords = erinCoords.map { entry ->
            val around = entry.key.adjacent().count { erinCoords.getOrDefault(it, '.') == '#' }
            entry.key to when {
                entry.value == '#' && around != 1 -> '.'
                entry.value == '.' && around in 1..2 -> '#'
                else -> entry.value
            }
        }.toMap().toMutableMap()
    }

    println("part2=" + erinCoords.count { it.value == '#' })
}

private fun part1(lines: Map<Coord, Char>) {
    var layout = lines
    val layouts = mutableListOf<Map<Coord, Char>>()
    while (!layouts.contains(layout)) {
        layouts.add(layout)
        layout = layout.map { entry ->
            val around = entry.key.around().count { layout.getOrDefault(it, '.') == '#' }
            entry.key to when {
                entry.value == '#' && around != 1 -> '.'
                entry.value == '.' && around in 1..2 -> '#'
                else -> entry.value
            }
        }.toMap()
    }
    println("part1=" + layout.filter { it.value == '#' }.map { 2.toBigInteger().pow(it.key.row * 5 + it.key.col) }.sumOf { it })
}

private data class ErisCoord(val coord: Coord, val level: Int) {
    fun adjacent(): List<ErisCoord> {
        val sameLevel = coord.around().filter { it.row in 0..4 && it.col in 0..4 && it != Coord(2, 2) }.map { ErisCoord(it, level) }
        val aboveInc = if (coord.row != 0) emptyList() else listOf(ErisCoord(Coord(1, 2), level + 1))
        val belowInc = if (coord.row != 4) emptyList() else listOf(ErisCoord(Coord(3, 2), level + 1))
        val leftInc = if (coord.col != 0) emptyList() else listOf(ErisCoord(Coord(2, 1), level + 1))
        val rightInc = if (coord.col != 4) emptyList() else listOf(ErisCoord(Coord(2, 3), level + 1))

        val aboveDec = if (coord != Coord(3, 2)) emptyList() else (0..5).map { ErisCoord(Coord(4, it), level - 1) }
        val belowDec = if (coord != Coord(1, 2)) emptyList() else (0..5).map { ErisCoord(Coord(0, it), level - 1) }
        val rightDec = if (coord != Coord(2, 1)) emptyList() else (0..5).map { ErisCoord(Coord(it, 0), level - 1) }
        val leftDec = if (coord != Coord(2, 3)) emptyList() else (0..5).map { ErisCoord(Coord(it, 4), level - 1) }

        return sameLevel + aboveInc + belowInc + leftInc + rightInc + aboveDec + belowDec + rightDec + leftDec
    }
}
