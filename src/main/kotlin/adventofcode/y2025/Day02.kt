package adventofcode.y2025 // ktlint-disable filename

import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).first().split(",")
    val ranges = lines.map { it.split('-') }.map { it.first().toLong()..it.last().toLong() }

    val part1 = ranges.flatMap { r ->
        r.filter {
            val length = it.toString().length
            val subLength = if (length % 2 == 0) length / 2 else length / 2 + 1
            patternRepeats(it, subLength..subLength)
        }
    }.sum()
    println("part1=$part1")

    val part2 = ranges.flatMap { r -> r.filter { patternRepeats(it, 1..it.toString().length / 2) } }.sum()
    println("part2=$part2")
}

private fun patternRepeats(match: Long, range: IntRange): Boolean {
    val s = match.toString()
    if (s.length == 1) return false
    return range.any { subLength ->
        val sub = s.substring(0, subLength)
        s.length % subLength == 0 && s.chunked(subLength).all { it == sub }
    }
}
