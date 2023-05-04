package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbersToBigInt
import adventofcode.readFile
import java.math.BigInteger

fun main() {
    val line = matchNumbersToBigInt(readFile("src/main/resources/y2019/day15.txt")[0])
    var curContexts = listOf(TotalContext(Coord(0, 0), line.toMutableList(), ProgramContext()))
    val screen = mutableMapOf<Coord, String>()
    var steps = 0
    var oxygenContext: TotalContext? = null
    while (oxygenContext == null) {
        curContexts = curContexts.flatMap { totalContext ->
            screen[totalContext.curCoord] = "."
            unknownNearby(totalContext.curCoord, screen).mapNotNull {
                val newInstructions = totalContext.instructions.toMutableList()
                val newProgramContext = runIntCodeProgram(newInstructions, it.toBigInteger(), totalContext.programContext)
                val newTotalContext = TotalContext(fromCoord(totalContext.curCoord, it), newInstructions, newProgramContext)
                when {
                    newProgramContext.isHalted -> null
                    newProgramContext.output?.toInt() == 0 -> { screen[fromCoord(totalContext.curCoord, it)] = "#"; null }
                    newProgramContext.output?.toInt() == 2 -> { oxygenContext = totalContext; totalContext }
                    else -> newTotalContext
                }
            }
        }
        steps++
    }
    println("part1=$steps")

    var oxygens = listOfNotNull(oxygenContext)
    var fillSeconds = 0
    while (oxygens.isNotEmpty()) {
        oxygens = oxygens.flatMap { moveOxygen(it, screen) }
        fillSeconds++
    }
    println("part2=$fillSeconds")
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
private fun noOxygenNearby(c: Coord, screen: Map<Coord, String>): List<Int> = listOf(3, 1, 2, 4).filter { screen.getOrDefault(fromCoord(c, it), ".") == "." }

private fun fromCoord(c: Coord, dir: Int) = when (dir) {
    1 -> c.copy(row = c.row - 1)
    2 -> c.copy(row = c.row + 1)
    3 -> c.copy(col = c.col - 1)
    else -> c.copy(col = c.col + 1)
}
