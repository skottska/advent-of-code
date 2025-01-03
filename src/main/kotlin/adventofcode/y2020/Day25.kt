package adventofcode.y2020 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val keys = readFile(MethodHandles.lookup()).map { matchNumbers(it).first() }
    val loops = keys.map { findLoop(it) }

    val part1 = (1..loops.first()).fold(1L) { total, _ -> (total * keys.last()) % 20201227 }
    println("part1=$part1")
}

private fun findLoop(target: Int): Int {
    var cur = 1L
    var loop = 0
    while (cur != target.toLong()) {
        loop++
        cur = (cur * 7) % 20201227
    }
    return loop
}
