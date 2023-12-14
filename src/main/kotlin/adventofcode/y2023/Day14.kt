package adventofcode.y2023 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.transposeStrings

fun main() {
    val grid = readFile("src/main/resources/y2023/day14.txt")
    println("part1=" + weight(north(grid)))

    val offsetAndLoop = findLoop(grid)
    val afterLoop = (1000000000L - offsetAndLoop.first) % offsetAndLoop.second
    val part2 = (1..(offsetAndLoop.first + afterLoop)).fold(grid) { total, _ -> cycle(total) }
    println("part2=" + weight(part2))
}

private fun weight(grid: List<String>) = grid.mapIndexed { i: Int, s: String -> (grid.size - i) * s.count { it == 'O' } }.sum()

private fun findLoop(grid: List<String>): Pair<Int, Int> {
    val seen = mutableListOf<List<String>>()
    var cur = grid
    while (cur !in seen) {
        seen += cur
        cur = cycle(cur)
    }
    return seen.indexOf(cur).let { it to seen.size - it }
}

private fun cycle(grid: List<String>) = listOf(::north, ::west, ::south, ::east).fold(grid) { total, func -> func(total) }

private fun north(grid: List<String>) = transposeStrings(transposeStrings(grid).map { westRow(it) })
private fun south(grid: List<String>) = transposeStrings(transposeStrings(grid).map { eastRow(it) })
private fun east(grid: List<String>) = grid.map { eastRow(it) }
private fun west(grid: List<String>) = grid.map { westRow(it) }

private fun replaceRow(row: String, from: String, to: String): String {
    var newRow = row
    while (newRow.contains(from)) newRow = newRow.replace(from, to)
    return newRow
}

private fun westRow(row: String) = replaceRow(row, ".O", "O.")
private fun eastRow(row: String) = replaceRow(row, "O.", ".O")
