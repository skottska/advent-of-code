package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.DirectedCoord
import adventofcode.Facing
import adventofcode.matchNumbersToBigInt
import adventofcode.readFile
import java.math.BigInteger
import kotlin.system.exitProcess

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
    val junctions = area.keys.filter {
        listOf(
            Coord(it.row, it.col - 1),
            Coord(it.row, it.col + 1),
            Coord(it.row + 1, it.col),
            Coord(it.row - 1, it.col)
        ).all { c -> area.getOrDefault(c, ".") != "." }
    }.toSet()

    println("part1=" + junctions.sumOf { it.row * it.col })

    val start = DirectedCoord(facing = Facing.UP, coord = area.filter { it.value == "^" }.keys.first())

    val ones = commandsFrom(start, listOf(start.coord), junctions, area).map { commandStatesFrom(listOf(it.command), area, junctions, CommandState(it.command, listOf(start.coord), start)) }.flatten()
    ones.map { one ->
        val twos = commandsFrom(one.cur, one.been, junctions, area).map { commandStatesFrom(listOf(it.command, one.command), area, junctions, CommandState(it.command, listOf(start.coord), start)) }.flatten()
        twos.map { two ->
            commandsFrom(two.cur, two.been, junctions, area).filter { it !in ones && it !in twos }.map { three ->
                val result = applyCommands(start, area, setOf(one.command, two.command, three.command), emptyList(), emptyList(), 286, junctions)
                if (result != null) {
                    val routine = result.map { function ->
                        when (function) {
                            one.command -> 65
                            two.command -> 66
                            else -> 67
                        }.let { listOf(it, 44) }
                    }.flatten().dropLast(1) + 10

                    val functions = listOf(one, two, three).map { func ->
                        func.command.movements.flatMap { m ->
                            when (m.action) {
                                Action.RIGHT -> listOf(82)
                                Action.LEFT -> listOf(76)
                                Action.FORWARD -> m.num.toString().map { it.digitToInt() + 48 }
                            }.let { it + 44 }
                        }.dropLast(1) + 10
                    }

                    val linePart2 = line.toMutableList()
                    linePart2[0] = BigInteger.TWO
                    val executions = (listOf(routine) + functions).map { it to ':' } + (listOf(110, 10) to '?')
                    programContext = executions.fold(ProgramContext()) { context, exec ->
                        runIntCodeProgram(linePart2, exec.first.map { it.toBigInteger() }, handleIntCodeOutput(context, linePart2, exec.second))
                    }
                    handleIntCodeOutput(programContext, linePart2)
                    exitProcess(0)
                }
            }
        }
    }
}

private fun handleIntCodeOutput(inProgramContext: ProgramContext, linePart2: MutableList<BigInteger>, compareTo: Char = ':'): ProgramContext {
    var programContext = inProgramContext
    while (!programContext.isHalted && programContext.output?.toInt()?.toChar() != compareTo) {
        programContext = runIntCodeProgram(linePart2, emptyList(), programContext).also {
            if ((it.output ?: BigInteger.ZERO) > 1000.toBigInteger()) println("part2=" + it.output)
        }
    }
    programContext = runIntCodeProgram(linePart2, emptyList(), programContext)
    return programContext
}

private fun commandStatesFrom(commands: List<Command>, area: Map<Coord, String>, junctions: Set<Coord>, startCommandState: CommandState): List<CommandState> {
    return commands.mapNotNull {
        val result = applyCommand(it, startCommandState, area, junctions)
        if (result != null) listOf(result) + commandStatesFrom(commands, area, junctions, result) else null
    }.flatten()
}

private fun applyCommands(cur: DirectedCoord, area: Map<Coord, String>, commands: Set<Command>, routine: List<Command>, been: List<Coord>, numPaths: Int, junctions: Set<Coord>): List<Command>? {
    if (routine.size > 10) return null
    if (been.toSet().size >= numPaths - 1) return routine
    return commands.firstNotNullOfOrNull {
        val newCommandState = applyCommand(it, CommandState(it, been, cur), area, junctions)
        if (newCommandState != null) applyCommands(newCommandState.cur, area, commands, routine + it, newCommandState.been, numPaths, junctions) else null
    }
}

private fun applyCommand(command: Command, commandState: CommandState, area: Map<Coord, String>, junctions: Set<Coord>): CommandState? = command.movements.fold(commandState) { total, m ->
    val options = options(total.cur, total.been, junctions, area)
    when (m.action) {
        Action.FORWARD -> {
            (1..m.num).fold(total) { forwardTotal, _ ->
                val newCoord = forwardTotal.cur.forward()
                forwardTotal.copy(cur = newCoord).let { if (!isAnOption(it.cur, it.been, junctions, area)) return null else it.copy(been = forwardTotal.been + newCoord.coord) }
            }
        }
        Action.RIGHT -> if (Action.RIGHT in options) total.copy(cur = total.cur.right()) else return null
        Action.LEFT -> if (Action.LEFT in options) total.copy(cur = total.cur.left()) else return null
    }
}

private data class CommandState(val command: Command, val been: List<Coord>, val cur: DirectedCoord)
private data class Command(val movements: List<Movement>) {
    fun add(m: Movement) = copy(
        movements = when {
            movements.isEmpty() -> listOf(m)
            m.action != Action.FORWARD -> movements + m
            movements.last().action != Action.FORWARD -> movements + m
            else -> {
                val last = movements.last()
                movements.dropLast(1) + last.copy(num = last.num + m.num)
            }
        }
    )
    fun add(c: Command) = c.movements.fold(this) { total, i -> total.add(i) }
}
private data class Movement(val action: Action, val num: Int)
private enum class Action { FORWARD, LEFT, RIGHT }

private fun options(cur: DirectedCoord, been: List<Coord>, junctions: Set<Coord>, area: Map<Coord, String>): List<Action> =
    listOf(cur.forward() to Action.FORWARD, cur.left().forward() to Action.LEFT, cur.right().forward() to Action.RIGHT)
        .filter { isAnOption(it.first, been, junctions, area) }
        .map { it.second }

private fun isAnOption(cur: DirectedCoord, been: List<Coord>, junctions: Set<Coord>, area: Map<Coord, String>) =
    area.getOrDefault(cur.coord, ".") == "#" && (cur.coord !in been || (cur.coord in junctions && been.count { it == cur.coord } < 2))

private fun commandsFrom(cur: DirectedCoord, been: List<Coord>, junctions: Set<Coord>, area: Map<Coord, String>, curCommand: Command = Command(emptyList())): List<CommandState> {
    return options(cur, been, junctions, area).map { action ->
        val singleCommand = Command(listOf(Movement(action, 1)))
        val combinedCommand = curCommand.add(singleCommand)
        val newCoord = applyCommand(singleCommand, CommandState(singleCommand, been + cur.coord, cur), area, junctions)?.cur
        if (combinedCommand.movements.size > 10 || newCoord == null || (combinedCommand.movements.size > 1 && combinedCommand.movements.takeLast(2).none { it.action == Action.FORWARD })) emptyList()
        else {
            val newBeen = been + newCoord.coord
            listOf(CommandState(combinedCommand, newBeen, newCoord)) + commandsFrom(newCoord, newBeen, junctions, area, combinedCommand)
        }
    }.flatten()
}
