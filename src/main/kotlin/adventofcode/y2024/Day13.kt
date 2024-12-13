package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Equation
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
        val eq1 = Equation(buttons.first().first, buttons.last().first, dest.first)
        val eq2 = Equation(buttons.first().second, buttons.last().second, dest.second)
        return eq1.solveWhole(eq2)?.let { (a, b) -> (a * 3) + b }
    }
}
