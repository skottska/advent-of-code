package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2023/day09.txt").map { matchNumbers(it) }
    println("part1=" + lines.sumOf { nextInSequence(it) })
    println("part2=" + lines.sumOf { nextInSequence(it.reversed()) })
}

private fun nextInSequence(lines: List<Int>): Int = when (lines.toSet().size == 1) {
    true -> lines.first()
    false -> nextInSequence(lines.windowed(size = 2).map { it.last() - it.first() }) + lines.last()
}
