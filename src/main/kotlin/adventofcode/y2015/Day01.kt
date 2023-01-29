package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val line = readFile("src/main/resources/y2015/day01.txt")[0]
    println("part1=" + (line.filter { it == '(' }.length - line.filter { it == ')' }.length))
    var pos = 0
    line.forEachIndexed { index, c ->
        if (c == '(') pos++ else pos--
        if (pos < 0) {
            println("part2=" + (index + 1))
            return
        }
    }
}
