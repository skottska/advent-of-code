package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.math.BigInteger

fun main() {
    val line = readFile("src/main/resources/y2016/day09.txt")[0]
    println("part1=" + decompress(line).length)
    println("part2=" + recursiveDecompress(line))
}

private fun decompress(s: String): String {
    val res = StringBuilder("")
    var index = 0
    while (index < s.length) {
        if (s[index] != '(') res.append(s[index++])
        else {
            val end = s.indexOf(')', index) + 1
            val nums = matchNumbers(s.substring(index, end))
            (1..nums.last()).forEach { _ -> res.append(s.substring(end, end + nums.first())) }
            index = end + nums.first()
        }
    }
    return res.toString()
}

private fun recursiveDecompress(s: String): BigInteger {
    var size = BigInteger.ZERO
    var index = 0
    while (index < s.length) {
        if (s[index] != '(') {
            size++; index++
        } else {
            val end = s.indexOf(')', index) + 1
            val nums = matchNumbers(s.substring(index, end))
            size += nums.last().toBigInteger() * recursiveDecompress(s.substring(end, end + nums.first()))
            index = end + nums.first()
        }
    }
    return size
}
