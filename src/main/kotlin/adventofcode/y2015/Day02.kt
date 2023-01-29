package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2015/day02.txt")
    lines.fold(0) { total, line ->
        val (l, w, h) = dimension(line)
        val side1 = l * h
        val side2 = h * w
        val side3 = l * w
        total + minOf(side1, side2, side3) + 2 * (side1 + side2 + side3)
    }.let { println("part1=$it") }

    lines.fold(0) { total, line ->
        val (l, w, h) = dimension(line)
        total + listOf(l, w, h).sorted().let { 2 * (it[0] + it[1]) } + (h * l * w)
    }.let { println("part2=$it") }
}

fun dimension(line: String) = matches(line, "[0-9]+").let { r -> Triple(r[0].toInt(), r[1].toInt(), r[2].toInt()) }
