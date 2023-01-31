package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.IllegalArgumentException

fun main() {
    val line = matchNumbers(readFile("src/main/resources/y2019/day05.txt")[0])
    println("part1=" + runProgram(line.toMutableList(), input = 1))
    println("part2=" + runProgram(line.toMutableList(), input = 5))
}

private fun runProgram(nums: MutableList<Int>, input: Int): Int {
    nums[nums[1]] = input
    var index = 2
    while (nums[index] != 99) {
        when (nums[index].toString().last()) {
            '1' -> index = opcodeModify(nums, index, Int::plus)
            '2' -> index = opcodeModify(nums, index) { a: Int, b: Int -> a * b }
            '4' -> posOrImm(nums, index + 1, if (nums[index] == 4) '0' else '1').let { if (it != 0) return it else index += 2 }
            '5' -> index = opcodeJump(nums, index) { a: Int -> a == 0 }
            '6' -> index = opcodeJump(nums, index) { a: Int -> a != 0 }
            '7' -> index = opcodeCompare(nums, index) { a: Int, b: Int -> a < b }
            '8' -> index = opcodeCompare(nums, index, Int::equals)
            else -> throw IllegalArgumentException("Don't know what to do with " + nums[index])
        }
    }
    return -1
}

private fun posOrImm(nums: MutableList<Int>, i: Int, c: Char) = when (c) { '0' -> nums[nums[i]] else -> nums[i] }
private fun opcode(i: Int) = i.toString().let { (it.length..3).fold(it) { total, _ -> "0$total" } }

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
