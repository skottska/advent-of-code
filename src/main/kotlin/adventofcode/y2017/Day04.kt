package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2017/day04.txt").map { matches(it, "[a-z]+") }
    println("part1=" + lines.count { it.size == it.toSet().size })
    println("part2=" + lines.count { line -> line.map { it.toCharArray().sorted() }.let { it.size == it.toSet().size } })
}
