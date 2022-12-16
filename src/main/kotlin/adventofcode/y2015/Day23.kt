package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.matches
import adventofcode.readFile
import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2015/day23.txt")
    println("part1="+ runCommands(lines, 0))
    println("part2="+ runCommands(lines, 1))
}

private fun runCommands(lines: List<String>, startA: Int): Int {
    var a = startA.toBigInteger()
    var b = 0
    var lineNum = 0
    while (lineNum < lines.size) {
        val line = lines[lineNum]
        when (line.take(3)) {
            "inc" -> { lineNum++; if (line.last() == 'a') a++ else b++ }
            "tpl" -> { lineNum++; if (line.last() == 'a') a *= 3.toBigInteger() else b *= 3 }
            "hlf" -> { lineNum++; if (line.last() == 'a') a /= BigInteger.TWO else b /= 2 }
            "jmp" -> { lineNum += matchNumbers(line).first()}
            "jie" -> { if ((line[4] == 'a' && a % BigInteger.TWO == BigInteger.ZERO) || (line[4] == 'b' && b % 2 == 0)) lineNum += matchNumbers(line).first() else lineNum++ }
            else-> { if ((line[4] == 'a' && a == BigInteger.ONE) || (line[4] == 'b' && b == 1)) lineNum += matchNumbers(line).first() else lineNum++ }
        }
    }
    return b
}