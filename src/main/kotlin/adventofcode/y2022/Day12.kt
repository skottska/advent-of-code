package adventofcode.y2022 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day12.txt").map { line -> line.map { it } }
    println("part1=" + findFewestSteps('S', lines))
    seen.clear()
    println("par2=" + findFewestSteps('a', lines))
}

private fun findFewestSteps(c: Char, lines: List<List<Char>>) =
    lines.mapIndexedNotNull { indexX, x ->
        x.mapIndexedNotNull { indexY, y -> if (y == c) Pair(indexX, indexY) else null }.firstOrNull()
    }.mapNotNull { move(lines, it, 0) }.minOrNull()

private val seen = mutableMapOf<Pair<Int, Int>, Int>()

private fun move(lines: List<List<Char>>, pos: Pair<Int, Int>, iter: Int): Int? {
    val currentHeight = lines[pos.first][pos.second]
    if (currentHeight == 'E') return iter
    if (seen[pos]?.let { it <= iter } == true) return null else seen[pos] = iter
    val directions = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)
    return directions.map { Pair(pos.first + it.first, pos.second + it.second) }
        .filter { newPos -> lines.getOrNull(newPos.first)?.getOrNull(newPos.second)?.let { replace(currentHeight) + 1 >= replace(it) } ?: false }
        .mapNotNull { move(lines, it, iter + 1) }
        .minOrNull()
}

private fun replace(c: Char) = when (c) {
    'S' -> 'a'
    'E' -> 'z'
    else -> c
}
