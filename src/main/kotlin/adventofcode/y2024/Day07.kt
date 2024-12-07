package adventofcode.y2024 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { matchNumbersLong(it) }.map { it.first() to it.drop(1) }
    println("part1="+lines.filter { solve(it.first, it.second, false) }.sumOf { it.first })
    println("part2="+lines.filter { solve(it.first, it.second, true) }.sumOf { it.first })

}

private fun solve(a: Long, b: List<Long>, join: Boolean): Boolean {
    if (a < 0) return false
    if (b.size == 1) return a == b.first()
    val options = mutableListOf(a - b.last())
    if (a % b.last() == 0L) options += a / b.last()
    if (join) {
        val lastString = b.last().toString()
        if (a != b.last() && a.toString().endsWith(lastString)) options += a.toString().dropLast(lastString.length).toLong()
    }
    return options.any { solve(it, b.dropLast(1), join) }
}