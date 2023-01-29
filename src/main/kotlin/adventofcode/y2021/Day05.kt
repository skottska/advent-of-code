package adventofcode.y2021 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbers
import adventofcode.readFile
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val lines = readFile("src/main/resources/y2021/day05.txt").map { line ->
        matchNumbers(line).let { Coord(it[0], it[1]) to Coord(it[2], it[3]) }
    }
    println("part1=" + overlap(lines, isPart2 = false))
    println("part2=" + overlap(lines, isPart2 = true))
}

private fun overlap(lines: List<Pair<Coord, Coord>>, isPart2: Boolean): Int {
    val incFunc = { i: Int -> when { i > 0 -> 1; i < 0 -> -1; else -> 0 } }
    return lines.asSequence().filter { isPart2 || (it.first.row == it.second.row || it.first.col == it.second.col) }.map {
        val rowDiff = it.second.row - it.first.row
        val colDiff = it.second.col - it.first.col
        val num = max(abs(rowDiff), abs(colDiff))
        (0..num).map { i -> Coord(it.first.row + incFunc(rowDiff) * i, it.first.col + incFunc(colDiff) * i) }
    }.flatten().groupBy { it }.count { it.value.size > 1 }
}
