package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbersToBigInt
import adventofcode.printCoords
import adventofcode.readFile

fun main() {
    val line = matchNumbersToBigInt(readFile("src/main/resources/y2019/day17.txt")[0])

    val linePart1 = line.toMutableList()
    var programContext = ProgramContext()
    var row = 0
    var col = 0
    val area = mutableMapOf<Coord, String>()
    while (true) {
        programContext = runIntCodeProgram(linePart1, emptyList(), programContext)
        if (programContext.isHalted) break
        programContext.output?.toInt()?.let {
            when (it) {
                46 -> area[Coord(row, col++)] = "."
                35 -> area[Coord(row, col++)] = "#"
                94 -> area[Coord(row, col++)] = "^"
                10 -> { row++; col = 0 }
                else -> throw IllegalArgumentException("" + it)
            }
        }
    }
    printCoords(area.keys) { c: Coord -> area.getValue(c) }
    val part1 = area.keys.filter {
        listOf(
            Coord(it.row, it.col - 1),
            Coord(it.row, it.col + 1),
            Coord(it.row + 1, it.col),
            Coord(it.row - 1, it.col),
        ).all { c -> area.getOrDefault(c, ".") != "." }
    }.sumOf { it.row * it.col }
    println("part1=$part1")
}
