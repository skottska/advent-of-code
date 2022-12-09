package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

fun main(args: Array<String>) {
    val line = readFile("src/main/resources/y2015/day12.txt")[0]
    println("part1=" + matches(line, "-?[0-9]+").sumOf { it.toInt() })
}
