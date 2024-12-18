package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.concat
import adventofcode.mapCoord
import adventofcode.matchNumbers
import adventofcode.matchNumbersLong
import adventofcode.power
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val coords = readFile(MethodHandles.lookup()).map { l -> matchNumbers(l).let { Coord(it[1], it[0]) } }

    val start = Coord(0, 0)
    val finish = Coord(70, 70)
    println("part1="+navigate(coords.take(1024).associateWith { '"' }, start, finish, 0))

    var min = 1024
    var max = coords.size
    while (max - min > 1) {
        val next = min + (max - min) / 2
        cache.clear()
        val nextResult = navigate(coords.take(next).associateWith { '"' }, start, finish, 0)
        if (nextResult == null) max = next else min = next
    }
    println("part2="+coords[max-1].let { ""+it.col+","+it.row })
}

private val cache = mutableMapOf<Coord, Int>()
private fun navigate(grid: Map<Coord, Char>, cur: Coord, end: Coord, moves: Int): Int? {
    cache[cur]?.let { if (it <= moves) return null }
    cache[cur] = moves
    if (cur == end) return moves
    return cur.around().filter { grid[it] == null && listOf(it.col, it.row).all { x -> x in 0..end.col } }
        .mapNotNull { navigate(grid, it, end, moves + 1) }.minOrNull()
}
