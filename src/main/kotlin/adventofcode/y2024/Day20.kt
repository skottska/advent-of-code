package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.mapCoord
import adventofcode.matches
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val grid = readFile(MethodHandles.lookup()).mapCoord()
    val start = grid.filter { it.value == 'S' }.map { it.key }.first()
    val end = grid.filter { it.value == 'E' }.map { it.key }.first()
    val earliestVisitFromStart = earliestVisits(grid, start)
    val earliestVisitFromEnd = earliestVisits(grid, end)
    val baseScore = earliestVisitFromEnd.getValue(start)
    val paths = grid.filter { it.value != '#' }.map { it.key }
    val maxDiff = 100

    val wormholeFunc = { picoseconds: Int ->
        paths.flatMap { p1 ->
            paths.filter { p1.distance(it) <= picoseconds }.map {
                earliestVisitFromStart.getValue(p1) + p1.distance(it) + earliestVisitFromEnd.getValue(it)
            }.filter { it <= baseScore - maxDiff }
        }
    }
    println("part1=" + wormholeFunc(2).size)
    println("part2=" + wormholeFunc(20).size)
}

private fun earliestVisits(grid: Map<Coord, Char>, start: Coord): Map<Coord, Int> {
    val earliestVisit: MutableMap<Coord, Int> = mutableMapOf()
    var next = setOf(start)
    var steps = 0
    while (next.isNotEmpty()) {
        next.forEach { earliestVisit[it] = steps }
        next = next.flatMap { it.around() }.filter { it !in earliestVisit && grid.getOrDefault(it, '#') != '#' }.toSet()
        steps++
    }
    return earliestVisit
}
