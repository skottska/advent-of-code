package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2023/day09.txt").map { matchNumbers(it) }
    println("part1=" + lines.sumOf { next(it) })
    println("part2=" + lines.sumOf { next(it.reversed()) })
}

private fun next(lines: List<Int>): Int = when (lines.toSet().size == 1) {
    true -> lines[0]
    false -> next(lines.windowed(size = 2).map { it[1] - it[0] }) + lines.last()
}
