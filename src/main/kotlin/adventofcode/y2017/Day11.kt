package adventofcode.y2017 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val directions = readFile("src/main/resources/y2017/day11.txt")[0].split(",")
    println("part1=" + iterate(directions))
    println("part2=" + (directions.indices).maxOf { iterate(directions.subList(0, it)) })
}

private fun iterate(directions: List<String>): Int {
    val directionsMap = directions.groupBy { it }.map { it.key to it.value.size }.toMap().toMutableMap()
    var steps = steps(directionsMap)
    reduce(directionsMap)
    while (steps != steps(directionsMap)) {
        steps = steps(directionsMap)
        reduce(directionsMap)
    }
    return steps
}
private fun steps(directions: Map<String, Int>) = directions.map { it.value }.sum()

private fun reduce(directions: MutableMap<String, Int>) {
    listOf("n" to "s", "ne" to "sw", "nw" to "se").forEach {
        val a = directions.getOrDefault(it.first, 0)
        val b = directions.getOrDefault(it.second, 0)
        val min = minOf(a, b)
        directions[it.first] = a - min
        directions[it.second] = b - min
    }
    listOf(
        listOf("ne", "s") to "se",
        listOf("nw", "s") to "sw",
        listOf("se", "n") to "ne",
        listOf("sw", "n") to "nw",
        listOf("ne", "nw") to "n",
        listOf("se", "sw") to "s"
    ).forEach {
        val a = directions.getOrDefault(it.first.first(), 0)
        val b = directions.getOrDefault(it.first.last(), 0)
        val c = directions.getOrDefault(it.second, 0)
        val min = minOf(a, b)
        directions[it.first.first()] = a - min
        directions[it.first.last()] = b - min
        directions[it.second] = c + min
    }
}
