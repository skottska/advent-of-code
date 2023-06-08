package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchNumbersToBigInt
import adventofcode.readFile
import java.math.BigInteger

fun main() {
    val line = matchNumbersToBigInt(readFile("src/main/resources/y2019/day23.txt")[0])

    val firstNic = Nic(ProgramContext(), line.toMutableList(), 0)
    val nics = mutableMapOf(firstNic.id to firstNic)
    var packet = runNic(firstNic, emptyList(), isStart = true)

    while (true) {
        val receiver = nics[packet.receiver]
        packet = if (receiver != null) {
            runNic(receiver, listOf(packet.x, packet.y))
        } else {
            val newReceiver = Nic(ProgramContext(), line.toMutableList(), packet.receiver)
            runNic(newReceiver, listOf(packet.x, packet.y), isStart = true)
        }
    }
}
private fun runNic(nic: Nic, input: List<Int>, isStart: Boolean = false): Packet {
    val programInputs = when {
        isStart && input.isEmpty() -> listOf(
            listOf(nic.id, -1),
            listOf(-1),
            listOf(-1),
        )
        isStart -> listOf(
            listOf(nic.id, input.first(), input.last(), -1, -1, -1, -1),
            listOf(-1),
            listOf(-1),
        )
        input.isEmpty() -> listOf(
            listOf(-1),
            listOf(-1),
            listOf(-1),
        )
        else -> listOf(
            listOf(input.first()),
            listOf(input.last()),
            listOf(-1),
        )
    }
    println("Handling " + nic.id + " will send " + programInputs)
    nic.programContext = runIntCodeProgram(nic.program, programInputs[0].map { it.toBigInteger() }, nic.programContext)
    val receiver = nic.programContext.output?.toInt()?.also { println("RECEIVER=$it") }
    nic.programContext = runIntCodeProgram(nic.program, programInputs[1].map { it.toBigInteger() }, nic.programContext)
    val x = nic.programContext.output
    nic.programContext = runIntCodeProgram(nic.program, programInputs[2].map { it.toBigInteger() }, nic.programContext)
    val y = nic.programContext.output
    if (receiver == null || x == null || y == null) throw IllegalArgumentException("Something is null receiver=$receiver x=$x y=$y")
    return Packet(receiver, x.toInt(), y.toInt())
}
private data class Nic(var programContext: ProgramContext, val program: MutableList<BigInteger>, val id: Int)
private data class Packet(val receiver: Int, val x: Int, val y: Int)

private fun springScript(line: List<BigInteger>, instructions: List<String>, whichPart: Int) {
    val program = line.toMutableList()
    var context = ProgramContext()
    while (!context.isHalted && context.output?.toInt()?.toChar() != '\n') {
        context = runIntCodeProgram(program, emptyList(), context)
        // print(context.output?.toInt()?.toChar())
    }
    context = runIntCodeProgram(program, instructions.fold("") { total, i -> total + i + '\n' }.map { it.code.toBigInteger() }, context)
    while (!context.isHalted) {
        context = runIntCodeProgram(program, emptyList(), context)
        val res = context.output?.toInt()
        if (res != null && res > 128) println("part$whichPart=$res")
        /*else {
            res?.let { print(it.toChar()) }
        }*/
    }
}
