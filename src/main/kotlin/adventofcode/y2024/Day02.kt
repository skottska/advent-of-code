package adventofcode.y2024 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { matchNumbers(it) }.flatMap { listOf(it, it.reversed()) }
    println("part1=" + lines.count { part1(it) })
    println("part2=" + lines.count { part2(it) })
}

private fun part1(l: List<Int>): Boolean {
    l.windowed(size = 2, step = 1, partialWindows = false).forEach {
        if (it.last() - it.first() !in listOf(1, 2, 3)) return false
    }
    return true
}

private fun part2(l: List<Int>): Boolean = l.indices.any { part1(l.filterIndexed { index, _ -> index != it }) }
