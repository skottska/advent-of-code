package adventofcode.y2017 // ktlint-disable filename

import adventofcode.asString
import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val danceMoves = readFile("src/main/resources/y2017/day16.txt").first().split(",")
    println("part1=" + loopDance(danceMoves, 1))
    println("part2=" + loopDance(danceMoves, 1_000_000_000L % whenDoesDanceRepeat(danceMoves)))
}

private fun loopDance(danceMoves: List<String>, times: Long) =
    (1..times).fold(('a'..'p').toList().asString()) { start, _ -> performDance(danceMoves, start) }

private fun performDance(danceMoves: List<String>, startPos: String) = danceMoves.fold(startPos) { cur, move ->
    val nums = matchNumbers(move)
    when (move.first()) {
        's' -> cur.takeLast(nums.first()) + cur.dropLast(nums.first())
        'x' -> switchAt(cur, nums.first(), nums.last())
        else -> switchAt(cur, cur.indexOf(move[1]), cur.indexOf(move[3]))
    }
}

private fun whenDoesDanceRepeat(danceMoves: List<String>, startPos: String = ('a'..'p').toList().asString()): Long {
    var num = 1L
    var cur = performDance(danceMoves, startPos)
    while (cur != startPos) {
        num++
        cur = performDance(danceMoves, cur)
    }
    return num
}

private fun String.replaceAt(index: Int, c: Char) = replaceRange(index..index, c.toString())
private fun switchAt(s: String, a: Int, b: Int) = s[a].let { s.replaceAt(a, s[b]).replaceAt(b, it) }
