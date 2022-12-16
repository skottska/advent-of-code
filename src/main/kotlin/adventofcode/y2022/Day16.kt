package adventofcode.y2022 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.matches
import adventofcode.readFile
import adventofcode.split
import kotlin.math.max

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day16.txt")
    val valves = lines.associate { line ->
        val match = matches(line, "[A-Z][A-Z]")
        match[0] to Valve(matchNumbers(split(line)[4]).first(), match.drop(1))
    }
    openValves(currentValve = "AA", valves = valves, timeLeft = 30, history = listOf("AA"))
    println("part1=$maxPressure")
}

var maxPressure: Int? = null

private fun openValves(currentValve: String, valves: Map<String, Valve>, timeLeft: Int, openValves: List<String> = emptyList(), pressure: Int = 0, history: List<String> = emptyList()) {
    if (maxPressure?.let { it >= maxPressure(valves, openValves, timeLeft) + pressure } == true) return
    if (timeLeft == 0 || valves.values.filter { it.rate != 0 }.size == openValves.size) {
        val a = maxPressure
        maxPressure = maxPressure?.let { max(it, pressure) } ?: pressure
        if (a != maxPressure) println("$maxPressure -> $history")
        return
    }
    if (history.size >= 4 && history.takeLast(4).toSet().size == 2) return
    valves[currentValve] ?.let {
        if (!openValves.contains(currentValve) && it.rate != 0) {
            openValves(
                currentValve,
                valves,
                timeLeft - 1,
                openValves + listOf(currentValve),
                pressure + it.rate * (timeLeft - 1),
                history + listOf("open>$currentValve")
            )
        }
        it.leadsTo.forEach { leadsTo ->
            openValves(leadsTo, valves, timeLeft - 1, openValves, pressure, history + listOf(leadsTo))
        }
    }
}

private fun maxPressure(valves: Map<String, Valve>, openValves: List<String>, timeLeft: Int): Int {
    var t = timeLeft
    var max = 0
    valves.entries.filter { it.value.rate != 0 }.filter { !openValves.contains(it.key) }.sortedByDescending { it.value.rate }.forEach {
        if (t <= 1) return max
        max += it.value.rate * (t - 1)
        t -= 2
    }
    return max
}

private data class Valve(val rate: Int, val leadsTo: List<String>)
