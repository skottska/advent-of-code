package adventofcode.y2016 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.transpose

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2016/day06.txt")
    val transpose = transpose(lines.map { it.toList() })
    println("part1=" + transpose.fold("") { t, line -> t + line.groupBy { it }.maxBy { it.value.size }.key })
    println("part2=" + transpose.fold("") { t, line -> t + line.groupBy { it }.minBy { it.value.size }.key })
}
