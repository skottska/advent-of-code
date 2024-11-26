package adventofcode.y2020 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.split
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val bags = lines.associate { line ->
        val split = split(line)
        val name = split[0] + " " + split[1]
        if (line.contains("contain no other bags.")) name to emptyList()
        else name to split.drop(4).windowed(size = 4, step = 4).map { it.first().toInt() to (it[1] + " " + it[2]) }.toList()
    }
    println("part1=" + contains(bags, "shiny gold").size)
    println("part2=" + contents(bags, "shiny gold"))
}

private fun contains(bags: Map<String, List<Pair<Int, String>>>, find: String): Set<String> {
    val matches = bags.filter { it.value.any { b -> b.second == find } }
    return matches.keys + matches.map { contains(bags, it.key) }.flatten()
}

private fun contents(bags: Map<String, List<Pair<Int, String>>>, find: String): Int = bags.getValue(find).sumOf { it.first + it.first * contents(bags, it.second) }
