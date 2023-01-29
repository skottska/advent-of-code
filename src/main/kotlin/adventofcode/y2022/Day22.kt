package adventofcode.y2022 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

/* For test input
private val cubeFaces = mapOf(
    '1' to Pair(Pair(0, 8), Pair(3, 11)),
    '2' to Pair(Pair(4, 0), Pair(7, 3)),
    '3' to Pair(Pair(4, 4), Pair(7, 7)),
    '4' to Pair(Pair(4, 8), Pair(7, 11)),
    '5' to Pair(Pair(8, 8), Pair(11, 11)),
    '6' to Pair(Pair(8, 12), Pair(11, 15))
)*/

private val cubeFaces = mapOf(
    '1' to Pair(Pair(0, 50), Pair(49, 99)),
    '2' to Pair(Pair(0, 100), Pair(49, 149)),
    '3' to Pair(Pair(50, 50), Pair(99, 99)),
    '4' to Pair(Pair(100, 0), Pair(149, 49)),
    '5' to Pair(Pair(100, 50), Pair(149, 99)),
    '6' to Pair(Pair(150, 0), Pair(199, 49))
)

fun main() {
    val lines = readFile("src/main/resources/y2022/day22.txt")

    val grid = lines.filter { it.isNotEmpty() && it.contains('.') }.map { s -> s.map { c -> Space.fromChar(c) } }
    val maxWidth = grid.maxOf { it.size }
    val paddedGrid = grid.map {
        if (it.size == maxWidth) it
        else it + List(maxWidth - it.size) { Space.EMPTY }
    }
    println("part1=" + iterate(paddedGrid, lines, isPart1 = true))
    println("part2=" + iterate(paddedGrid, lines, isPart1 = false))
}

private fun iterate(grid: List<List<Space>>, lines: List<String>, isPart1: Boolean): Int {
    var pos = Position(0, grid.first().indexOf(Space.PATH), Facing.UP)
    matches("R" + lines.last(), "[R|L][0-9]+").forEach {
        pos = when (it.first()) {
            'R' -> pos.turnRight()
            else -> pos.turnLeft()
        }
        val spacesToMove = it.substring(1, it.length).toInt()
        // println("${pos.facing} $spacesToMove")
        repeat(spacesToMove) {
            val newPos = if (isPart1) movePart1(pos, grid) else movePart2(pos, grid)
            if (grid[newPos.row][newPos.col] == Space.PATH) pos = newPos
        }
        // println(pos.copy(row = pos.row + 1, col = pos.col + 1))
    }
    return 1000 * (pos.row + 1) + 4 * (pos.col + 1) + pos.facing.value
}

private data class Position(val row: Int, val col: Int, val facing: Facing) {
    fun turnLeft() = when (facing) {
        Facing.UP -> copy(facing = Facing.LEFT)
        Facing.RIGHT -> copy(facing = Facing.UP)
        Facing.DOWN -> copy(facing = Facing.RIGHT)
        Facing.LEFT -> copy(facing = Facing.DOWN)
    }

    fun turnRight() = when (facing) {
        Facing.UP -> copy(facing = Facing.RIGHT)
        Facing.RIGHT -> copy(facing = Facing.DOWN)
        Facing.DOWN -> copy(facing = Facing.LEFT)
        Facing.LEFT -> copy(facing = Facing.UP)
    }

    fun move() = when (facing) {
        Facing.UP -> copy(row = row - 1)
        Facing.RIGHT -> copy(col = col + 1)
        Facing.DOWN -> copy(row = row + 1)
        Facing.LEFT -> copy(col = col - 1)
    }

    fun cubeFace() = cubeFaces.filter {
        it.value.first.first <= row && it.value.second.first >= row &&
            it.value.first.second <= col && it.value.second.second >= col
    }.keys.first()

    /* For test input
    fun cubeFaceMove(): Position {
        val cubeFace = cubeFace()
        return when {
            cubeFace == '1' && facing == Facing.UP -> copy(row = 11)
            cubeFace == '1' && facing == Facing.RIGHT -> copy(row = 11 - row, col = 15, facing = Facing.LEFT)
            cubeFace == '1' && facing == Facing.LEFT -> copy(row = 4, col = row + 4, facing = Facing.DOWN)

            cubeFace == '3' && facing == Facing.UP -> copy(row = col - 4, col = 8, facing = Facing.RIGHT)
            cubeFace == '4' && facing == Facing.RIGHT -> copy(row = 8, col = 19 - row, facing = Facing.DOWN)
            cubeFace == '5' && facing == Facing.DOWN -> copy(row = 7, col = 11 - col, facing = Facing.UP)
            else -> throw IllegalArgumentException("No support from $this")
        }
    }*/

    fun cubeFaceMove(): Position {
        val cubeFace = cubeFace()
        return when {
            cubeFace == '1' && facing == Facing.UP -> copy(row = col + 100, col = 0, facing = Facing.RIGHT)
            cubeFace == '1' && facing == Facing.LEFT -> copy(row = 149 - row, col = 0, facing = Facing.RIGHT)

            cubeFace == '2' && facing == Facing.UP -> copy(row = 199, col = col - 100, facing = Facing.UP)
            cubeFace == '2' && facing == Facing.RIGHT -> copy(row = 149 - row, col = 99, facing = Facing.LEFT)
            cubeFace == '2' && facing == Facing.DOWN -> copy(row = col - 50, col = 99, facing = Facing.LEFT)

            cubeFace == '3' && facing == Facing.LEFT -> copy(row = 100, col = row - 50, facing = Facing.DOWN)
            cubeFace == '3' && facing == Facing.RIGHT -> copy(row = 49, col = row + 50, facing = Facing.UP)

            cubeFace == '4' && facing == Facing.UP -> copy(row = col + 50, col = 50, facing = Facing.RIGHT)
            cubeFace == '4' && facing == Facing.LEFT -> copy(row = 149 - row, col = 50, facing = Facing.RIGHT)

            cubeFace == '5' && facing == Facing.DOWN -> copy(row = col + 100, col = 49, facing = Facing.LEFT)
            cubeFace == '5' && facing == Facing.RIGHT -> copy(row = 149 - row, col = 149, facing = Facing.LEFT)

            cubeFace == '6' && facing == Facing.LEFT -> copy(row = 0, col = row - 100, facing = Facing.DOWN)
            cubeFace == '6' && facing == Facing.RIGHT -> copy(row = 149, col = row - 100, facing = Facing.UP)
            cubeFace == '6' && facing == Facing.DOWN -> copy(row = 0, col = col + 100, facing = Facing.DOWN)
            else -> throw IllegalArgumentException("No support from $this")
        }
    }
}

private fun movePart1(pos: Position, grid: List<List<Space>>): Position {
    val newPos = pos.move()
    return when {
        newPos.row < 0 -> movePart1(newPos.copy(row = grid.size), grid)
        newPos.row >= grid.size -> movePart1(newPos.copy(row = -1), grid)
        newPos.col < 0 -> movePart1(newPos.copy(col = grid.first().size), grid)
        newPos.col >= grid.first().size -> movePart1(newPos.copy(col = -1), grid)
        grid[newPos.row][newPos.col] == Space.EMPTY -> movePart1(newPos, grid)
        else -> newPos
    }
}

private fun movePart2(pos: Position, grid: List<List<Space>>): Position {
    val newPos = pos.move()
    return when {
        newPos.row < 0 || newPos.row >= grid.size -> pos.cubeFaceMove()
        newPos.col < 0 || newPos.col >= grid.first().size -> pos.cubeFaceMove()
        grid[newPos.row][newPos.col] == Space.EMPTY -> pos.cubeFaceMove()
        else -> newPos
    }
}

private enum class Facing(val value: Int) {
    UP(3), RIGHT(0), DOWN(1), LEFT(2);
}

private enum class Space {
    PATH, WALL, EMPTY;

    companion object {
        fun fromChar(c: Char) = when (c) {
            '.' -> PATH
            '#' -> WALL
            else -> EMPTY
        }
    }
}
