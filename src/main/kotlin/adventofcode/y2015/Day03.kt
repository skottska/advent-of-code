package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val line = readFile("src/main/resources/y2015/day03.txt")[0]
    println("part1=" + move(line) { true }.size)
    val santa = move(line) { i -> i % 2 == 0 }
    val robotSanta = move(line) { i -> i % 2 != 0 }
    println("part2=" + (santa + robotSanta).size)
}

fun move(line: String, func: (Int) -> Boolean): Set<Pair<Int, Int>> {
    val start = Pair(0, 0)
    val result = mutableSetOf(start)
    line.foldIndexed(start) { index, pos, c ->
        if (func(index)) {
            when (c) {
                '^' -> pos.copy(first = pos.first + 1)
                'v' -> pos.copy(first = pos.first - 1)
                '>' -> pos.copy(second = pos.second + 1)
                else -> pos.copy(second = pos.second - 1)
            }.also { result.add(it) }
        } else pos
    }
    return result
}
