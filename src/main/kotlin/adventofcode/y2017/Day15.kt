package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.readFile
import kotlin.math.pow

fun main() {
    val inputs = readFile("src/main/resources/y2017/day15.txt").map { matchNumbersLong(it).first() }
    println("part1=" + matchingBits(40_000_000, inputs, listOf(1, 1)))
    println("part2=" + matchingBits(5_000_000, inputs, listOf(4, 8)))
}

private fun matchingBits(loops: Int, inputs: List<Long>, remainders: List<Int>): Long {
    val remainder = 2.0.pow(16.0).toLong()
    var found = 0L
    (1..loops).fold(inputs) { total, _ ->
        val a = iterate(total.first(), 16807L, remainders.first())
        val b = iterate(total.last(), 48271L, remainders.last())
        if (a % remainder == b % remainder) found++
        listOf(a, b)
    }
    return found
}
private fun iterate(a: Long, multiplier: Long, remainder: Int): Long {
    var r = a * multiplier % 2147483647L
    while (r % remainder != 0L) r = r * multiplier % 2147483647L
    return r
}
