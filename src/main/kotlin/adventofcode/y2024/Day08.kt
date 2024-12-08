package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.mapCoord
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val grid = readFile(MethodHandles.lookup()).mapCoord()
    println("part1=" + calculateAntidotes(grid, findAll = false))
    println("part2=" + calculateAntidotes(grid, findAll = true))
}

private fun calculateAntidotes(grid: Map<Coord, Char>, findAll: Boolean) = (grid.values.toSet() - '.').flatMap { antenna ->
    val coords = grid.filter { it.value == antenna }.map { it.key }
    coords.flatMap { coord ->
        (coords - coord).flatMap { other ->
            val rowOffset = (other.row - coord.row)
            val colOffset = (other.col - coord.col)
            var c = Coord(coord.row - rowOffset, coord.col - colOffset)
            if (!findAll) listOf(c)
            else {
                val answers = mutableListOf(coord)
                while (c in grid) {
                    answers += c
                    c = Coord(c.row - rowOffset, c.col - colOffset)
                }
                answers
            }
        }
    }
}.toSet().filter { it in grid }.size
