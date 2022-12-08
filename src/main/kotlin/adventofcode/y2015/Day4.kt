package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile
import java.math.BigInteger
import java.security.MessageDigest

fun main(args: Array<String>) {
    val line = readFile("src/main/resources/y2015/day4.txt")[0]
    println("part1=" + findMd5(line, 5))
    println("part2=" + findMd5(line, 6))
}
fun findMd5(line: String, numZeroes: Int): Int {
    var prefix = ""
    repeat(numZeroes) { prefix += "0" }
    var i = 1
    while (true) {
        if (md5(line + i).startsWith(prefix)) return i
        i++
    }
}

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}
