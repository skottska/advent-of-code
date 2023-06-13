package adventofcode.y2019 // ktlint-disable filename

import adventofcode.asString
import adventofcode.matchNumbersToBigInt
import adventofcode.readFile
import java.math.BigInteger

fun main() {
    val line = matchNumbersToBigInt(readFile("src/main/resources/y2019/day25.txt")[0])
    val program = line.toMutableList()
    var context = ProgramContext()

    context = listOf(
        "", "east", "east", "take semiconductor", "north", "take planetoid", "north", "take antenna", "south", "west", "take food ration", "west", "west", "take monolith", "east",
        "east", "north", "take space law space brochure", "north", "north", "take weather machine", "south", "south", "east", "take jam", "west", "south", "east", "south", "east",
        "south", "south", "east", "east"
    ).fold(context) { result, action -> runProgram(result, program, action, false) }

    val inventory = listOf("food ration", "weather machine", "antenna", "space law space brochure", "jam", "semiconductor", "planetoid", "monolith").sorted()

    context = inventory.fold(context) { result, inv -> runProgram(result, program, "drop $inv", false) }
    val validInventory = recurseProgram(context, program, leftInventory = inventory, curInventory = emptyList()) ?: throw IllegalStateException("no valid inventory found!")
    context = validInventory.fold(context) { result, inv -> runProgram(result, program, "take $inv", false) }
    runProgram(context, program, "east")
}

private fun recurseProgram(contextIn: ProgramContext, programIn: MutableList<BigInteger>, leftInventory: List<String>, curInventory: List<String>): List<String>? {
    val terminateOn = "Command?\n"
    return leftInventory.firstNotNullOfOrNull {
        val newInventory = curInventory + it
        val program = programIn.toMutableList()
        var context = contextIn
        val output = mutableListOf<Char>()
        context = newInventory.fold(context) { result, inv -> runProgram(result, program, "take $inv", false) }
        val inputList = "east\n".map { c -> c.code.toBigInteger() }.toMutableList()
        while (!context.isHalted && output.takeLast(terminateOn.length).asString() != terminateOn) {
            context = runIntCodeProgram(program, inputList, context)
            output.add(context.output?.toInt()?.toChar() ?: ' ')
        }
        when {
            output.asString().contains("this ship are lighter") -> null
            output.asString().contains("this ship are heavier") -> recurseProgram(contextIn, programIn, leftInventory.filter { i -> i > it }, newInventory)
            else -> newInventory.also { r -> println("Found a result=$r") }
        }
    }
}

private fun runProgram(contextIn: ProgramContext, program: MutableList<BigInteger>, input: String, print: Boolean = true): ProgramContext {
    var context = contextIn
    val output = mutableListOf<Char>()
    val terminateOn = "Command?\n"
    val inputList = if (input.isEmpty()) emptyList() else (input + "\n").map { it.code.toBigInteger() }.toMutableList()
    while (!context.isHalted && output.takeLast(terminateOn.length).asString() != terminateOn) {
        context = runIntCodeProgram(program, inputList, context)
        output.add(context.output?.toInt()?.toChar() ?: ' ')
        if (print) print(context.output?.toInt()?.toChar())
    }
    return context
}
