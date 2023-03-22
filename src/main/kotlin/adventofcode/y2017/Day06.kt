package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    var banks = readFile("src/main/resources/y2017/day06.txt").map { matchNumbers(it) }.flatten()
    val seen = mutableSetOf<List<Int>>()
    var moves = 0
    while (!seen.contains(banks)) {
        seen.add(banks)
        moves++
        val max = banks.max()
        val maxPos = banks.indexOf(max)
        banks = banks.mapIndexed { index, i ->
            val posDiff = (index - maxPos).let { if (it > 0) it else it + banks.size }
            max / banks.size + when {
                index == maxPos -> 0
                max % banks.size >= posDiff -> 1 + i
                else -> i
            }
        }
    }
    println("part1=$moves")
    println("part2=" + (seen.size - seen.indexOf(banks)))
}
