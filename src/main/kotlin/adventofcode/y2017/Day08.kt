package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split
import java.lang.IllegalArgumentException
import kotlin.math.max

fun main() {
    val lines = readFile("src/main/resources/y2017/day08.txt")
    val registers = mutableMapOf<String, Int>()
    var max = 0
    lines.forEach { line ->
        val split = split(line)
        val compareTo = matchNumbers(split[6]).first()
        val compareRegister = registers.getOrDefault(split[4], 0)
        val condition = when (split[5]) {
            "==" -> compareRegister == compareTo
            ">=" -> compareRegister >= compareTo
            ">" -> compareRegister > compareTo
            "!=" -> compareRegister != compareTo
            "<" -> compareRegister < compareTo
            "<=" -> compareRegister <= compareTo
            else -> throw IllegalArgumentException("What is " + split[5])
        }
        if (condition) {
            val cur = registers.getOrDefault(split[0], 0)
            registers[split[0]] = cur + matchNumbers(split[2]).first() * if (split[1] == "inc") 1 else -1
        }
        max = max(registers.values.max(), max)
    }
    println("part1=" + registers.values.max())
    println("part2=$max")
}
