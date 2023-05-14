package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.DirectedCoord
import adventofcode.Facing
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
    var directedCoord = DirectedCoord(facing = Facing.UP, coord = Coord(0, 0))
    val panels = mutableMapOf(directedCoord.coord to initialPanelColour)
    val nums = line.toMutableList()
    var programContext = ProgramContext()
    while (true) {
        val curColour = panels.getOrDefault(directedCoord.coord, 0)
        programContext = runIntCodeProgram(nums, curColour.toBigInteger(), programContext)
        if (programContext.isHalted) break
        panels[directedCoord.coord] = programContext.output?.toInt() ?: throw IllegalArgumentException("Output was null")
        programContext = runIntCodeProgram(nums, emptyList(), programContext)
        directedCoord = when (programContext.output?.toInt()) {
            0 -> directedCoord.left()
            1 -> directedCoord.right()
            else -> throw IllegalArgumentException("Output was null")
        }.forward()
    }
    return panels
}
