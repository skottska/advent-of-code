package adventofcode.y2016 // ktlint-disable filename

import adventofcode.asString
import adventofcode.readFile

fun main() {
    val input = readFile("src/main/resources/y2016/day18.txt")[0]

    val maze = (2..40).fold(listOf(input)) { total, _ -> total + nextMazeLine(total.last()) }
    println("part1=" + maze.sumOf { it.count { c -> c == '.' } })
    val maze2 = (2..400000).fold(input to 0) { total, _ ->
        nextMazeLine(total.first) to total.second + total.first.count { it == '.' }
    }
    println("part2=" + (maze2.second + maze2.first.count { it == '.' }))
}

private fun nextMazeLine(prev: String): String {
    return (".$prev.").windowed(size = 3, step = 1).map {
        when (it) {
            "^^." -> '^'
            ".^^" -> '^'
            "^.." -> '^'
            "..^" -> '^'
            else -> '.'
        }
    }.asString()
}
