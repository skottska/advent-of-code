package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { matchNumbers(it) }
    println("part1=" + lines.sumOf { next(it) })
    println("part2=" + lines.sumOf { next(it.reversed()) })
}

private fun next(lines: List<Int>): Int = if (lines.toSet().size == 1) lines[0]
    else next(lines.windowed(size = 2).map { it[1] - it[0] }) + lines.last()
