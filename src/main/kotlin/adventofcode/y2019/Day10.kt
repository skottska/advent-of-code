package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.anyRange
import adventofcode.readFile
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.min

fun main() {
    val asteroids = readFile("src/main/resources/y2019/day10.txt").mapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, point -> if (point == '.') null else Coord(rowIndex, colIndex) }
    }.flatten().filterNotNull().toMutableList()

    val max = asteroids.map { it to canSee(it, asteroids).size }.maxBy { it.second }
    println("part1=" + max.second)

    var numDestroyed = 0
    while (true) {
        val canSee = canSee(max.first, asteroids).sortedBy {
            val extras = when {
                it.row > 0 && it.col <= 0 -> 0
                it.row <= 0 && it.col < 0 -> 90
                it.row < 0 -> 180
                else -> 270
            }
            extras + atan(
                when {
                    it.row == 0 -> 0.0
                    extras < 180 -> abs(it.col).toDouble() / abs(it.row).toDouble()
                    else -> 1 - abs(it.col).toDouble() / abs(it.row).toDouble()
                }
            )
        }

        if (numDestroyed + canSee.size >= 200) {
            println("part2=" + canSee[200 - numDestroyed - 1].let { (max.first.col - it.col) * 100 + (max.first.row - it.row) })
            break
        }
        numDestroyed += canSee.size
        asteroids.removeAll(canSee.map { Coord(max.first.row - it.row, max.first.col - it.col) })
    }
}

private fun canSee(a: Coord, asteroids: List<Coord>): List<Coord> {
    return asteroids.filter { it != a }.map { Coord(a.row - it.row, a.col - it.col) }
        .sortedBy { abs(it.row) + abs(it.col) }
        .fold(emptyList()) { total, i -> if (total.any { inFront(i).contains(it) }) total else total + listOf(i) }
}

private fun inFront(c: Coord) = when {
    abs(c.row) == 1 || abs(c.col) == 1 -> emptyList()
    c.row == 0 -> anyRange(0, c.col).map { Coord(0, it) }
    c.col == 0 -> anyRange(0, c.row).map { Coord(it, 0) }
    else -> {
        val maxDivisor = (2..min(abs(c.row), abs(c.col))).reversed().firstOrNull { c.row % it == 0 && c.col % it == 0 }
        if (maxDivisor == null) emptyList()
        else {
            val diffCoord = Coord(c.row / maxDivisor, c.col / maxDivisor)
            (1..maxDivisor).map { Coord(c.row - diffCoord.row * it, c.col - diffCoord.col * it) }
        }
    }
}
