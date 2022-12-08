package adventofcode.y2022 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val elves = mutableListOf(0)
    readFile("src/main/resources/y2022/day1.txt").forEach { line ->
        if (line.isNotEmpty()) elves[elves.size - 1] += line.toInt()
        else elves.add(0)
    }
    elves.toSortedSet().reversed().let { sortedElves ->
        println("part1=" + sortedElves.first())
        println("part2=" + sortedElves.take(3).sumOf { it })
    }
}
