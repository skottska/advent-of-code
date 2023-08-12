package adventofcode.y2021 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.split

fun main() {
    val lines = readFile("src/main/resources/y2021/day14.txt")
    val originalPolymer = lines.first()
    val rules = lines.filter { it.contains('-') }.map { split(it) }.associate { it.first() to it.last() }
    val polymers = originalPolymer.windowed(2, 1).groupBy { it }.map { it.key to it.value.size.toLong() }.toMap()
    println("part1=" + iterate(steps = 10, originalPolymer, polymers, rules))
    println("part2=" + iterate(steps = 40, originalPolymer, polymers, rules))
}

private fun iterate(steps: Int, original: String, polymers: Map<String, Long>, rules: Map<String, String>): Long {
    val grouping3 = (1..steps).fold(polymers) { total, _ -> iterateInternal(total, rules) }
    val summary3 = ('A'..'Z').map { c ->
        val start = if (c == original.first()) 1 else 0
        val end = if (c == original.last()) 1 else 0
        val groupingCount = grouping3.map {
            when {
                it.key == "" + c + c -> it.value * 2
                c in it.key -> it.value
                else -> 0L
            }
        }.sum()
        (groupingCount + start + end) / 2
    }.filter { it != 0L }
    return summary3.max() - summary3.min()
}

private fun iterateInternal(polymers: Map<String, Long>, rules: Map<String, String>): Map<String, Long> {
    val result = mutableMapOf<String, Long>()
    polymers.forEach {
        val rule = rules.getValue(it.key)
        result[it.key.first() + rule] = result.getOrDefault(it.key.first() + rule, 0L) + it.value
        result[rule + it.key.last()] = result.getOrDefault(rule + it.key.last(), 0L) + it.value
    }
    return result
}
