package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2023/day09.txt").map { matchNumbers(it) }

    println("part1=" + lines.sumOf { nextInSequence(it) })
    println("part2=" + lines.sumOf { prevInSequence(it) })
}

private fun nextInSequence(lines: List<Int>): Int {
    return if (lines.toSet().size == 1) lines.first()
    else nextInSequence(lines.windowed(size = 2).map { it.last() - it.first() }) + lines.last()
}

private fun prevInSequence(lines: List<Int>): Int {
    return if (lines.toSet().size == 1) lines.first()
    else lines.first() - prevInSequence(lines.windowed(size = 2).map { it.last() - it.first() })
}
