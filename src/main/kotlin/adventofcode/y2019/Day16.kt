package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import kotlin.math.absoluteValue
import kotlin.math.min

fun main() {
    val line = readFile("src/main/resources/y2019/day16.txt")[0]
    val input = line.map { it.digitToInt() }
    println("part1=" + (1..100).fold(input) { total, _ -> iterate(total) }.take(8).joinToString(""))

    val startIndex = matchNumbers(line.substring(0..6)).first()
    val part2Input = List(size = 10_000 * input.size) { i: Int -> input[i % input.size] }.drop(startIndex).toMutableList()
    println("part2=" + part2(part2Input))
}

private fun part2(l: MutableList<Int>): String {
    repeat(100) {
        l.indices.reversed().fold(0) { total, i ->
            ((total + l[i]) % 10).also { l[i] = it }
        }
    }
    return l.take(8).joinToString("")
}

private fun iterate(l: List<Int>) = List(l.size) { repeat -> (generateBase(repeat + 1, l) % 10).absoluteValue }

private fun generateBase(repeat: Int, l: List<Int>): Int {
    var result = 0
    var index = 0
    while (index <= l.size) {
        val plusRangeStart = repeat + index - 1
        if (plusRangeStart < l.size) result += l.subList(plusRangeStart, min(l.size, plusRangeStart + repeat)).sum()
        val minusRangeStart = 3 * repeat + index - 1
        if (minusRangeStart < l.size) result -= l.subList(minusRangeStart, min(l.size, minusRangeStart + repeat)).sum()
        index += repeat * 4
    }
    return result
}
