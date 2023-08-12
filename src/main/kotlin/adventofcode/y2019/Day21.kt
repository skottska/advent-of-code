package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchNumbersToBigInt
import adventofcode.readFile
import java.math.BigInteger

fun main() {
    val line = matchNumbersToBigInt(readFile("src/main/resources/y2019/day21.txt")[0]).toMutableList()
    val instructions1 = listOf(
        "NOT C T",
        "NOT B J",
        "OR J T",
        "NOT A J",
        "OR T J",
        "AND D J",
        "WALK"
    )
    springScript(line, instructions1, whichPart = 1)

    val instructions2 = listOf(
        "NOT C T",
        "NOT B J",
        "OR J T",
        "NOT A J",
        "OR T J",
        "AND D J",
        "NOT E T",
        "NOT T T",
        "OR H T",
        "AND T J",
        "RUN"
    )
    springScript(line, instructions2, whichPart = 2)
}

private fun springScript(line: List<BigInteger>, instructions: List<String>, whichPart: Int) {
    val program = line.toMutableList()
    var context = ProgramContext()
    while (!context.isHalted && context.output?.toInt()?.toChar() != '\n') {
        context = runIntCodeProgram(program, emptyList(), context)
    }
    context = runIntCodeProgram(program, instructions.fold("") { total, i -> total + i + '\n' }.map { it.code.toBigInteger() }, context)
    while (!context.isHalted) {
        context = runIntCodeProgram(program, emptyList(), context)
        val res = context.output?.toInt()
        if (res != null && res > 128) println("part$whichPart=$res")
    }
}
