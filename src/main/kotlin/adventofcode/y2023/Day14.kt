package adventofcode.y2023 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.runNTimes
import adventofcode.transposeStrings
import java.lang.invoke.MethodHandles

fun main() {
    val grid = readFile(MethodHandles.lookup())
    println("part1=" + weight(north(grid)))
    println("part2=" + weight(runNTimes(::cycle, grid, 1000000000L)))
}

private fun weight(grid: List<String>) = grid.mapIndexed { i: Int, s: String -> (grid.size - i) * s.count { it == 'O' } }.sum()

private fun cycle(grid: List<String>) = listOf(::north, ::west, ::south, ::east).fold(grid) { total, func -> func(total) }
private fun north(grid: List<String>) = transposeStrings(transposeStrings(grid).map { westRow(it) })
private fun south(grid: List<String>) = transposeStrings(transposeStrings(grid).map { eastRow(it) })
private fun east(grid: List<String>) = grid.map { eastRow(it) }
private fun west(grid: List<String>) = grid.map { westRow(it) }

private fun westRow(row: String) = replaceRow(row, ".O", "O.")
private fun eastRow(row: String) = replaceRow(row, "O.", ".O")

private fun replaceRow(row: String, from: String, to: String): String {
    var newRow = row
    while (newRow.contains(from)) newRow = newRow.replace(from, to)
    return newRow
}
