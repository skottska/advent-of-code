package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbersToBigInt
import adventofcode.printCoords
import adventofcode.readFile
import java.math.BigInteger

fun main() {
    val line = matchNumbersToBigInt(readFile("src/main/resources/y2019/day11.txt")[0])
    println("part1=" + paintPanels(line, initialPanelColour = 0).size)
    println("part2=")
    val panels = paintPanels(line, initialPanelColour = 1)
    printCoords(panels.keys) { if (panels.getOrDefault(it, 0) == 1) "#" else " " }
}

private fun paintPanels(line: List<BigInteger>, initialPanelColour: Int): Map<Coord, Int> {
    var curCoord = Coord(0, 0)
    var facing = Facing.UP
    val panels = mutableMapOf(curCoord to initialPanelColour)
    val nums = line.toMutableList()
    var programContext = ProgramContext()
    while (true) {
        val curColour = panels.getOrDefault(curCoord, 0)
        programContext = runIntCodeProgram(nums, curColour.toBigInteger(), programContext)
        if (programContext.isHalted) break
        panels[curCoord] = programContext.output?.toInt() ?: throw IllegalArgumentException("Output was null")
        programContext = runIntCodeProgram(nums, emptyList(), programContext)
        facing = when (programContext.output?.toInt()) {
            0 -> facing.left()
            1 -> facing.right()
            else -> throw IllegalArgumentException("Output was null")
        }
        curCoord = facing.move(curCoord)
    }
    return panels
}

private enum class Facing(val move: Coord) {
    UP(Coord(-1, 0)), RIGHT(Coord(0, 1)), DOWN(Coord(1, 0)), LEFT(Coord(0, -1));

    fun left() = when (this) {
        UP -> LEFT
        RIGHT -> UP
        DOWN -> RIGHT
        LEFT -> DOWN
    }

    fun right() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }

    fun move(c: Coord) = Coord(c.row + move.row, c.col + move.col)
}
