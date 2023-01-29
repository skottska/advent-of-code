package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.IllegalArgumentException

fun main() {
    val line = matchNumbers(readFile("src/main/resources/y2019/day02.txt")[0])
    println("part1=" + runProgram(line.toMutableList(), 12, 2))
    println("part2=" + (0..99).flatMap { noun ->
        (0..99).mapNotNull { verb ->
            if (runProgram(line.toMutableList(), noun, verb) == 19690720) 100 * noun + verb else null
        }
    }.first())
}

private fun runProgram(nums: MutableList<Int>, noun: Int, verb: Int): Int {
    nums[1] = noun; nums[2] = verb
    var index = 0
    while (nums[index] != 99) {
        when (nums[index]) {
            1 -> nums[nums[index + 3]] = nums[nums[index + 1]] + nums[nums[index + 2]]
            2 -> nums[nums[index + 3]] = nums[nums[index + 1]] * nums[nums[index + 2]]
            else -> throw IllegalArgumentException("Don't know what to do with " + nums[index])
        }
        index += 4
    }
    return nums[0]
}
