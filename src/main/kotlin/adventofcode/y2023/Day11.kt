package adventofcode.y2023 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.mapCoord
import adventofcode.readFile
import java.lang.invoke.MethodHandles
import kotlin.math.abs

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val universe = lines.mapCoord()
    val blankRows = lines.indices.filter { i -> lines[i].all { it == '.' } }
    val blankCols = lines.first().toList().indices.filter { col -> lines.map { it[col] }.all { it == '.' } }
    println("part1=" + distanceBetween(universe, blankRows, blankCols, 2L))
    println("part2=" + distanceBetween(universe, blankRows, blankCols, 1_000_000L))
}

private fun distanceBetween(grid: Map<Coord, Char>, blankRows: List<Int>, blankColumns: List<Int>, multiplier: Long): Long {
    val galaxies = grid.filter { it.value == '#' }.map { it.key }
    return galaxies.sumOf { g ->
        val expandG = expand(g, blankRows, blankColumns, multiplier)
        galaxies.filter { it != g }.sumOf {
            val expandIt = expand(it, blankRows, blankColumns, multiplier)
            abs(expandG.first - expandIt.first) + abs(expandG.second - expandIt.second)
        }
    } / 2L
}

private fun expand(c: Coord, blankRows: List<Int>, blankColumns: List<Int>, multiplier: Long): Pair<Long, Long> =
    c.row + blankRows.count { it < c.row } * (multiplier - 1) to c.col + blankColumns.count { it < c.col } * (multiplier - 1)
