package adventofcode.y2020 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.mapCoord
import adventofcode.readFile

fun main() {
    val grid = readFile("src/main/resources/y2020/day11.txt").mapCoord()
    println("part1=" + iterate(::switchSeatsPart1, grid))
    println("part2=" + iterate(::switchSeatsPart2, grid))
}

private fun iterate(func: (grid: Map<Coord, Char>) -> Map<Coord, Char>, grid: Map<Coord, Char>): Int {
    var cur = grid
    var next = func(cur)
    while (cur != next) {
        cur = next; next = func(next)
    }
    return cur.values.count { it == '#' }
}

private fun switchSeatsPart1(grid: Map<Coord, Char>) = grid.map { c ->
    val around = c.key.aroundDiag().map { grid.getOrDefault(it, '.') }
    c.key to when {
        c.value == 'L' && around.none { it == '#' } -> '#'
        c.value == '#' && around.count { it == '#' } >= 4 -> 'L'
        else -> c.value
    }
}.toMap()

private fun switchSeatsPart2(grid: Map<Coord, Char>) = grid.map { c ->
    val moveFuncs = listOf(Coord::left, Coord::right, Coord::up, Coord::down, Coord::downLeft, Coord::downRight, Coord::upRight, Coord::upLeft)
    val around = moveFuncs.map { func ->
        var a = func(c.key)
        var aValue = grid[a]
        while (aValue != null && aValue == '.') {
            a = func(a)
            aValue = grid[a]
        }
        aValue ?: '.'
    }
    c.key to when {
        c.value == 'L' && around.none { it == '#' } -> '#'
        c.value == '#' && around.count { it == '#' } >= 5 -> 'L'
        else -> c.value
    }
}.toMap()
