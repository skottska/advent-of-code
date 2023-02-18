package adventofcode.y2021 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2021/day09.txt")
    val heights = lines.map { line -> line.map { it.digitToInt() } }
    val lowPoints = heights.mapIndexed { indexRow, row ->
        row.mapIndexedNotNull { indexCol, i ->
            if (adjacent(heights, indexRow, indexCol).all { heights[it.row][it.col] > i }) Coord(indexRow, indexCol) to i else null
        }
    }.flatten()
    println("part1=" + lowPoints.sumOf { it.second + 1 })
    val basins = lowPoints.map { low -> basin(heights, low.first).size }
    println("part2=" + basins.sorted().takeLast(3).fold(1) { total, i -> total * i })
}

private fun basin(heights: List<List<Int>>, toAdd: Coord, basin: Set<Coord> = emptySet()): Set<Coord> {
    return if (basin.contains(toAdd) || heights[toAdd.row][toAdd.col] == 9) basin
    else adjacent(heights, toAdd.row, toAdd.col).fold(basin + toAdd) { total, i -> basin(heights, i, total) }
}

private fun adjacent(heights: List<List<Int>>, row: Int, col: Int) = listOfNotNull(
    if (row == 0) null else Coord(row - 1, col),
    if (row == heights.size - 1) null else Coord(row + 1, col),
    if (col == 0) null else Coord(row, col - 1),
    if (col == heights.first().size - 1) null else Coord(row, col + 1)
)
