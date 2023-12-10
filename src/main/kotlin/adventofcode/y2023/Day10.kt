package adventofcode.y2023 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.Facing
import adventofcode.mapCoord
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2023/day10.txt")
    val grid = lines.mapCoord().toMutableMap()

    val start = grid.filter { it.value == 'S' }.keys.first()

    val down = grid.getOrDefault(start.down(), '.')
    val up = grid.getOrDefault(start.up(), '.')
    val right = grid.getOrDefault(start.right(), '.')
    val left = grid.getOrDefault(start.left(), '.')

    val upConnection = up in listOf('|', 'F', '7')
    val downConnection = down in listOf('|', 'J', 'L')
    val leftConnection = left in listOf('-', 'F', 'L')
    val rightConnection = right in listOf('-', '7', 'J')
    grid[start] = when {
        downConnection && upConnection -> '|'
        leftConnection && rightConnection -> '-'
        downConnection && rightConnection -> 'F'
        downConnection && leftConnection -> '7'
        upConnection && rightConnection -> 'L'
        upConnection && leftConnection -> 'J'
        else -> throw IllegalArgumentException("Cannot replace S")
    }

    val next = when {
        down in listOf('|', 'L', 'J') -> start.down()
        up in listOf('|', 'F', '7') -> start.up()
        right in listOf('-', 'J', '7') -> start.right()
        else -> start.left()
    }
    val loop = mutableSetOf(start, next)

    var cur: Coord = next
    while (cur != start) {
        cur = moveCoord(grid.getValue(cur), cur).first { it !in loop || it == start }.also { loop += it }
    }
    println("part1=" + (loop.size + 1) / 2)

    part2(grid, lines, loop)
}

private fun moveCoord(curSymbol: Char, cur: Coord) = when (curSymbol) {
    '|' -> listOf(cur.down(), cur.up())
    '-' -> listOf(cur.left(), cur.right())
    'L' -> listOf(cur.up(), cur.right())
    'J' -> listOf(cur.up(), cur.left())
    '7' -> listOf(cur.left(), cur.down())
    'F' -> listOf(cur.right(), cur.down())
    else -> throw IllegalArgumentException("CurSymbol $curSymbol")
}

private fun part2(grid: MutableMap<Coord, Char>, lines: List<String>, loop: MutableSet<Coord>) {
    var outside =
        grid.keys.filter { it.row == 0 || it.row == lines.size - 1 || it.col == 0 || it.col == lines.first().length - 1 }
            .filter { it !in loop }
    while (outside.isNotEmpty()) {
        outside.forEach { grid[it] = 'X' }
        outside = outside.flatMap { it.around() }.toSet()
            .filter { it in grid.keys && it !in loop && grid.getValue(it) != 'X' }
    }

    grid.filter { it.value != 'X' && it.key !in loop }.forEach { grid[it.key] = '.' }

    val insideFunc = { facing: Facing, c: Coord -> facing.move(c).let { if (grid.getOrDefault(it, 'X') == '.') grid[it] = 'I' } }
    val start = grid.filter { it.value == '|' && grid.getOrDefault(it.key.left(), '.') == 'X' }.keys.first()
    val seen = mutableListOf(start)
    var cur: Coord? = start
    var insideFacing = Facing.RIGHT
    insideFunc(insideFacing, start)

    while (cur != null) {
        cur = moveCoord(grid.getValue(cur), cur).firstOrNull { it !in seen }?.also { c ->
            seen += c

            insideFunc(insideFacing, c)
            insideFacing = when (grid.getValue(c)) {
                'F' -> if (cur?.up() == c) insideFacing.right() else insideFacing.left()
                '7' -> if (cur?.up() == c) insideFacing.left() else insideFacing.right()
                'L' -> if (cur?.down() == c) insideFacing.left() else insideFacing.right()
                'J' -> if (cur?.down() == c) insideFacing.right() else insideFacing.left()
                else -> insideFacing
            }
            // Need to do this twice as there can be two coords on some turns
            insideFunc(insideFacing, c)
        }
    }

    var touchingIs = grid.filter { it.value == '.' && it.key.around().any { a -> grid.getOrDefault(a, '.') == 'I' } }
    while (touchingIs.isNotEmpty()) {
        touchingIs.forEach { grid[it.key] = 'I' }
        touchingIs = grid.filter { it.value == '.' && it.key.around().any { a -> grid.getOrDefault(a, '.') == 'I' } }
    }

    println("part2=" + grid.count { it.value == 'I' })
}
