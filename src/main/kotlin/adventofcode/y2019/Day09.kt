package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.math.BigInteger

fun main() {
    val line = matchNumbers(readFile("src/main/resources/y2019/day09.txt")[0]).map { it.toBigInteger() }
    println("part1=" + runIntCodeProgram(line.toMutableList(), BigInteger.ONE).output)
    println("part2=" + runIntCodeProgram(line.toMutableList(), BigInteger.TWO).output)
}
