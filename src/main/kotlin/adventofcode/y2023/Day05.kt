package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.readFile
import java.lang.invoke.MethodHandles
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val seeds = matchNumbersLong(lines[0])
    println("part1=" + minConversion(lines, seeds.map { it..it }))
    println("part2=" + minConversion(lines, seeds.windowed(size = 2, step = 2) { it.first()..it.first() + it.last() }))
}

private fun move(a: LongRange, move: Long) = a.first + move..a.last + move

private fun minConversion(lines: List<String>, seeds: List<LongRange>): Long {
    val blankLines = lines.mapIndexed { i, line -> i to line }.filter { it.second.isEmpty() }.map { it.first } + lines.size
    val conversions = blankLines.windowed(size = 2).map { c ->
        lines.subList(c.first() + 2, c.last()).map {
            val nums = matchNumbersLong(it)
            (nums[1] until nums[1] + nums[2]) to nums[0]
        }
    }
    return conversions.fold(seeds) { total, conversionRound ->
        val totalM = total.toMutableList()
        val converted = mutableListOf<LongRange>()
        conversionRound.forEach { conv ->
            totalM.filter { overlaps(conv.first, it) }.forEach { seed ->
                overlap(seed, conv.first)?.let { converted.add(move(it, conv.second - conv.first.first)) }
                totalM.remove(seed); totalM.addAll(removeFromRange(seed, conv.first))
            }
        }
        totalM + converted
    }.minOf { it.first }
}

private fun removeFromRange(a: LongRange, b: LongRange) = when {
    a.first >= b.first && a.last <= b.last -> emptyList()
    a.first < b.first && a.last < b.last -> listOf(a.first until b.first)
    a.first < b.first -> listOf(a.first until b.first, b.last + 1..a.last)
    else -> listOf(b.last + 1..a.last)
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
