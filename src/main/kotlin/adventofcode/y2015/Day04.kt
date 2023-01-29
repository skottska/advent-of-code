package adventofcode.y2015 // ktlint-disable filename

import adventofcode.md5
import adventofcode.readFile

fun main() {
    val line = readFile("src/main/resources/y2015/day04.txt")[0]
    println("part1=" + findMd5(line, 5))
    println("part2=" + findMd5(line, 6))
}
fun findMd5(line: String, numZeroes: Int): Int {
    var prefix = ""
    repeat(numZeroes) { prefix += "0" }
    var i = 1
    while (true) {
        if (md5(line + i).startsWith(prefix)) return i else i++
    }
}
