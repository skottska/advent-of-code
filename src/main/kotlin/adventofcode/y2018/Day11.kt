package adventofcode.y2018 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val serial = matchNumbers(readFile("src/main/resources/y2018/day11.txt")[0])[0]

    val grid = (1..300).flatMap { row ->
        (1..300).map { col ->
            val rack = col + 10
            val power = (rack * row + serial) * rack
            val result = (power / 100).toString().last().digitToInt() - 5
            Coord(row, col) to result
        }
    }.toMap()

    val part1 = squarePower(3, grid)
    println("part1=" + part1.first.col + "," + part1.first.row)

    val part2 = (1..300).map { it to squarePower(it, grid) }.maxBy { it.second.second }
    println("part2=" + part2.second.first.col + "," + part2.second.first.row + "," + part2.first)
}

private fun squarePower(size: Int, grid: Map<Coord, Int>) = (1..300 - size + 1).map { row ->
    val maxCol = (1..300).map { col ->
        col to (row until row + size).sumOf { grid.getValue(Coord(it, col)) }
    }.windowed(size).map { l ->
        l.first().first to l.sumOf { it.second }
    }.maxBy { it.second }
    Coord(row, maxCol.first) to maxCol.second
}.maxBy { it.second }
