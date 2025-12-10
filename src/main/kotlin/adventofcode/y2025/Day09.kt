package adventofcode.y2025 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.anyRange
import adventofcode.mapWithOthers
import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val redTiles = readFile(MethodHandles.lookup()).map { line -> matchNumbers(line).let { Coord(it.last(), it.first()) } }
    val boxes = redTiles.mapWithOthers { a, b -> (a to b) to (abs(a.col - b.col) + 1L) * (abs(a.row - b.row) + 1L) }
    println("part1=" + boxes.map { it.second }.max())

    val redLines = (redTiles + redTiles.first()).zipWithNext()

    val filteredBoxes = boxes.filter { box ->
        redTiles.none { rt ->
            rt.row < max(box.first.first.row, box.first.second.row) && rt.row > min(box.first.first.row, box.first.second.row) &&
                rt.col < max(box.first.first.col, box.first.second.col) && rt.col > min(box.first.first.col, box.first.second.col)
        }
    }
    val outer = (redTiles + redTiles.first()).zipWithNext { a, b ->
        anyRange(a.row, b.row).flatMap { row -> anyRange(a.col, b.col).map { col -> Coord(row, col) } }
    }.flatten().toSet()

    val part2 = filteredBoxes.sortedByDescending { it.second }.first { box ->
        val boxCorners = box.first.let { listOf(it.first, Coord(it.first.row, it.second.col), it.second, Coord(it.second.row, it.first.col)) }
        outerTiles(boxCorners, outer, redLines)
    }
    println("part2=" + part2.second)
}

private fun outerTiles(l: List<Coord>, outer: Set<Coord>, redLines: List<Pair<Coord, Coord>>): Boolean {
    if (l.any { !insideShape(outer, redLines, it) }) return false
    (l + l.first()).zipWithNext { a, b ->
        anyRange(a.row, b.row).forEach { row -> anyRange(a.col, b.col).forEach { col -> if (!insideShape(outer, redLines, Coord(row, col))) return false } }
    }
    return true
}

private fun insideShape(outer: Set<Coord>, redLines: List<Pair<Coord, Coord>>, c: Coord): Boolean {
    if (c in outer) return true
    val numLeft = redLines.count {
        if (it.first.row == it.second.row) c.row == it.first.row && max(it.first.col, it.second.col) > c.col
        else c.row in anyRange(it.first.row, it.second.row) && it.first.col > c.col
    }
    return numLeft % 2 == 1
}
