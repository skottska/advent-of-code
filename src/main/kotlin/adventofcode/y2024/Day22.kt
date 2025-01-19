package adventofcode.y2024 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { matchNumbersLong(it).first() }
    println("part1=" + lines.sumOf { (1..2000).fold(it) { total, _ -> generateSecret(total) } })

    val result = lines.map { generateDiffs(it) }
    val keys = result.flatMap { it.keys }.toSet()
    println("part2=" + keys.maxOf { key -> result.sumOf { it[key] ?: 0 } })
}

private fun generateDiffs(secret: Long): Map<List<Int>, Int> {
    val result = mutableMapOf<List<Int>, Int>()
    (1..2000).fold(secret to emptyList<Int>()) { iter, _ ->
        val next = generateSecret(iter.first)
        val diffs = (iter.second + (next.toString().last().digitToInt() - iter.first.toString().last().digitToInt())).takeLast(4)
        if (diffs.size == 4 && diffs !in result.keys) {
            result[diffs] = next.toString().last().digitToInt()
        }
        next to diffs
    }
    return result
}

private fun generateSecret(secret: Long) = mixAndPrune(secret, secret * 64).let { mixAndPrune(it, it / 32) }.let { mixAndPrune(it, it * 2048) }
private fun mixAndPrune(a: Long, b: Long) = (b xor a) % 16777216L
