package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.readFile
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2019/day03.txt")
    val start = Coord(0, 0)
    val wires = lines.map { it.split(',') }.map {
        it.fold(listOf(start)) { total, i ->
            val num = i.substring(1).toInt()
            val move = when (i.first()) {
                'D' -> 0 to -1
                'U' -> 0 to 1
                'R' -> 1 to 0
                else -> -1 to 0
            }
            total + total.last().let { last ->
                last.copy(
                    row = last.row + num * move.first,
                    col = last.col + num * move.second
                )
            }
        }.windowed(size = 2, step = 1).map { cs -> Line(cs.first(), cs.last()) }
    }
    val meetings = wires.first().flatMap { a -> wires.last().flatMap { it.intersection(a) } }.filter { it != start }
    println("part1=" + meetings.minOf { abs(it.row) + abs(it.col) })
    println("part2=" + meetings.minOf { distance(it, wires.first()) + distance(it, wires.last()) })
}

private fun distance(c: Coord, wire: List<Line>, index: Int = 0): Int = when {
    Line(c, c).intersection(wire[index]).isEmpty() -> wire[index].len() + distance(c, wire, index + 1)
    else -> Line(wire[index].a, c).len()
}

private data class Line(val a: Coord, val b: Coord) {
    private fun maxCol() = max(a.col, b.col)
    private fun maxRow() = max(a.row, b.row)
    private fun minCol() = min(a.col, b.col)
    private fun minRow() = min(a.row, b.row)
    private fun eqCol() = a.col == b.col
    private fun eqRow() = a.row == b.row
    fun len() = abs(a.row - b.row) + abs(a.col - b.col)
    fun intersection(l: Line) = when {
        eqRow() && l.eqRow() -> horizontalIntersection(l)
        eqCol() && l.eqCol() -> verticalIntersection(l)
        eqRow() -> mixIntersection(l)
        else -> l.mixIntersection(this)
    }

    private fun mixIntersection(vertical: Line) = when {
        a.row < vertical.minRow() || a.row > vertical.maxRow() -> emptyList()
        maxCol() < vertical.a.col || minCol() > vertical.a.col -> emptyList()
        else -> listOf(Coord(a.row, vertical.a.col))
    }

    private fun verticalIntersection(l: Line) = when {
        a.col != l.a.col -> emptyList()
        else -> (max(minRow(), l.minRow())..min(maxRow(), l.maxRow())).map { Coord(a.col, it) }
    }

    private fun horizontalIntersection(l: Line) = when {
        a.row != l.a.row -> emptyList()
        else -> (max(minCol(), l.minCol())..min(maxCol(), l.maxCol())).map { Coord(a.row, it) }
    }
}
