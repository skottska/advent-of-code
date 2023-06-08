package adventofcode.y2019

import java.math.BigInteger

fun runIntCodeProgram(nums: MutableList<BigInteger>, initialInput: BigInteger, programContext: ProgramContext = ProgramContext()) = runIntCodeProgram(nums, mutableListOf(initialInput), programContext)

fun runIntCodeProgram(nums: MutableList<BigInteger>, input: List<BigInteger>, programContext: ProgramContext = ProgramContext()) = runIntCodeProgramMutable(
    nums,
    input.toMutableList(),
    programContext
)

fun runIntCodeProgramMutable(nums: MutableList<BigInteger>, input: MutableList<BigInteger>, programContext: ProgramContext = ProgramContext()): ProgramContext {
    val output = mutableListOf<BigInteger>()
    var index = programContext.index
    var relativeBase = programContext.relativeBase
    val param = { param: Int -> valueByMode(nums, index, param, relativeBase, false) }
    while (nums[index] != 99.toBigInteger()) {
        when (nums[index].toString().last()) {
            '1' -> index = opcodeModify(nums, index, relativeBase, BigInteger::plus)
            '2' -> index = opcodeModify(nums, index, relativeBase) { a: BigInteger, b: BigInteger -> a * b }
            '3' -> index = opcodeModifySimple(nums, index, if (input.isEmpty()) (-1).toBigInteger() else input.removeAt(0), relativeBase)
            '4' -> { output.add(param(1)); index += 2; return ProgramContext(isHalted = false, output.firstOrNull(), index, relativeBase) }
            '5' -> index = opcodeJump(nums, index, relativeBase) { a: BigInteger -> a == BigInteger.ZERO }
            '6' -> index = opcodeJump(nums, index, relativeBase) { a: BigInteger -> a != BigInteger.ZERO }
            '7' -> index = opcodeCompare(nums, index, relativeBase) { a: BigInteger, b: BigInteger -> a < b }
            '8' -> index = opcodeCompare(nums, index, relativeBase, BigInteger::equals)
            '9' -> { relativeBase += param(1).toInt(); index += 2 }
            else -> throw IllegalArgumentException("Don't know what to do with " + nums[index])
        }
    }
    return ProgramContext(isHalted = true, output.firstOrNull(), index, relativeBase)
}

data class ProgramContext(val isHalted: Boolean = false, val output: BigInteger? = null, val index: Int = 0, val relativeBase: Int = 0)

private fun valueByMode(nums: MutableList<BigInteger>, index: Int, param: Int, relativeBase: Int, writingParam: Boolean): BigInteger {
    val opcode = opcode(nums[index])
    val mode = when (param) {
        1 -> opcode[2]
        2 -> opcode[1]
        3 -> opcode[0]
        else -> throw IllegalArgumentException("No support for param num=$param")
    }
    return when {
        mode == '0' && writingParam -> nums[index + param]
        mode == '0' -> getNums(nums, getNums(nums, index + param).toInt()) // Position mode
        mode == '1' -> nums[index + param] // Immediate mode
        mode == '2' && writingParam -> getNums(nums, index + param) + relativeBase.toBigInteger()
        mode == '2' -> getNums(nums, getNums(nums, index + param).toInt() + relativeBase) // Relative mode
        else -> throw IllegalArgumentException("Unknown mode: $mode")
    }
}

private fun opcode(i: BigInteger): String = i.toString().let { (it.length..4).fold(it) { total, _ -> "0$total" } }

private fun opcodeModifySimple(nums: MutableList<BigInteger>, index: Int, input: BigInteger, relativeBase: Int): Int {
    val param = { param: Int, writingParam: Boolean -> valueByMode(nums, index, param, relativeBase, writingParam) }
    modifyNums(nums, param(1, true), input)
    return index + 2
}

private fun opcodeModify(nums: MutableList<BigInteger>, index: Int, relativeBase: Int, operator: (BigInteger, BigInteger) -> BigInteger): Int {
    val param = { param: Int, writingParam: Boolean -> valueByMode(nums, index, param, relativeBase, writingParam) }
    modifyNums(nums, param(3, true), operator(param(1, false), param(2, false)))
    return index + 4
}

private fun opcodeJump(nums: MutableList<BigInteger>, index: Int, relativeBase: Int, jumpCompare: (BigInteger) -> Boolean): Int {
    val param = { param: Int -> valueByMode(nums, index, param, relativeBase, false) }
    return if (jumpCompare(param(1))) index + 3 else param(2).toInt()
}

private fun opcodeCompare(nums: MutableList<BigInteger>, index: Int, relativeBase: Int, operator: (BigInteger, BigInteger) -> Boolean): Int {
    val param = { param: Int, writingParam: Boolean -> valueByMode(nums, index, param, relativeBase, writingParam) }
    val value = if (operator(param(1, false), param(2, false))) BigInteger.ONE else BigInteger.ZERO
    modifyNums(nums, param(3, true), value)
    return index + 4
}

private fun getNums(nums: MutableList<BigInteger>, index: Int): BigInteger {
    ((nums.size - 1)..index).forEach { _ -> nums.add(BigInteger.ZERO) }
    return nums[index]
}

private fun modifyNums(nums: MutableList<BigInteger>, index: BigInteger, value: BigInteger) = modifyNums(nums, index.toInt(), value)
private fun modifyNums(nums: MutableList<BigInteger>, index: Int, value: BigInteger) {
    ((nums.size - 1)..index).forEach { _ -> nums.add(BigInteger.ZERO) }
    nums[index] = value
}
