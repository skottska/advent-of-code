package adventofcode.y2018 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2018/day01.txt").map { matchNumbers(it).first() }
    println("part1=" + lines.sum())
    var cur = 0
    val seen = mutableSetOf(cur)
    while (true) {
        lines.forEach { i ->
            cur += i
            if (seen.contains(cur)) { println("part2=$cur"); return }
            seen.add(cur)
        }
    }
}
