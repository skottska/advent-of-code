package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2016/day04.txt")
    val part1 = lines.sumOf { line ->
        val alphas = matches(line, "[a-z\\-]+")
        val name = alphas.first().filter { it != '-' }
        val countFunc = { c: Char -> name.count { it == c } }
        val validChecksum = name.toSet().sortedWith { a, b ->
            when {
                countFunc(a) > countFunc(b) -> -1
                countFunc(b) > countFunc(a) -> 1
                else -> a.compareTo(b)
            }
        }.take(5)
        if (validChecksum == alphas.last().toList()) matches(line, "[0-9]+").first().toInt() else 0
    }
    println("part1=$part1")

    lines.forEach { line ->
        val sector = matches(line, "[0-9]+").first().toInt()
        val res = matches(line, "[a-z\\-]+").first().fold("") { total, i ->
            total + if (i == '-') ' ' else decrypt(i, sector)
        }.trim()
        if (res == "northpole object storage") println("part2=$sector")
    }
}

private fun decrypt(c: Char, move: Int): Char {
    val movedCode = c.code + move % 26
    return Char(if (movedCode > 'z'.code) movedCode - 26 else movedCode)
}
