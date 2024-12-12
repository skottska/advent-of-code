package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.mapCoord
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val grid = readFile(MethodHandles.lookup()).mapCoord()
    val regions = regions(grid)
    println("part1=" + regions.sumOf { it.size * perimeter(it) })
    println("part2=" + regions.sumOf { it.size * sides(it) })
}

private fun regions(grid: Map<Coord, Char>): List<Set<Coord>> = grid.entries.fold(emptyList()) { total, entry ->
    if (total.none { entry.key in it }) {
        total + listOf(findRegion(entry.value, setOf(entry.key), setOf(entry.key), grid))
    } else total
}

private fun sides(s: Set<Coord>): Int =
    listOf(Coord::up, Coord::down, Coord::left, Coord::right).sumOf { dir ->
        val around = s.map { dir.invoke(it) }.filter { it !in s }.toSet()
        regions(around.associateWith { 'a' }).count()
    }

private fun perimeter(s: Set<Coord>): Int = s.sumOf { c -> 4 - c.around().count { it in s } }

private fun findRegion(c: Char, newFound: Set<Coord>, total: Set<Coord>, grid: Map<Coord, Char>): Set<Coord> {
    if (newFound.isEmpty()) return total
    val around = newFound.flatMap { it.around() }.filter { it !in total && grid.getOrDefault(it, '.') == c }.toSet()
    return findRegion(c, around, total + around, grid)
}