package adventofcode.y2017 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val line = readFile("src/main/resources/y2017/day01.txt")[0].map { it.digitToInt() }
    println("part1=" + (line + line.first()).windowed(size = 2, step = 1).sumOf { i -> if (i[0] == i[1]) i[0] else 0 })
    println("part2=" + line.mapIndexed { index, i -> if (i == line[(index + line.size / 2) % line.size]) i else 0 }.sum())
}
