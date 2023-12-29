package adventofcode.y2023

import adventofcode.Coord
import adventofcode.Edge
import adventofcode.findLongestDistance
import adventofcode.mapCoord
import adventofcode.readFile

fun main() {
    val file = readFile("src/main/resources/y2023/day23.txt")
    val grid = file.mapCoord()

    val start = Coord(0, file[0].indexOf('.'))
    val end = Coord(file.size - 1, file.last().indexOf('.'))
    println("part1=" + iterate(start, end, grid))

    val nodes = grid.filter { it.value != '#' && it.key.around().count { a -> grid.getOrDefault(a, '#') != '#' } > 2 }.map { it.key } + start + end
    val edges = nodes.flatMap { edges(it, grid, listOf(start, end)) }
    println("part2=" + findLongestDistance(edges, start, end))
}

fun edges(c: Coord, grid: Map<Coord, Char>, ends: List<Coord>): List<Edge> = safeAround(c, grid).mapNotNull { edge(it, c, grid, ends) }

fun edge(first: Coord, start: Coord, grid: Map<Coord, Char>, ends: List<Coord>): Edge? {
    val seen = mutableListOf(start, first)
    var around = safeAround(first, grid).filter { it !in seen }
    while (around.size < 2 && (seen.isEmpty() || seen.last() !in ends)) {
        if (around.isEmpty()) return null
        val next = around[0]
        seen += next
        around = safeAround(next, grid).filter { it !in seen }
    }
    return Edge(start, seen.last(), seen.size - 1)
}

fun safeAround(c: Coord, grid: Map<Coord, Char>): List<Coord> = c.around().filter { grid.getOrDefault(it, '#') != '#' }

private val cache = mutableMapOf<Coord, Int?>()
fun iterate(cur: Coord, end: Coord, grid: Map<Coord, Char>, seen: Set<Coord> = emptySet()): Int? = cache.getOrPut(cur) {
    if (cur == end) return 0
    val possible = when (grid.getValue(cur)) {
        '.' -> cur.around()
        '^' -> listOf(cur.up())
        '>' -> listOf(cur.right())
        '<' -> listOf(cur.left())
        'v' -> listOf(cur.down())
        else -> emptyList()
    }

    val next = possible.filter { it !in seen && grid.getOrDefault(it, '#') != '#' }
    val best = next.mapNotNull { iterate(it, end, grid, seen + cur) }.maxOrNull()
    best?.let { it + 1 }
}
