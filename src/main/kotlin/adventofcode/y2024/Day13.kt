package adventofcode.y2024 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val claws = readFile(MethodHandles.lookup()).map { matchNumbersLong(it) }.windowed(size = 4, step = 4, partialWindows = true).map {
        Claw(it.subList(0, 2).map { b -> b.first() to b.last() }, it[2].first() to it[2].last())
    }
    println("part1="+claws.mapNotNull { it.minClaw() }.sum())
    println("part2="+claws.mapNotNull { it.toPart2(10000000000000L).minClaw() }.sum())
}

private data class Claw(val buttons: List<Pair<Long, Long>>, val dest: Pair<Long, Long>) {
    fun toPart2(inc: Long) = copy(dest = (dest.first + inc) to (dest.second + inc))

    fun minClaw(): Long? {
        val leftDivisor = buttons.first().first * 3
        val rightDivisor = buttons.first().second * 3
        val numB = (-buttons.last().first * rightDivisor) + (buttons.last().second * leftDivisor)
        val value = (dest.second * leftDivisor) - (dest.first *  rightDivisor)
        val b = divisor(value, numB) ?: return null
        val a = divisor(dest.first - b * buttons.last().first, buttons.first().first) ?: return null
        return (a * 3) + b
    }
}

private fun divisor(a: Long, b: Long): Long? = if ( a % b == 0L) a / b else null
