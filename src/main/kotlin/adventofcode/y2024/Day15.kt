package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.Equation
import adventofcode.concat
import adventofcode.mapCoord
import adventofcode.matchNumbers
import adventofcode.matchNumbersLong
import adventofcode.printCoords
import adventofcode.product
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val gap = lines.indexOf("")
    val grid = lines.subList(0, gap).mapCoord()
    val commands = lines.subList(gap + 1, lines.size).concat().map {
        when (it) {
            '<' -> Coord::left
            '^' -> Coord::up
            '>' -> Coord::right
            'v' -> Coord::down
            else -> throw IllegalArgumentException("What is=$it")
        }
    }

    val final = commands.fold(grid) { curGrid, command ->
        val robot = curGrid.filter { it.value == '@' }.map { it.key }.first()
        var nextTo = command.invoke(robot)
        when (curGrid.getValue(nextTo)) {
            '#' -> curGrid
            '.' -> curGrid + (nextTo to '@') + (robot to '.')
            else -> {
                while (curGrid.getValue(nextTo) == 'O') nextTo = command.invoke(nextTo)
                if (curGrid.getValue(nextTo) == '.') {
                    curGrid + (command.invoke(robot) to '@') + (robot to '.') + (nextTo to 'O')
                }
                else curGrid
            }
        }
    }

    val part1 = final.filter { it.value == 'O' }.map { 100 * it.key.row + it.key.col }.sum()
    println("part1=$part1")
    part2()
    part3(true)
    part3(false)
}

private fun part2() {
    val lines = readFile(MethodHandles.lookup())
    val gap = lines.indexOf("")
    val grid1 = lines.subList(0, gap).map { line -> line.map {
        when (it) {
            '@' -> "@."
            'O' -> "[]"
            else ->it.toString() + it
        }
    }.concat() }.mapCoord()

    val commands = lines.subList(gap + 1, lines.size).concat().map {
        when (it) {
            '<' -> Coord::left
            '^' -> Coord::up
            '>' -> Coord::right
            'v' -> Coord::down
            else -> throw IllegalArgumentException("What is=$it")
        }
    }

    var num = 0
    val final = commands.fold(grid1) { curGrid, command ->
        num++
        val robot = curGrid.filter { it.value == '@' }.map { it.key }.first()
        var nextTo = command.invoke(robot)
        when {
            curGrid.getValue(nextTo) == '#' -> curGrid
            curGrid.getValue(nextTo) == '.' -> curGrid + (nextTo to '@') + (robot to '.')
            command in listOf(Coord::left, Coord::right) -> {
                val boxes = mutableListOf(nextTo)
                while (curGrid.getValue(nextTo) in listOf('[', ']')) {
                    nextTo = command.invoke(nextTo)
                    boxes += nextTo
                }
                if (curGrid.getValue(nextTo) == '.') {
                    val default = if (command == Coord::left) '[' else ']'
                    curGrid + boxes.map { it to opp(curGrid.getValue(it), default) } + (command.invoke(robot) to '@') + (robot to '.')
                }
                else curGrid
            }
            else -> {
                var curNext = box(nextTo, curGrid).toSet()
                val boxes = mutableListOf<Coord>()

                val firstBox = curNext
                while (curNext.none { curGrid.getValue(it) == '#' } && curNext.any { curGrid.getValue(it) == '[' }) {
                    curNext = curNext.filter { curGrid.getValue(it) in listOf('[', ']') }.toSet()
                    boxes.addAll(curNext)
                    curNext = curNext.flatMap { box(command.invoke(it), curGrid) }.toSet()
                }
                if (curNext.all { curGrid.getValue(it) == '.' }) {
                    val movedBoxes = boxes.reversed().flatMap { listOf(command.invoke(it) to curGrid.getValue(it), it to '.') }
                    val movedFirstBox = firstBox.map { it to '.' }
                    curGrid + movedBoxes + movedFirstBox + (command.invoke(robot) to '@') + (robot to '.')
                }
                else curGrid
            }
        }
    }

    val part2 = final.filter { it.value == '[' }.map { 100 * it.key.row + it.key.col }.sum()
    println("part2=$part2")
}

private fun part3(isPart1: Boolean) {
    val lines = readFile(MethodHandles.lookup())
    val boxChars = listOf('[', ']', 'O')
    val gap = lines.indexOf("")
    val grid1 = if (isPart1) lines.subList(0, gap).mapCoord()
    else lines.subList(0, gap).map { line -> line.map {
        when (it) {
            '@' -> "@."
            'O' -> "[]"
            else ->it.toString() + it
        }
    }.concat() }.mapCoord()

    val commands = lines.subList(gap + 1, lines.size).concat().map {
        when (it) {
            '<' -> Coord::left
            '^' -> Coord::up
            '>' -> Coord::right
            'v' -> Coord::down
            else -> throw IllegalArgumentException("What is=$it")
        }
    }

    var num = 0
    val final = commands.fold(grid1) { curGrid, command ->
        num++
        val robot = curGrid.filter { it.value == '@' }.map { it.key }.first()
        var nextTo = command.invoke(robot)
        when {
            curGrid.getValue(nextTo) == '#' -> curGrid
            curGrid.getValue(nextTo) == '.' -> curGrid + (nextTo to '@') + (robot to '.')
            command in listOf(Coord::left, Coord::right) -> {
                val boxes = mutableListOf(nextTo)
                while (curGrid.getValue(nextTo) in boxChars) {
                    nextTo = command.invoke(nextTo)
                    boxes += nextTo
                }
                if (curGrid.getValue(nextTo) == '.') {
                    val default = if (isPart1) 'O' else if (command == Coord::left) '[' else ']'
                    curGrid + boxes.map { it to opp(curGrid.getValue(it), default) } + (command.invoke(robot) to '@') + (robot to '.')
                }
                else curGrid
            }
            else -> {
                var curNext = box(nextTo, curGrid).toSet()
                val boxes = mutableListOf<Coord>()

                val firstBox = curNext
                while (curNext.none { curGrid.getValue(it) == '#' } && curNext.any { curGrid.getValue(it) in boxChars }) {
                    curNext = curNext.filter { curGrid.getValue(it) in boxChars }.toSet()
                    boxes.addAll(curNext)
                    curNext = curNext.flatMap { box(command.invoke(it), curGrid) }.toSet()
                }
                if (curNext.all { curGrid.getValue(it) == '.' }) {
                    val movedBoxes = boxes.reversed().flatMap { listOf(command.invoke(it) to curGrid.getValue(it), it to '.') }
                    val movedFirstBox = firstBox.map { it to '.' }
                    curGrid + movedBoxes + movedFirstBox + (command.invoke(robot) to '@') + (robot to '.')
                }
                else curGrid
            }
        }
    }

    //printCoords(final.keys) { c -> final.getValue(c).toString() }
    val part3 = final.filter { it.value == '[' || it.value == 'O' }.map { 100 * it.key.row + it.key.col }.sum()
    println("part3=$part3")
}

private fun box(c: Coord, grid: Map<Coord, Char>): List<Coord> = when(grid.getValue(c)) {
    '[' -> listOf(c, c.right())
    ']' -> listOf(c.left(), c)
    else -> listOf(c)
}

private fun opp(c: Char, default: Char) = when(c) {
    '.' -> default
    '[' -> ']'
    'O' -> 'O'
    else -> '['
}