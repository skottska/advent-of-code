package adventofcode.y2017 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.readFile
import java.util.*

fun main() {
    val input = readFile("src/main/resources/y2017/day14.txt").first()

    Year2017.knotHash("flqrgnkx-2")

    val grid = (0..127).map { line ->
        Year2017.knotHash("$input-$line").map {
            val byte = it.toString().toUInt(16).toString(radix = 2)
            (byte.length until 4).fold(byte) { total, _ -> "0$total" }
        }.reduce { a, b -> a + b }
    }
    println("part1=" + grid.sumOf { it.count { c -> c == '1' } })

    val coords = grid.mapIndexed { row, line ->
        line.mapIndexedNotNull { col, c ->
            if (c == '1') Coord(row, col) else null
        }
    }.flatten().toSet()

    val seen = mutableSetOf<Coord>()
    var regions = 0
    while (seen.size != coords.size) {
        regions++
        seen += region(coords, setOf(coords.first { it !in seen }))
    }
    println("part2=$regions")
}

private fun region(grid: Set<Coord>, from: Set<Coord>, region: Set<Coord> = emptySet()): Set<Coord> {
    val new = from.flatMap { it.around() }.filter { it in grid && it !in region && it !in from }.toSet()
    return if (new.isEmpty()) region + from
    else region(grid, new, region + new + from)
}
