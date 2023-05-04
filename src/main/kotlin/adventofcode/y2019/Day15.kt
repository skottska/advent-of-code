package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbersToBigInt
import adventofcode.printCoords
import adventofcode.readFile
import java.math.BigInteger

fun main() {
    val line = matchNumbersToBigInt(readFile("src/main/resources/y2019/day15.txt")[0])
    otherPart1(line)
    val part1 = line.toMutableList()
    var programContext = ProgramContext()
    var curCoord = Coord(0, 0)
    val screen = mutableMapOf(curCoord to ".")
    val path = mutableListOf<Int>()
    var dir = 4
    var reversing = false
    while (true) {
        programContext = runIntCodeProgram(part1, dir.toBigInteger(), programContext)
        if (programContext.isHalted) break
        when (programContext.output?.toInt()) {
            0 -> {
                screen[fromCoord(curCoord, dir)] = "#"
                val nearby = unknownNearby(curCoord, screen).firstOrNull()
                reversing = nearby == null
                dir = nearby ?: opposite(path.removeLast())
            }
            1 -> {
                curCoord = fromCoord(curCoord, dir)
                screen[curCoord] = "."
                if (!reversing) path += dir
                val nearby = unknownNearby(curCoord, screen).firstOrNull()
                reversing = nearby == null
                dir = nearby ?: opposite(path.removeLast())
            }
            else -> {
                path += dir
                break
            }
        }
        // printCoords(screen.keys) { c: Coord -> if (c == curCoord) "D" else screen.getOrDefault(c, " ") }
        // println()
    }
    println("part1=" + path.size)

    var oxygens = listOf(TotalContext(curCoord, part1, programContext))
    var fillSeconds = -1
    while (oxygens.isNotEmpty()) {
        oxygens = oxygens.flatMap { moveOxygen(it, screen) }
        fillSeconds++
        printCoords(screen.keys) { c: Coord -> screen.getOrDefault(c, " ") }
        println()
    }
    println("part2=$fillSeconds")
}

private fun otherPart1(line: List<BigInteger>) {
    var curContexts = listOf(TotalContext(Coord(0, 0), line.toMutableList(), ProgramContext()))
    val screen = mutableMapOf<Coord, String>()
    var steps = 0
    var foundOxygen = false
    while (!foundOxygen) {
        curContexts = curContexts.flatMap { totalContext ->
            screen[totalContext.curCoord] = "O"
            unknownNearby(totalContext.curCoord, screen).mapNotNull {
                val newInstructions = totalContext.instructions.toMutableList()
                val newProgramContext = runIntCodeProgram(newInstructions, it.toBigInteger(), totalContext.programContext)
                when {
                    newProgramContext.isHalted -> null
                    newProgramContext.output?.toInt() == 0 -> { screen[fromCoord(totalContext.curCoord, it)] = "#"; null }
                    newProgramContext.output?.toInt() == 2 -> { foundOxygen = true; TotalContext(fromCoord(totalContext.curCoord, it), newInstructions, newProgramContext) }
                    else -> TotalContext(fromCoord(totalContext.curCoord, it), newInstructions, newProgramContext)
                }
            }
        }
        steps++
    }
    println("newpart1=$steps")
}

private fun moveOxygen(totalContext: TotalContext, screen: MutableMap<Coord, String>): List<TotalContext> {
    screen[totalContext.curCoord] = "O"
    return noOxygenNearby(totalContext.curCoord, screen).mapNotNull {
        val newInstructions = totalContext.instructions.toMutableList()
        val newProgramContext = runIntCodeProgram(newInstructions, it.toBigInteger(), totalContext.programContext)
        when {
            newProgramContext.isHalted -> null
            newProgramContext.output?.toInt() == 0 -> { screen[fromCoord(totalContext.curCoord, it)] = "#"; null }
            else -> TotalContext(fromCoord(totalContext.curCoord, it), newInstructions, newProgramContext)
        }
    }
}

private data class TotalContext(val curCoord: Coord, val instructions: MutableList<BigInteger>, val programContext: ProgramContext)

private fun unknownNearby(c: Coord, screen: Map<Coord, String>): List<Int> = listOf(3, 1, 2, 4).filter { !screen.contains(fromCoord(c, it)) }
private fun noOxygenNearby(c: Coord, screen: Map<Coord, String>): List<Int> = listOf(3, 1, 2, 4).filter { screen.getOrDefault(fromCoord(c, it), ".") == "." }.also { println("OXY=" + it) }

private fun opposite(dir: Int) = when (dir) {
    1 -> 2
    2 -> 1
    3 -> 4
    else -> 3
}

private fun fromCoord(c: Coord, dir: Int) = when (dir) {
    1 -> c.copy(row = c.row - 1)
    2 -> c.copy(row = c.row + 1)
    3 -> c.copy(col = c.col - 1)
    else -> c.copy(col = c.col + 1)
}
