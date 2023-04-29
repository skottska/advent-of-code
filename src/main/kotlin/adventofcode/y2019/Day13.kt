package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbersToBigInt
import adventofcode.printCoords
import adventofcode.readFile
import java.math.BigInteger

fun main() {
    val line = matchNumbersToBigInt(readFile("src/main/resources/y2019/day13.txt")[0])
    val part1 = line.toMutableList()
    var programContext = ProgramContext()
    val screen = mutableMapOf<Coord, Int>()
    val command = mutableListOf<Int>()
    while (true) {
        programContext = runIntCodeProgram(part1, emptyList(), programContext)
        if (programContext.isHalted) break
        programContext.output?.toInt()?.let { command.add(it) }
        if (command.size == 3) {
            screen[Coord(command[1], command[0])] = command[2]
            command.clear()
        }
    }
    println("part1=" + screen.values.count { it == 2 })

    val part2 = line.toMutableList()
    part2[0] = BigInteger.TWO
    programContext = ProgramContext()
    screen.clear()
    command.clear()
    val ballPos = mutableListOf<Coord>()
    var paddlePos = Coord(23, 21)
    while (true) {
        programContext = runIntCodeProgram(part2, movePaddle(ballPos, paddlePos), programContext)
        if (programContext.isHalted) break
        programContext.output?.toInt()?.let { command.add(it) }
        if (command.size == 3) {
            val c = Coord(command[1], command[0])
            screen[c] = command[2]
            if (command[2] == 4) ballPos.add(c)
            if (command[2] == 3) paddlePos = c
            command.clear()
        }
    }
    println("part2=" + screen[Coord(0, -1)])
}

private fun movePaddle(ballPos: List<Coord>, paddlePos: Coord): BigInteger {
    return if (ballPos.isEmpty()) BigInteger.ZERO
    else ballPos.last().col.compareTo(paddlePos.col).toBigInteger()
}

private fun printScreen(screen: Map<Coord, Int>) {
    printCoords(screen.keys) { c: Coord ->
        when (screen[c]) {
            1 -> "|"
            2 -> "*"
            3 -> "_"
            4 -> "."
            else -> " "
        }
    }
}
