package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.transpose

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2016/day03.txt").map { matchNumbers(it) }
    val isValidTriangle = { triangle: List<Int> -> triangle.sorted().let { it.take(2).sum() > it.last() } }
    println("part1=" + lines.count { isValidTriangle(it) })
    println("part2=" + transpose(lines).sumOf { line -> line.chunked(3).count { isValidTriangle(it) } })
}
