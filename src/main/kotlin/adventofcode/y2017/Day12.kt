package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2017/day12.txt").flatMap { line ->
        val numbers = matchNumbers(line)
        numbers.drop(1).map { numbers.first() to it }
    }
    println("part1=" + canCommunicate(lines, setOf(0), setOf(0)).size)
    val all = lines.map { it.first }.toMutableList()
    var count = 0
    while (all.isNotEmpty()) {
        count++
        all.removeAll(canCommunicate(lines, setOf(all.first()), setOf(all.first())))
    }
    println("part2=$count")
}

private fun canCommunicate(all: List<Pair<Int, Int>>, seen: Set<Int>, from: Set<Int>): Set<Int> {
    val new = from.flatMap { f -> all.filter { it.first == f }.map { it.second } + all.filter { it.second == f }.map { it.first } }.toSet() - seen
    return if (new.isEmpty()) seen
    else canCommunicate(all, seen + new, new)
}
