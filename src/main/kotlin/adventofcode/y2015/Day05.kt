package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2015/day05.txt")
    println("part1=" + lines.filter { isNicePart1(it) }.size)
    println("part2=" + lines.filter { isNicePart2(it) }.size)
}

fun isNicePart1(line: String) = when {
    listOf("ab", "cd", "pq", "xy").any { line.contains(it) } -> false
    line.filter { "aeiou".contains(it) }.length < 3 -> false
    ('a'..'z').any { line.contains("" + it + it) } -> true
    else -> false
}

fun isNicePart2(line: String) = when {
    !inTheMiddle(line) -> false
    !duplicatePair(line) -> false
    else -> true
}

fun inTheMiddle(line: String) = line.windowed(3, 1).any { it[0] == it[2] }

fun duplicatePair(line: String) = line.windowed(3, 1).any { it.any { c -> it.first() != c } && matches(line, "" + it[0] + it[1]).size > 1 }
