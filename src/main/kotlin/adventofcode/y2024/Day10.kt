package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.mapCoord
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val grid = readFile(MethodHandles.lookup()).mapCoord().filter { it.value != '.' }
        .map { it.key to it.value.digitToInt() }.toMap()
    val starts = grid.filter { it.value ==  0 }.map { it.key }
    println("part1="+starts.sumOf { navigate1(grid, setOf(it)) })
    println("part2="+starts.sumOf { navigate2(grid, it)})
}

private fun navigate1(grid: Map<Coord, Int>, curs: Set<Coord>, num: Int = 0): Int {
    if (num == 9) return curs.size
    return navigate1(grid, curs.flatMap { c ->
       c.around().filter { grid.getOrDefault(it, -1) == num + 1 }
    }.toSet(), num + 1)
}

private fun navigate2(grid: Map<Coord, Int>, cur: Coord, num: Int = 0): Int {
    if (num == 9) return 1
    return cur.around().filter { grid.getOrDefault(it, -1) == num + 1 }
        .sumOf { navigate2(grid, it, num + 1) }
}
