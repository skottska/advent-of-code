package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val line = readFile("src/main/resources/y2015/day10.txt")[0]
    var result = java.lang.StringBuilder(line)
    repeat(40) { result = iterate(result) }
    println("part1=" + result.length)

    repeat(10) { result = iterate(result) }
    println("part2=" + result.length)
}

fun iterate(s: StringBuilder): StringBuilder {
    val result: StringBuilder = java.lang.StringBuilder()
    var curChar: Char? = null
    var curNum = 0
    s.forEach {
        if (curChar == null) curChar = it
        if (curChar == it) curNum++
        else {
            result.append(curNum).append(curChar)
            curChar = it
            curNum = 1
        }
    }
    return result.append(curNum).append(curChar)
}
