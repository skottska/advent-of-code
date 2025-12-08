package adventofcode.y2025 // ktlint-disable filename

import adventofcode.asString
import adventofcode.matchNumbersLong
import adventofcode.readFile
import adventofcode.split
import adventofcode.transpose
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val part1 = transpose(lines.map { split(it) }).sumOf { line ->
        val op: (Long, Long) -> Long = if (line.last() == "+") Long::plus else Long::times
        line.dropLast(1).map { it.toLong() }.reduce { acc, next -> op(acc, next) }
    }
    println("part1=$part1")

    var part2 = 0L
    val cur = mutableListOf<Long>()
    transpose(lines.map { it.reversed().toList() }).forEach { line ->
        val fix = line.asString()
        cur += matchNumbersLong(fix)
        when (fix.last()) {
            '+' -> {
                part2 += cur.sum()
                cur.clear()
            }
            '*' -> {
                part2 += cur.fold(1L) { a, b -> a * b }
                cur.clear()
            }
        }
    }
    println("part2=$part2")
}
