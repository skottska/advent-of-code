package adventofcode.y2023 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.DirectedCoord
import adventofcode.Facing
import adventofcode.mapCoord
import adventofcode.readFile
import java.lang.invoke.MethodHandles
import kotlin.math.min

fun main() {
    val grid = readFile(MethodHandles.lookup()).mapCoord().map { it.key to it.value.digitToInt() }.toMap()
    val start = Coord(0, 0)
    val end = Coord(grid.maxOf { it.key.row }, grid.maxOf { it.key.col })

    val theoreticalBest = generateTheoreticalBest(grid, end)

    listOf(Facing.RIGHT, Facing.DOWN).forEach { iterate(GridHeatPath(DirectedCoord(it, start), 0), end, grid, 1..3, theoreticalBest) }
    println("part1=$bestResult")

    bestResult = Int.MAX_VALUE; best.clear()
    listOf(Facing.RIGHT, Facing.DOWN).forEach { iterate(GridHeatPath(DirectedCoord(it, start), 0), end, grid, 4..10, theoreticalBest) }
    println("part2=$bestResult")
}

private var bestResult = Int.MAX_VALUE
private val best: MutableMap<DirectedCoord, Int> = mutableMapOf()

private data class GridHeatPath(val coord: DirectedCoord, val heatLoss: Int) {
    fun maxMove(end: Coord): Int = when (coord.facing) {
        Facing.UP -> coord.coord.row
        Facing.DOWN -> end.row - coord.coord.row
        Facing.LEFT -> coord.coord.col
        Facing.RIGHT -> end.col - coord.coord.col
    }
    fun move(grid: Map<Coord, Int>): GridHeatPath = coord.forward().let { GridHeatPath(it, heatLoss + grid.getValue(it.coord)) }
    fun left() = copy(coord = coord.left())
    fun right() = copy(coord = coord.right())
}

private fun iterate(cur: GridHeatPath, end: Coord, grid: Map<Coord, Int>, range: IntRange, theoreticalBest: Map<Coord, Int>) {
    best[cur.coord]?.let { if (it <= cur.heatLoss) return }
    best[cur.coord] = cur.heatLoss
    if (cur.coord.coord == end) { bestResult = min(bestResult, cur.heatLoss); return }
    if (bestResult <= cur.heatLoss + theoreticalBest.getValue(cur.coord.coord)) return

    val maxMove = cur.maxMove(end)
    if (range.first > maxMove || maxMove == 0) return
    val startMove = (0 until range.first).fold(cur) { final, _ -> final.move(grid) }
    val adjustedRange = range.first until min(range.last, maxMove)
    val validMoves = adjustedRange.fold(listOf(startMove)) { total, _ -> total + total.last().move(grid) }.filter {
        bestResult > it.heatLoss + theoreticalBest.getValue(it.coord.coord)
    }.flatMap {
        if (cur.coord.facing in listOf(Facing.UP, Facing.RIGHT)) listOf(it.right(), it.left()) else listOf(it.left(), it.right())
    }
    validMoves.forEach { iterate(it, end, grid, range, theoreticalBest) }
}

private fun generateTheoreticalBest(grid: Map<Coord, Int>, end: Coord): Map<Coord, Int> {
    val theoreticalBest = mutableMapOf<Coord, Int>()

    var cur = listOf(end to 0)
    while (cur.isNotEmpty()) {
        cur = cur.mapNotNull { c ->
            if (c.second >= (theoreticalBest[c.first] ?: Int.MAX_VALUE)) null
            else {
                theoreticalBest[c.first] = c.second
                c.first.around().filter { it.row in 0..end.row && it.col in 0..end.col }.map { it to c.second + grid.getValue(c.first) }
            }
        }.flatten()
    }

    return theoreticalBest
}
