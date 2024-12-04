package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.asString
import adventofcode.mapCoord
import adventofcode.readFile
import adventofcode.transposeStrings
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val grid = lines.mapCoord()
    val part1 = grid.map { c ->
        listOf(Coord::right, Coord::left, Coord::down, Coord::up, Coord::upLeft, Coord::upRight, Coord::downRight, Coord::downLeft).map {
            (1..3).fold(listOf(c.key)) { total, _ -> total + it(total.last()) }
        }.count { compareToGrid(grid, it, "XMAS") }
    }.sum()
    println("part1=$part1")

    val grids = listOf(lines, transposeStrings(lines)).flatMap { listOf(it, it.reversed()) }.map { it.mapCoord() }
    val part2 = grids.sumOf { g ->
        g.count { c -> compareToGrid(g, listOf(c.key, c.key.upLeft(), c.key.upRight(), c.key.downLeft(), c.key.downRight()), "AMMSS") }
    }
    println("part2=$part2")
}

private fun compareToGrid(grid: Map<Coord, Char>, cs: List<Coord>, s: String): Boolean = cs.map { grid.getOrDefault(it, '.') }.asString() == s
