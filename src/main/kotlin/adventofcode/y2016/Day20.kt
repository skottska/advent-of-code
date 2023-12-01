package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.readFile
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lines = readFile("src/main/resources/y2016/day20.txt").map { line ->
        matchNumbersLong(line.replace('-', ' ')).let { it.first()..it.last() }
    }.mergeRange()

    println("part1=" + (lines.first().last + 1))

    val startDiff = lines.first().first
    val internalDiff = lines.windowed(size = 2, step = 1).sumOf { it.last().first - it.first().last - 1L }
    val endDiff = 4294967295L - lines.last().last
    println("part2=" + (startDiff + internalDiff + endDiff))
}

private fun List<LongRange>.mergeRange(): List<LongRange> {
    if (this.size == 1) return this
    val originalSize = this.size
    val sorted = this.sortedBy { it.first }
    val merged = sorted.windowed(size = 2, step = 1) { mergeRanges(it.first(), it.last()) }.flatten().toSet()
    val dedup = merged.filter { i -> merged.none { it.first <= i.first && it.last >= i.last && i != it } }
    return when (dedup.size) {
        originalSize -> dedup
        else -> dedup.mergeRange()
    }
}

private fun mergeRanges(a: LongRange, b: LongRange): List<LongRange> = when {
    a.last > b.first + 1 && a.first > b.last + 1 -> listOf(a, b)
    b.last > a.first + 1 && b.first > a.last + 1 -> listOf(a, b)
    else -> listOf(min(a.first, b.first)..max(a.last, b.last))
}
