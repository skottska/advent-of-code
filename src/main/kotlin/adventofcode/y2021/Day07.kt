package adventofcode.y2021 // ktlint-disable filename

import adventofcode.anyRange
import adventofcode.matchNumbers
import adventofcode.readFile
import kotlin.math.abs

fun main() {
    val crabs = matchNumbers(readFile("src/main/resources/y2021/day07.txt")[0])
    println("part1=" + anyRange(crabs).minOf { pos -> crabs.sumOf { abs(pos - it) } })
    println("part2=" + anyRange(crabs).minOf { pos -> crabs.sumOf { sumAll(abs(pos - it)) } })
}

private fun sumAll(sum: Int) = ((sum.toDouble() / 2) * (sum + 1)).toInt()
