package adventofcode.y2018 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    println("part1=" + iterate(listOf(0, 0, 0, 0, 0, 0), lines))
    println("part2=" + iterate(listOf(1, 0, 0, 0, 0, 0), lines))
}

private fun iterate(registersIn: List<Int>, lines: List<String>): Int {
    val pointer = matchNumbers(lines.first()).first()

    val instructions = lines.drop(1).map { SampleInstruction(it.take(4), matchNumbers(it), pointer) }

    var cur = 0
    var registers = registersIn
    while (cur < instructions.size) {
        registers = if (cur == 3) loopLine5toLine12(registers) else instructions[cur].apply(registers)
        cur = registers[pointer]
    }
    return registers.first()
}

private fun loopLine5toLine12(registersIn: List<Int>): List<Int> {
    val r = registersIn.toMutableList()
    if (registersIn[1] >= registersIn[2]) {
        val divisor = r[1] / r[4]
        if (divisor * r[4] == r[1] && divisor in r[2]..r[1]) r[0] += r[4]
        r[2] = r[1] + 1
    } else {
        if (r[4] * r[2] == r[1]) r[0] += r[4]
        r[2] += 1
    }
    r[3] = 1
    r[5] = 12
    return r
}

private data class SampleInstruction(val name: String, val opcode: List<Int>, val pointer: Int) {
    private val func = sampleFuncsNames().first { it.first == name }.second
    private val fullOpcode = listOf(0) + opcode
    fun apply(registers: List<Int>): List<Int> = func(Sample(registers, fullOpcode, emptyList())).mapIndexed { index, i -> if (index == pointer) i + 1 else i }
}
