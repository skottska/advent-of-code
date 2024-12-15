package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.concat
import adventofcode.mapCoord
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    moveRobot(true)
    moveRobot(false)
}

private fun moveRobot(isPart1: Boolean) {
    val lines = readFile(MethodHandles.lookup())
    val boxChars = listOf('[', ']', 'O')
    val gap = lines.indexOf("")
    val grid = (if (isPart1) lines.subList(0, gap).mapCoord()
    else lines.subList(0, gap).map { line ->
        line.map {
            when (it) {
                '@' -> "@."
                'O' -> "[]"
                else -> it.toString() + it
            }
        }.concat()
    }.mapCoord()).toMutableMap()

    val commands = lines.subList(gap + 1, lines.size).concat().map {
        when (it) {
            '<' -> Coord::left
            '^' -> Coord::up
            '>' -> Coord::right
            'v' -> Coord::down
            else -> throw IllegalArgumentException("What is=$it")
        }
    }

    commands.forEach { command ->
        val robot = grid.filter { it.value == '@' }.map { it.key }.first()
        val movedRobot = listOf(command.invoke(robot) to '@', robot to '.')
        var nextTo = command.invoke(robot)
        when {
            grid.getValue(nextTo) == '.' -> grid += movedRobot
            command in listOf(Coord::left, Coord::right) -> {
                val boxes = mutableListOf(nextTo)
                while (grid.getValue(nextTo) in boxChars) {
                    nextTo = command.invoke(nextTo)
                    boxes += nextTo
                }
                if (grid.getValue(nextTo) == '.') {
                    val default = if (isPart1) 'O' else if (command == Coord::left) '[' else ']'
                    grid += boxes.map { it to opp(grid.getValue(it), default) } + movedRobot
                }
            }

            else -> {
                var curNext = box(nextTo, grid).toSet()
                val boxes = mutableListOf<Coord>()

                while (curNext.none { grid.getValue(it) == '#' } && curNext.any { grid.getValue(it) in boxChars }) {
                    curNext = curNext.filter { grid.getValue(it) in boxChars }.toSet()
                    boxes.addAll(curNext)
                    curNext = curNext.flatMap { box(command.invoke(it), grid) }.toSet()
                }
                if (curNext.all { grid.getValue(it) == '.' }) {
                    val movedBoxes =
                        boxes.reversed().flatMap { listOf(command.invoke(it) to grid.getValue(it), it to '.') }
                    grid += movedBoxes + movedRobot
                }
            }
        }
    }

    val result = grid.filter { it.value == '[' || it.value == 'O' }.map { 100 * it.key.row + it.key.col }.sum()
    println("part" + (if (isPart1) "1" else "2") + "=$result")
}

private fun box(c: Coord, grid: Map<Coord, Char>): List<Coord> = when (grid.getValue(c)) {
    '[' -> listOf(c, c.right())
    ']' -> listOf(c.left(), c)
    else -> listOf(c)
}

private fun opp(c: Char, default: Char) = when (c) {
    '.' -> default
    '[' -> ']'
    'O' -> 'O'
    else -> '['
}