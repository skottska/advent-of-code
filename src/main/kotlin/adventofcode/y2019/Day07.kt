package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val line = matchNumbers(readFile("src/main/resources/y2019/day07.txt")[0])
    val part1 = generatePhases(0..4).maxOf {
        it.fold(0) { total, phase -> runProgram(line.toMutableList(), phase, total).output }
    }
    println("part1=$part1")
    val part2 = listOf(listOf(9,7,8,5,6)).maxOf {
        var input = ProgramOutput(false, 0)
        while (!input.isHalted) {
            input = it.fold(input) { total, phase -> runProgram(line.toMutableList(), phase, total.output) }
            println(input)
            Thread.sleep(1000)
        }
        input.output
    }
    println("part2=$part2")
}

private fun generatePhases(range: IntRange, prefix: List<Int> = emptyList()): List<List<Int>> =
    range.filter { !prefix.contains(it) }.map {
        val next = prefix + listOf(it)
        if (next.size == 5) listOf(next) else generatePhases(range, next)
    }.flatten()

private fun runProgram(nums: MutableList<Int>, phase: Int, initialInput: Int): ProgramOutput {
    val input = mutableListOf(phase, initialInput)
    var index = 0
    while (nums[index] != 99) {
        when (nums[index].toString().last()) {
            '1' -> index = opcodeModify(nums, index, Int::plus)
            '2' -> index = opcodeModify(nums, index) { a: Int, b: Int -> a * b }
            '3' -> index = opcodeModifySimple(nums, index, input.removeAt(0))
            '4' -> {
                val result = posOrImm(nums, index + 1, if (nums[index] == 4) '0' else '1')
                if (phase in (5..9)) {
                    return ProgramOutput(false, result)
                } else { input.add(result); index += 2 }
            }
            '5' -> index = opcodeJump(nums, index) { a: Int -> a == 0 }
            '6' -> index = opcodeJump(nums, index) { a: Int -> a != 0 }
            '7' -> index = opcodeCompare(nums, index) { a: Int, b: Int -> a < b }
            '8' -> index = opcodeCompare(nums, index, Int::equals)
            else -> throw IllegalArgumentException("Don't know what to do with " + nums[index])
        }
    }
    if (input.size != 1) throw IllegalArgumentException("Don't know what to do with " + input)
    return ProgramOutput(true, input.first()).also { println("HALTED") }
}

private data class ProgramOutput(val isHalted: Boolean, val output: Int)

private fun posOrImm(nums: MutableList<Int>, i: Int, c: Char) = when (c) { '0' -> nums[nums[i]] else -> nums[i] }
private fun opcode(i: Int) = i.toString().let { (it.length..3).fold(it) { total, _ -> "0$total" } }

private fun opcodeModifySimple(nums: MutableList<Int>, index: Int, input: Int): Int {
    if (nums[index].toString().length > 1) throw IllegalArgumentException("what am i: " + nums[index])
    nums[nums[index + 1]] = input
    return index + 2
}

private fun opcodeModify(nums: MutableList<Int>, index: Int, operator: (Int, Int) -> Int): Int {
    val opcode = opcode(nums[index])
    nums[nums[index + 3]] = operator(posOrImm(nums, index + 1, opcode[1]), posOrImm(nums, index + 2, opcode[0]))
    return index + 4
}

private fun opcodeJump(nums: MutableList<Int>, index: Int, jumpCompare: (Int) -> Boolean): Int {
    val opcode = opcode(nums[index])
    val jump = posOrImm(nums, index + 1, opcode[1])
    return if (jumpCompare(jump)) index + 3 else posOrImm(nums, index + 2, opcode[0])
}

private fun opcodeCompare(nums: MutableList<Int>, index: Int, operator: (Int, Int) -> Boolean): Int {
    val opcode = opcode(nums[index])
    nums[nums[index + 3]] = if (operator(posOrImm(nums, index + 1, opcode[1]), posOrImm(nums, index + 2, opcode[0]))) 1 else 0
    return index + 4
}
