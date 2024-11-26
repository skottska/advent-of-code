package adventofcode.y2021 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.binaryToInt
import adventofcode.concat
import adventofcode.mapCoord
import adventofcode.matchNumbers
import adventofcode.printCoords
import adventofcode.readFile
import java.lang.IllegalArgumentException
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val algo = lines.first()
    val grid = lines.drop(2).mapCoord()
    println("part1=" + iterate(grid, algo, 2))
    println("part2=" + iterate(grid, algo, 50))
}

private fun iterate(grid: Map<Coord, Char>, algo: String, times: Int): Int {
    val result = (1..times).fold(grid) { total, i ->
        val default = when {
            algo.first() == '.' -> '.'
            i % 2 == 0 -> '#'
            else -> '.'
        }
        grid2grid(padGrid(total, default), algo, default)
    }
    return result.values.count { it == '#' }
}

private fun padGrid(grid: Map<Coord, Char>, default: Char): Map<Coord, Char> {
    val minCol = grid.keys.minOf { it.col }
    val maxCol = grid.keys.maxOf { it.col }
    val minRow = grid.keys.minOf { it.row }
    val maxRow = grid.keys.maxOf { it.row }
    val cols = listOf(minCol - 1, minCol - 2, maxCol + 1, maxCol + 2).flatMap { col ->
        (minRow - 2..maxRow + 2).map { row ->
            Coord(col, row) to default
        }
    }.toMap()
    val rows = listOf(minRow - 1, minRow - 2, maxRow + 1, maxRow + 2).flatMap { row ->
        (minCol - 2..maxCol + 2).map { col ->
            Coord(col, row) to default
        }
    }.toMap()
    return grid + cols + rows
}

private fun grid2grid(grid: Map<Coord, Char>, algo: String, default: Char) = grid.map {
    val around = listOf(-1, 0, 1).flatMap { x ->
        listOf(-1, 0, 1).map { y ->
            Coord(it.key.row + x, it.key.col + y)
        }
    }.map { c -> grid.getOrDefault(c, default) }
    it.key to outputPixel(algo, around)
}.toMap()

private val cache: MutableMap<List<Char>, Char> = mutableMapOf()
private fun outputPixel(algo: String, around: List<Char>): Char = cache.getOrPut(around) {
    val index = around.map { if (it == '.') "0" else "1" }.concat().binaryToInt().toInt()
    algo[index]
}
