package adventofcode.y2021 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val routes = readFile("src/main/resources/y2021/day12.txt").map {
        val split = it.indexOf('-')
        val before = it.substring(0, split)
        val after = it.substring(split + 1)
        listOf(before to after, after to before)
    }.flatten()
    println("part1=" + iterate(routes, listOf("start"), disallowDoubleSmallCaves = true))
    println("part2=" + iterate(routes, listOf("start"), disallowDoubleSmallCaves = false))
}

private fun iterate(routes: List<Pair<String, String>>, curPath: List<String>, disallowDoubleSmallCaves: Boolean): Int {
    if (curPath.last() == "end") return 1
    val doubleSmallCave = disallowDoubleSmallCaves || curPath.filter { it.lowercase() == it }.let { it.size != it.toSet().size }
    return routes.asSequence().filter { it.first == curPath.last() }
        .map { it.second }
        .filter { it != "start" }
        .filter { it.lowercase() != it || it !in curPath || !doubleSmallCave }
        .map { iterate(routes, curPath + it, disallowDoubleSmallCaves) }
        .sum()
}
