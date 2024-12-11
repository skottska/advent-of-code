package adventofcode.y2024 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val line = matchNumbersLong(readFile(MethodHandles.lookup()).first())
    println("part1=" + line.sumOf { part2(it, 25) })
    println("part2=" + line.sumOf { part2(it, 75) })
}

val cache = mutableMapOf<Pair<Long, Int>, Long>()

private fun part2(l: Long, times: Int): Long {
    if (times == 0) return 1
    cache[l to times]?.let { return it }
    return iterate(l).sumOf { part2(it, times - 1) }.also { cache[l to times] = it }
}

private fun iterate(l: Long): List<Long> = when {
    l == 0L -> listOf(1L)
    l.toString().length % 2 == 0 -> {
        val s = l.toString()
        listOf(s.substring(0, s.length / 2).toLong(), s.substring(s.length / 2).toLong())
    }
    else -> listOf(l * 2024)
}