package adventofcode.y2022 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day22.txt")

    val grid = lines.filter { it.isNotEmpty() && it.contains('.')}.map { s -> s.map { c -> Space.fromChar(c) } }
    val maxWidth = grid.maxOf { it.size }
    val paddedGrid = grid.map {
        if (it.size == maxWidth) it
        else it + List(maxWidth - it.size) { Space.EMPTY }
    }

    var facing = Facing.UP
    var pos = 0 to paddedGrid.first().indexOf(Space.PATH)
    matches("R" + lines.last(), "[R|L][0-9]+").forEach {
        facing = when(it.first()) {
            'R' -> facing.turnRight()
            else -> facing.turnLeft()
        }
        val spacesToMove = it.substring(1, it.length).toInt()
        println("$facing $spacesToMove")
        repeat(spacesToMove) {
            val newPos = move(pos, facing, paddedGrid)
            if (paddedGrid[newPos.first][newPos.second] == Space.PATH) pos = newPos
        }
        println(pos.copy(first = pos.first + 1, second = pos.second + 1))
    }
    val part1 = 1000 * (pos.first + 1) + 4 * (pos.second + 1) + facing.value
    println("part1=$part1")
}

private fun move(pos: Pair<Int, Int>, facing: Facing, grid: List<List<Space>>): Pair<Int, Int> {
    val newPos = facing.move(pos)
    return when {
        newPos.first < 0 -> move(newPos.copy(first = grid.size), facing, grid)
        newPos.first >= grid.size -> move(newPos.copy(first = -1), facing, grid)
        newPos.second < 0 -> move(newPos.copy(second = grid.first().size), facing, grid)
        newPos.second >= grid.first().size -> move(newPos.copy(second = -1), facing, grid)
        grid[newPos.first][newPos.second] == Space.EMPTY -> move(newPos, facing, grid)
        else -> newPos
    }
}

private enum class Facing(val value: Int) {
    UP(3) {
        override fun turnLeft() = LEFT
        override fun turnRight() = RIGHT
        override fun move(pos: Pair<Int, Int>) = pos.copy(first = pos.first - 1)
    }, RIGHT(0) {
        override fun turnLeft() = UP
        override fun turnRight() = DOWN
        override fun move(pos: Pair<Int, Int>) = pos.copy(second = pos.second + 1)
    }, DOWN(1) {
        override fun turnLeft() = RIGHT
        override fun turnRight() = LEFT
        override fun move(pos: Pair<Int, Int>) = pos.copy(first = pos.first + 1)
    }, LEFT(2) {
        override fun turnLeft() = DOWN
        override fun turnRight() = UP
        override fun move(pos: Pair<Int, Int>) = pos.copy(second = pos.second - 1)
    };

    abstract fun turnLeft(): Facing
    abstract fun turnRight(): Facing
    abstract fun move(pos: Pair<Int, Int>): Pair<Int, Int>
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