package adventofcode.y2021 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.split

fun main() {
    val lines = readFile("src/main/resources/y2021/day14.txt")
    val polymer = lines.first()
    val rules = lines.filter { it.contains('-') }.map { split(it) }.associate { it.first() to it.last() }
    val grouping = (1..10).fold(polymer) { total, i -> println(i); iterate(total, rules) }.groupBy { it }.map { it.key to it.value.size }
    println("part1=" + (grouping.maxOf { it.second } - grouping.minOf { it.second }))
    val grouping2 = (1..40).fold(polymer) { total, i -> println(i); iterate(total, rules) }.groupBy { it }.map { it.key to it.value.size }
    println("part2=" + (grouping2.maxOf { it.second } - grouping2.minOf { it.second }))
}

private fun iterate(polymer: String, rules: Map<String, String>): String {
    println("sizey=" + polymer.length)
    return polymer.windowed(size = 2, step = 1).map { it.first() + rules.getValue(it) }.fold("") { total, i -> total + i } + polymer.last()
}
