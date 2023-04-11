package adventofcode.y2018 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbers
import adventofcode.readFile
import java.util.*

fun main() {
    var lines = readFile("src/main/resources/y2018/day10.txt").map { line ->
        matchNumbers(line).let { Coord(col = it[0], row = it[1]) to Coord(col = it[2], row = it[3]) }
    }

    var minWidth = lines.maxOf { it.first.col } - lines.minOf { it.first.col }
    var seconds = 0

    while (true) {
        val newLines = lines.map { Coord(col = it.first.col + it.second.col, row = it.first.row + it.second.row) to it.second }
        val newWidth = newLines.maxOf { it.first.col } - newLines.minOf { it.first.col }
        if (newWidth <= minWidth) {
            seconds++; lines = newLines; minWidth = newWidth
        } else {
            printLines(lines)
            println("part2=$seconds")
            break
        }
    }
}

private fun printLines(lines: List<Pair<Coord, Coord>>) {
    val coords = lines.map { it.first }
    val minCol = coords.minOf { it.col }
    val maxCol = coords.maxOf { it.col }
    (coords.minOf { it.row }..coords.maxOf { it.row }).forEach { row ->
        (minCol..maxCol).forEach { col -> if (coords.contains(Coord(row, col))) print("#") else print(".") }
        println()
    }
    println()
}
