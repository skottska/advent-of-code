package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.readFile
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lines = readFile("src/main/resources/y2023/day05.txt")
    val seeds = matchNumbersLong(lines[0])
    println("part1=" + minConversion(lines, seeds.map { it..it }))
    println("part2=" + minConversion(lines, seeds.windowed(size = 2, step = 2) { it.first()..it.first() + it.last() }))
}

private fun overlaps(a: LongRange, b: LongRange) = overlap(a, b) != null
private fun overlap(a: LongRange, b: LongRange): LongRange? {
    val min = max(a.first, b.first)
    val max = min(a.last, b.last)
    return when {
        min > max -> null
        else -> min..max
    }
}

private fun move(a: LongRange, move: Long) = a.first + move..a.last + move

private fun removeFromRange(a: LongRange, b: LongRange) = when {
    a.first >= b.first && a.last <= b.last -> emptyList()
    a.first < b.first && a.last < b.last -> listOf(a.first until b.first)
    a.first < b.first -> listOf(a.first until b.first, b.last + 1..a.last)
    else -> listOf(b.last + 1..a.last)
}

private fun minConversion(lines: List<String>, inSeeds: List<LongRange>, inPos: Int = 1): Long {
    val seeds = inSeeds.toMutableList()
    val conversions = mutableListOf<Pair<LongRange, Long>>()
    var pos = inPos + 2
    while (pos < lines.size && lines[pos].isNotEmpty()) {
        val nums = matchNumbersLong(lines[pos++])
        conversions += (nums[1] until nums[1] + nums[2]) to nums[0]
    }
    return when (conversions.isEmpty()) {
        true -> seeds.minOf { it.first }
        false -> {
            val converted = mutableListOf<LongRange>()
            conversions.forEach { conv ->
                seeds.filter { overlaps(conv.first, it) }.forEach { seed ->
                    overlap(seed, conv.first)?.let { converted.add(move(it, conv.second - conv.first.first)) }
                    seeds.remove(seed); seeds.addAll(removeFromRange(seed, conv.first))
                }
            }
            minConversion(lines, seeds + converted, pos)
        }
    }
}
