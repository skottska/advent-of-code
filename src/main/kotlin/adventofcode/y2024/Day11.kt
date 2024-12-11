package adventofcode.y2024 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val line = matchNumbersLong(readFile(MethodHandles.lookup()).first())
    println("part1=" + line.sumOf { iterate(it, 25) })
    println("part2=" + line.sumOf { iterate(it, 75) })
}

private val cache = mutableMapOf<Pair<Long, Int>, Long>()
private fun iterate(l: Long, times: Int): Long = cache.getOrPut(l to times) {
    if (times == 0) 1
    else transform(l).sumOf { iterate(it, times - 1) }
}

private fun transform(l: Long): List<Long> = when {
    l == 0L -> listOf(1L)
    l.toString().length % 2 == 0 -> {
        val s = l.toString()
        listOf(s.substring(0, s.length / 2).toLong(), s.substring(s.length / 2).toLong())
    }
    else -> listOf(l * 2024)
}