package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2015/day8.txt")
    println("part1=" + lines.fold(0) { total, line -> total + line.length - decode(line).length })
    println("part2=" + lines.fold(0) { total, line -> total + encode(line).length - line.length })
}

fun decode(s: String) = s.substring(1, s.length - 1)
    .replace("\\\\", "\\")
    .replace("\\\"", "\"")
    .replace(Regex("\\\\x[0-9a-f][0-9a-f]"), "A")

fun encode(s: String) = s
    .replace("\\", "\\\\")
    .replace("\"", "\\\"")
    .let { "\"" + it + "\"" }
