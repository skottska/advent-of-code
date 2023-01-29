package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile
import adventofcode.split

fun main() {
    val lines = readFile("src/main/resources/y2015/day06.txt")
    val square = 1000
    var result = 0
    repeat(square) { i -> result += lightCommandsByRow(lines, i, true).filter { it == 1 }.size }
    println("part1=$result")
    result = 0
    repeat(square) { i -> result += lightCommandsByRow(lines, i, false).sumOf { it } }
    println("part2=$result")
}

fun lightCommandsByRow(lines: List<String>, index: Int, isPart1: Boolean): List<Int> {
    val row = MutableList(1000) { 0 }
    lines.forEach { it ->
        val line = if (it.contains("toggle")) ("aa $it") else it
        val split = split(line)
        val pos1 = matches(split[2], "[0-9]+").map { it.toInt() }
        val pos2 = matches(split[4], "[0-9]+").map { it.toInt() }

        if (inBetweenOnX(index, pos1, pos2)) {
            when {
                split[1] == "toggle" -> toggle(row, pos1, pos2, isPart1)
                split[1] == "on" -> on(row, pos1, pos2, isPart1)
                else -> off(row, pos1, pos2, isPart1)
            }
        }
    }
    return row
}

fun off(row: MutableList<Int>, pos1: List<Int>, pos2: List<Int>, isPart1: Boolean) {
    (minOf(pos1[1], pos2[1])..maxOf(pos1[1], pos2[1])).forEach {
        row[it] = if (isPart1) 0 else maxOf(0, row[it] - 1)
    }
}

fun on(row: MutableList<Int>, pos1: List<Int>, pos2: List<Int>, isPart1: Boolean) {
    (minOf(pos1[1], pos2[1])..maxOf(pos1[1], pos2[1])).forEach {
        row[it] = if (isPart1) 1 else row[it] + 1
    }
}

fun toggle(row: MutableList<Int>, pos1: List<Int>, pos2: List<Int>, isPart1: Boolean) {
    (minOf(pos1[1], pos2[1])..maxOf(pos1[1], pos2[1])).forEach {
        row[it] = when {
            isPart1 && row[it] == 1 -> 0
            isPart1 -> 1
            else -> row[it] + 2
        }
    }
}

fun inBetweenOnX(index: Int, pos1: List<Int>, pos2: List<Int>) =
    when {
        (index >= pos1.first() && index <= pos2.first()) -> true
        (index >= pos2.first() && index <= pos1.first()) -> true
        else -> false
    }
