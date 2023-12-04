package adventofcode.y2016 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.readFile

fun main() {
    val maze = readFile("src/main/resources/y2016/day24.txt").mapIndexed { row, s ->
        s.mapIndexed { col, c ->
            Coord(row, col) to c
        }
    }.flatten().toMap()

    val numbers = maze.filter { it.value !in listOf('.', '#') }
    val start = numbers.entries.first { it.value == '0' }.key
    val others = numbers.map { it.key }.filter { it != start }
    val fromStart = others.associateWith { minBetween(start, it, maze) }
    val betweenOthers = others.flatMap { others.filter { b -> it != b }.map { b -> (it to b) to minBetween(it, b, maze) } }.toMap()
    val otherDistance = generateRoutes(others).map { route -> route to route.windowed(size = 2).sumOf { betweenOthers.getValue(it.first() to it.last()) } }
    println("part1=" + otherDistance.minOf { fromStart.getValue(it.first.first()) + it.second })
    println("part2=" + otherDistance.minOf { fromStart.getValue(it.first.first()) + it.second + fromStart.getValue(it.first.last()) })
}

private fun generateRoutes(all: List<Coord>, route: List<Coord> = emptyList()): List<List<Coord>> {
    return if (route.size == all.size) return listOf(route)
    else all.filter { it !in route }.flatMap { generateRoutes(all, route + it) }
}

private fun minBetween(from: Coord, to: Coord, maze: Map<Coord, Char>): Int {
    var cur = setOf(from)
    val been = mutableSetOf(from)
    var moves = 0

    while (to !in cur) {
        moves++
        cur = cur.flatMap { it.around() }.filter { maze.getOrDefault(it, '#') != '#' }.filter { it !in been }.toSet()
        been += cur
    }
    return moves
}
