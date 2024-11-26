package adventofcode.y2020 // ktlint-disable filename

import adventofcode.Coord3D
import adventofcode.Coord4D
import adventofcode.DiagAdjacentCoord
import adventofcode.mapCoord
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val active = readFile(MethodHandles.lookup()).mapCoord().filter { it.value == '#' }.keys
    println("part1=" + solve(active.map { Coord3D(it.row, it.col, 0) }))
    println("part2=" + solve(active.map { Coord4D(0, it.row, it.col, 0) }))
}

private fun solve(active: List<DiagAdjacentCoord>): Int {
    return (1..6).fold(active) { total, _ ->
        total.flatMap { it.diagAdjacentAndThis() }.toSet().filter { cur ->
            val adjacentAndActive = (cur.diagAdjacentAndThis() - cur).count { it in total }
            when (cur) {
                in total -> adjacentAndActive in listOf(2, 3)
                else -> adjacentAndActive == 3
            }
        }
    }.size
}
