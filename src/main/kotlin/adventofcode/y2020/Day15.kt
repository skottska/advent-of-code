package adventofcode.y2020 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val numbers = matchNumbers(readFile(MethodHandles.lookup()).first())
    println("part1=" + iterate(numbers, stop = 2020))
    println("part2=" + iterate(numbers, stop = 30_000_000))
}

private fun iterate(numbers: List<Int>, stop: Int): Int {
    val map = numbers.dropLast(1).mapIndexed { index, i -> i to index + 1 }.toMap().toMutableMap()
    return (numbers.size until stop).fold(numbers.last()) { last, i ->
        when (last) {
            !in map -> 0
            else -> i - map.getValue(last)
        }.also { map[last] = i }
    }
}
