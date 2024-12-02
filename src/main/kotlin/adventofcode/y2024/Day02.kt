package adventofcode.y2024 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { matchNumbers(it) }.flatMap { listOf(it, it.reversed()) }
    println("part1=" + lines.count { part1(it) })
    println("part2=" + lines.count { part2(it) })
}

private fun part1(l: List<Int>) = l.zipWithNext { a, b -> (b - a) in 1..3 }.all { it }
private fun part2(l: List<Int>) = l.indices.any { part1(l.filterIndexed { i, _ -> i != it }) }
