package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.matches
import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2017/day05.txt").map { matchNumbers(it).first() }
    println("part1="+ move(lines.toMutableList(), isPart2 = false))
    println("part2="+ move(lines.toMutableList(), isPart2 = true))
}

private fun move(lines: MutableList<Int>, isPart2: Boolean): Int {
    var index = 0
    var moves = 0
    while (index >= 0 && index < lines.size) {
        val cur = lines[index]
        lines[index] = if (cur >=3 && isPart2) cur - 1 else cur + 1
        index += cur
        moves++
    }
    return moves
}
