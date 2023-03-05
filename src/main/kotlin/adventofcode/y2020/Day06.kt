package adventofcode.y2020 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2020/day06.txt")
    val groups = mutableListOf<List<String>>(emptyList())
    lines.forEach { line ->
        if (line.isEmpty()) groups.add(emptyList())
        else groups[groups.size - 1] = groups[groups.size - 1] + line
    }
    println("part1=" + groups.sumOf { it.joinToString("").toSet().size })
    println("part2=" + groups.sumOf { it.reduce { total, i -> total.filter { t -> t in i } }.length })
}
