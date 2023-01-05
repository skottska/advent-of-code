package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2017/day02.txt").map { matchNumbers(it) }
    println("part1=" + lines.sumOf { it.max() - it.min() })
    println("part2=" + lines.sumOf { line -> line.sumOf { a -> line.sumOf { b -> if (a > b && a % b == 0) a / b else 0 } } })
}
