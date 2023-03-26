package adventofcode.y2017 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val line = readFile("src/main/resources/y2017/day09.txt")[0]
    var commentBefore = false
    val strippedComments = line.mapNotNull {
        when {
            commentBefore -> { commentBefore = false; null }
            it == '!' -> { commentBefore = true; null }
            else -> it
        }
    }
    var garbageTotal = 0
    var inGarbage = false
    val strippedGarbage = strippedComments.mapNotNull {
        when {
            it == '>' -> { inGarbage = false; null }
            inGarbage -> { garbageTotal++; null }
            it == '<' -> { inGarbage = true; null }
            else -> it
        }
    }
    var total = 0; var cur = 0
    strippedGarbage.forEach {
        when (it) {
            '{' -> { cur++; total += cur }
            '}' -> cur--
            else -> Unit
        }
    }
    println("part1=$total")
    println("part2=$garbageTotal")
}
