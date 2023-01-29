package adventofcode.y2019 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2019/day01.txt").map { it.toInt() }
    println("part1=" + lines.sumOf { it / 3 - 2 })
    println("part2=" + lines.sumOf { calculateFuel(it) })
}

private fun calculateFuel(mass: Int): Int = (mass / 3 - 2).let { if (it <= 0) 0 else it + calculateFuel(it) }
