package adventofcode.y2017 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.Facing
import adventofcode.mapCoord
import adventofcode.readFile

fun main() {
    val grid = readFile("src/main/resources/y2017/day22.txt").mapCoord()
    println("part1=" + infect(grid.toMutableMap(), 10_000, false))
    println("part2=" + infect(grid.toMutableMap(), 10_000_000, true))
}

private fun infect(grid: MutableMap<Coord, Char>, iterations: Int, isPart2: Boolean): Int {
    var cur = Coord(grid.keys.maxOf { it.row } / 2, grid.keys.maxOf { it.col } / 2)
    var facing = Facing.UP
    var infections = 0

    (1..iterations).forEach { _ ->
        when (grid.getOrDefault(cur, '.')) {
            '#' -> {
                facing = facing.right()
                grid[cur] = if (isPart2) 'F' else '.'
            }
            '.' -> {
                facing = facing.left()
                grid[cur] = if (isPart2) 'W' else '#'.also { infections++ }
            }
            'W' -> {
                grid[cur] = '#'
                infections++
            }
            'F' -> {
                facing = facing.left().left()
                grid[cur] = '.'
            }
        }
        cur = facing.move(cur)
    }
    return infections
}
