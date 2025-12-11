package adventofcode.y2025 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.matches
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val schemtics = readFile(MethodHandles.lookup()).map { line ->
        val target = line.substring(1, line.indexOf(']')).map { it == '#' }
        val buttons = matches(line, "\\([^\\)]+").map { matchNumbers(it) }
        val joltage = matches(line, "\\{[^\\}]+").map { matchNumbers(it) }.first()
        Schematic(target, buttons, joltage)
    }
    val minPresses = schemtics.map { it.minPresses() }
    println("nulls=" + minPresses.count { it == null })
    println("part1=" + minPresses.filterNotNull().sum())
}

private data class Schematic(val target: List<Boolean>, val buttons: List<List<Int>>, val joltage: List<Int>) {
    fun minPresses(cache: MutableMap<List<Boolean>, Long> = mutableMapOf(), curPresses: Long = 0, cur: List<Boolean> = target.map { false }): Long? {
        if (cache.getOrDefault(cur, Long.MAX_VALUE) <= curPresses) return null
        cache[cur] = curPresses
        if (target == cur) return curPresses
        return buttons.mapNotNull { button ->
            minPresses(cache, curPresses + 1, cur.mapIndexed { i, c -> if (i in button) !c else c })
        }.minOrNull()
    }
}
