package adventofcode.y2022

import adventofcode.readFile

fun main() {
    val line = readFile("src/main/resources/y2022/day06.txt")[0]
    println("part1=" + answer(line, 4))
    println("part2=" + answer(line, 14))
}

fun answer(line: String, windowSize: Int): Int {
    line.windowed(windowSize, 1).forEachIndexed { index, window ->
        if (window.toSet().size == windowSize) return index + windowSize
    }
    return 0
}
