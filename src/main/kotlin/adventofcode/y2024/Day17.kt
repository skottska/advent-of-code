package adventofcode.y2024 // ktlint-disable filename

import adventofcode.concat
import adventofcode.matchNumbers
import adventofcode.matchNumbersLong
import adventofcode.power
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val a = matchNumbersLong(lines[0]).first()
    val b = matchNumbersLong(lines[1]).first()
    val c = matchNumbersLong(lines[2]).first()
    val program = matchNumbers(lines[4])
    println("part1="+ iterate(a, b, c, program).map { "$it," }.concat().dropLast(1))

    var cur = 0L
    program.reversed().forEach { p ->
        var result = iterate(cur, b, c, program)
        while (result != program.takeLast(result.size)) result = iterate(++cur, b, c, program)
        cur *= 8
    }
    println("part2="+(cur/8))
}

private fun iterate(aIn: Long, bIn: Long, cIn: Long, program: List<Int>): List<Int> {
    var a = aIn
    var b = bIn
    var c = cIn

    val output = mutableListOf<Int>()
    var pos = 0
    while (pos < program.size - 1) {
        val opcode = program[pos]
        val operand = program[pos + 1]
        when (opcode) {
            0 -> a = (a / power(2, combo(a, b, c, operand)))
            1 -> b = b xor operand.toLong()
            2 -> b = combo(a, b, c, operand) % 8
            3 -> if (a != 0L) pos = operand - 2
            4 -> b = b xor c
            5 -> output += (combo(a, b, c, operand) % 8).toInt()
            6 -> b = a / power(2L, combo(a, b, c, operand))
            7 -> c = a / power(2L, combo(a, b, c, operand))
        }
        pos += 2
    }
    return output
}

private fun combo(a: Long, b: Long, c: Long, operand: Int): Long = when (operand) {
    in 0..3 -> operand.toLong()
    4 -> a
    5 -> b
    6 -> c
    else -> throw IllegalArgumentException("Invalid opcode: $operand")
}