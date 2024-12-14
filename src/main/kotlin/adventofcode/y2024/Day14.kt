package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.Equation
import adventofcode.matchNumbers
import adventofcode.matchNumbersLong
import adventofcode.printCoords
import adventofcode.product
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val robots = readFile(MethodHandles.lookup()).map { l -> matchNumbers(l).let { Robot(
        Coord(it[1], it[0]), Coord(it[3], it[2]),
    ) } }
    val max = Coord(103, 101)
    val moved1 = robots.map { it.move(100, max).c }

    val midRow = max.row / 2
    val midCol = max.col / 2
    val nums = listOf(
        0 until midRow to (0 until midCol),
        0 until midRow to midCol+1..max.col,
        midRow+1..max.row to (0 until midCol),
        midRow+1..max.row to midCol+1..max.col,
    ).map { q -> moved1.count { it.row in q.first && it.col in q.second } }
    println("part1="+nums.product())

    (0..10000).forEach { x ->
        val moved2 = robots.map { it.move(x, max).c }
        if (moved2.size == moved2.toSet().size) {
            printCoords(moved2) { c -> moved2.count { it == c }.toString().let { if (it == "0") "." else it } }
            println("part2=$x")
            return
        }
    }
}

private data class Robot(val c: Coord, val move: Coord) {
    fun move(seconds: Int, max: Coord): Robot =
        copy(c = Coord(
            row = next(c.row, seconds * move.row, max.row),
            col = next(c.col, seconds * move.col, max.col),
        ))

    private fun next(start: Int, move: Int, max: Int): Int {
        val next = (start + move) % max
        return if (next >= 0) next else (next + max)
    }
}