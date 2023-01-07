package adventofcode.y2021 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2021/day01.txt").map { it.toInt() }
    val func = { l : List<Int> -> l.windowed(size = 2, step = 1).count { it.first() < it.last() } }
    println("part1="+func(lines))
    println("part2="+func(lines.windowed(size = 3, step = 1).map { w -> w.sumOf { it } }))
}
