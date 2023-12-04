package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2017/day13.txt").map { line ->
        matchNumbers(line).let { it.first() to it.last() }
    }
    val part1 = lines.sumOf {
        val pos = it.first % (it.second + it.second - 2)
        if (pos == 0) it.first * it.second else 0
    }
    println("part1=$part1")

    var delay = 0
    while (lines.any { (delay + it.first) % (it.second + it.second - 2) == 0 }) delay++
    println("part2=$delay")
}
