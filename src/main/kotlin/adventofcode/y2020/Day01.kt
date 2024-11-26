package adventofcode.y2020 // ktlint-disable filename

import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { it.toLong() }
    println("part1=" + sumEquals(2020, 2, lines)?.let { it.first() * it.last() })
    println("part2=" + sumEquals(2020, 3, lines)?.let { it[0] * it[1] * it[2] })
}

private fun sumEquals(eq: Long, num: Int, lines: List<Long>): List<Long>? {
    if (num == 1) { return if (lines.any { it == eq }) listOf(eq) else null }
    return lines.firstNotNullOfOrNull { line -> sumEquals(eq - line, num - 1, lines.minus(line))?.let { it + listOf(line) } }
}
