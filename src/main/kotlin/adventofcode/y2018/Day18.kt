package adventofcode.y2018 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.mapCoord
import adventofcode.readFile
import adventofcode.runNTimes

fun main() {
    val grid = readFile("src/main/resources/y2018/day18.txt").mapCoord()

    val func = { times: Long ->
        val result = runNTimes(::iterate, grid, times)
        (result.values.count { it == '|' } * result.values.count { it == '#' })
    }
    println("part1=" + func(10))
    println("part2=" + func(1000000000))
}

private fun iterate(grid: Map<Coord, Char>): Map<Coord, Char> = grid.map { entry ->
    val around = entry.key.aroundDiag().filter { it in grid.keys }
    entry.key to when {
        entry.value == '.' && around.count { grid.getValue(it) == '|' } >= 3 -> '|'
        entry.value == '|' && around.count { grid.getValue(it) == '#' } >= 3 -> '#'
        entry.value == '#' && !(around.any { grid.getValue(it) == '#' } && around.any { grid.getValue(it) =='|' }) -> '.'
        else -> entry.value
    }
}.toMap()
