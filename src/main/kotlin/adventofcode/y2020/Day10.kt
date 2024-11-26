package adventofcode.y2020 // ktlint-disable filename

import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { it.toInt() }.sorted()
    val part1 = (lines.mapIndexed { index: Int, i: Int -> i - if (index == 0) 0 else lines[index - 1] } + 3).groupBy { it }.let {
        it.getValue(1).size * it.getValue(3).size
    }
    println("part1=$part1")

    val cache = mutableMapOf<Int, Long>()
    lines.reversed().forEach { cache[it] = adapterOrganise(lines, it, cache) }
    println("part2=" + (listOf(1, 2, 3).sumOf { cache.getOrDefault(it, 0) }))
}

private fun adapterOrganise(adapters: List<Int>, last: Int, cache: Map<Int, Long>) = when (last) {
    adapters.last() -> 1
    else -> listOf(1, 2, 3).map { it + last }.filter { adapters.contains(it) }.sumOf { cache.getValue(it) }
}
