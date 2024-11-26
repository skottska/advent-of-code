package adventofcode.y2020 // ktlint-disable filename

import adventofcode.crt
import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val earliestTimestamp = matchNumbers(lines.first()).first()
    val buses = lines.last().split(',').mapIndexedNotNull { index, s -> if (s == "x") null else Bus(index, matchNumbers(s).first()) }
    val busWaits = buses.map { it.id to it.id - (earliestTimestamp % it.id) }
    println("part1=" + busWaits.minBy { it.second }.let { it.first * it.second })

    val part2 = buses.map {
        var offset = it.index
        while (offset > it.id) offset -= it.id
        it.id.toLong() to (it.id - offset).toLong()
    }.let { crt(it) }
    println("part2=$part2")
}

private data class Bus(val index: Int, val id: Int)
