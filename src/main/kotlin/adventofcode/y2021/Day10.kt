package adventofcode.y2021 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2021/day10.txt")
    val values1 = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    println("part1=" + lines.mapNotNull { findCorruptChar(it).second }.sumOf { values1.getValue(it) })

    val values2 = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
    val incomplete = lines.asSequence().map { findCorruptChar(it) }.filter { it.second == null }.map { it.first.reversed() }.map {
        it.fold(0L) { total, i -> total * 5L + values2.getValue(i) }
    }.sorted().toList()
    println("part2=" + incomplete[incomplete.size / 2])
}

private fun findCorruptChar(line: String): Pair<List<Char>, Char?> {
    val chars = mutableListOf<Char>()
    line.forEach {
        when (it) {
            '(' -> chars.add(')')
            '[' -> chars.add(']')
            '{' -> chars.add('}')
            '<' -> chars.add('>')
            chars.last() -> chars.removeLast()
            else -> return chars to it
        }
    }
    return chars to null
}
