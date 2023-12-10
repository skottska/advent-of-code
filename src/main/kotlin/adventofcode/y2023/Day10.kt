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
    val next = when {
        down in listOf('|', 'L', 'J') -> start.down()
        up in listOf('|', 'F', '7') -> start.up()
        right in listOf('-', 'J', '7') -> start.right()
        else -> start.left()
    }

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

    val seen = mutableListOf(next, start)

    var cur: Coord? = next
    while (cur != null) {
        val curSymbol = grid.getValue(cur)
        cur = when (curSymbol) {
            '|' -> listOf(cur.down(), cur.up())
            '-' -> listOf(cur.left(), cur.right())
            'L' -> listOf(cur.up(), cur.right())
            'J' -> listOf(cur.up(), cur.left())
            '7' -> listOf(cur.left(), cur.down())
            'F' -> listOf(cur.right(), cur.down())
            else -> throw IllegalArgumentException("CurSymbol " + curSymbol)
        }.firstOrNull() { it !in seen }?.also { seen += it }
    }
    println("part1=" + (seen.size + 1) / 2)

    val loop = seen + start
    var outside = grid.keys.filter { it.row == 0 || it.row == lines.size - 1 || it.col == 0 || it.col == lines.first().length - 1 }
        .filter { it !in loop }
    while (outside.isNotEmpty()) {
        outside.forEach { grid[it] = 'X' }
        outside = outside.flatMap { it.around() }.toSet().filter { it in grid.keys && it !in loop && grid.getValue(it) != 'X' }
    }

    grid.filter { it.value != 'X' && it.key !in loop }.forEach { grid[it.key] = '.' }

    val start2 = grid.filter { it.value == '|' && grid.getOrDefault(it.key.left(), '.') == 'X' }.keys.first()
    val seen2 = mutableListOf(start2)
    var cur2: Coord? = start2
    val inside = mutableSetOf<Coord>()
    var insideFacing = Facing.RIGHT
    insideFacing.move(start2).let { if (grid.getOrDefault(it, 'X') == '.') inside += it }

    while (cur2 != null) {
        val curSymbol = grid.getValue(cur2)
        val next2 = when (curSymbol) {
            '|' -> listOf(cur2.up(), cur2.down())
            '-' -> listOf(cur2.left(), cur2.right())
            'L' -> listOf(cur2.up(), cur2.right())
            'J' -> listOf(cur2.up(), cur2.left())
            '7' -> listOf(cur2.left(), cur2.down())
            'F' -> listOf(cur2.right(), cur2.down())
            else -> throw IllegalArgumentException("CurSymbol $curSymbol")
        }.firstOrNull { it !in seen2 }?.also { seen2 += it }

        next2?.let {
            val move = insideFacing.move(it)
            if (grid.getOrDefault(move, 'X') == '.') inside += move

            val nextSymbol = grid.getValue(it)
            insideFacing = when {
                nextSymbol == 'F' -> if (cur2?.up() == next2) insideFacing.right() else insideFacing.left()
                nextSymbol == '7' -> if (cur2?.up() == next2) insideFacing.left() else insideFacing.right()
                nextSymbol == 'L' -> if (cur2?.down() == next2) insideFacing.left() else insideFacing.right()
                nextSymbol == 'J' -> if (cur2?.down() == next2) insideFacing.right() else insideFacing.left()
                else -> insideFacing
            }
        }

        cur2 = next2

        cur2?.let {
            val move = insideFacing.move(it)
            if (grid.getOrDefault(move, 'X') == '.') inside += move
        }
    }

    loop.forEach { grid[it] = '*' }
    inside.forEach { grid[it] = 'I' }

    var touchingIs = grid.filter { it.value == '.' && it.key.around().any { a -> grid.getOrDefault(a, '.') == 'I' } }
    while (touchingIs.isNotEmpty()) {
        touchingIs.forEach { grid[it.key] = 'I' }
        touchingIs = grid.filter { it.value == '.' && it.key.around().any { a -> grid.getOrDefault(a, '.') == 'I' } }
    }

    println("part2="+grid.count { it.value == 'I' })
}
