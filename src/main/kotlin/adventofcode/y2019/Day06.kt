package adventofcode.y2019 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val map = mutableMapOf<String, List<String>>()
    readFile("src/main/resources/y2019/day06.txt").map { it.take(3) to it.takeLast(3) }.forEach {
        map[it.first] = map.getOrDefault(it.first, mutableListOf()) + it.second
    }
    println("part1=" + paths(map, 0, "COM"))
    println("par21=" + (find(map, "YOU", "SAN") - 2))
}

private fun paths(map: Map<String, List<String>>, depth: Int, cur: String): Long =
    depth + map.getOrDefault(cur, emptyList()).sumOf { paths(map, depth + 1, it) }

private fun find(map: Map<String, List<String>>, cur: String, find: String): Int =
    howDeep(map, cur, find) ?: (1 + find(map, map.filter { it.value.contains(cur) }.keys.first(), find))

private fun howDeep(map: Map<String, List<String>>, cur: String, find: String): Int? =
    if (cur == find) 0 else map.getOrDefault(cur, emptyList()).mapNotNull { howDeep(map, it, find) }.minOfOrNull { it + 1 }
