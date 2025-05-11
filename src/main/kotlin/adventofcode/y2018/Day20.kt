package adventofcode.y2018 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val regex = readFile(MethodHandles.lookup()).first().drop(1).dropLast(1)
    iterate(regex, 0, setOf(Coord(0, 0)))

    val distance = navigate(rooms + doors).filter { it.key in rooms }.map { it.value / 2 }
    println("part1=" + distance.max())
    println("part2=" + distance.count { it >= 1000 })
}

private fun navigate(grid: Set<Coord>): Map<Coord, Int> {
    var moves = 0
    val result = mutableMapOf<Coord, Int>()
    var cur = setOf(Coord(0, 0))
    while (cur.isNotEmpty()) {
        result += cur.map { it to moves }
        moves++
        cur = cur.flatMap { it.around() }.filter { it !in result.keys && it in grid }.toSet()
    }
    return result
}

private val rooms = mutableSetOf<Coord>()
private val doors = mutableSetOf<Coord>()

private fun iterate(regex: String, start: Int, inCs: Set<Coord>): Pair<Int, Set<Coord>> {
    var cur = start
    var curCs = inCs
    while (cur < regex.length) {
        when (regex[cur++]) {
            'N' -> curCs = curCs.map { it.up() }.also { doors += it }.map { it.up() }.toSet()
            'S' -> curCs = curCs.map { it.down() }.also { doors += it }.map { it.down() }.toSet()
            'E' -> curCs = curCs.map { it.right() }.also { doors += it }.map { it.right() }.toSet()
            'W' -> curCs = curCs.map { it.left() }.also { doors += it }.map { it.left() }.toSet()
            '|' -> return cur to curCs
            ')' -> return cur to curCs
            '(' -> {
                var result = iterate(regex, cur, curCs)
                val newCurs = result.second.toMutableList()
                cur = result.first
                while (regex[cur - 1] != ')') {
                    result = iterate(regex, cur, curCs)
                    cur = result.first
                    newCurs += result.second
                }
                curCs = newCurs.toSet()
            }
            else -> throw IllegalArgumentException("what is this=" + regex[cur - 1])
        }
        rooms.addAll(curCs)
    }
    return cur to emptySet()
}
