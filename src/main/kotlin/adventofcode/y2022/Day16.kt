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
        match[0] to Valve(match[0], matchNumbers(split(line)[4]).first(), match.drop(1))
    }.also { populateShortestRoutes(it) }

    val opener = Opener("AA")
    openValves(listOf(opener), valves, timeLeft = 30)
    val part1 = "part1=$maxPressure"

    maxPressure = null
    openValves(listOf(opener, opener), valves, timeLeft = 26)
    println(part1)
    println("part2=$maxPressure")
}

var maxPressure: Int? = null
val shortestRoutes: MutableMap<Pair<String, String>, Int> = mutableMapOf()

private fun openValves(openers: List<Opener>, valves: Map<String, Valve>, timeLeft: Int, pressure: Int = 0) {
    val a = maxPressure
    maxPressure = maxPressure?.let { max(it, pressure) } ?: pressure
    if (a != maxPressure) println("$maxPressure -> " + openers.map { it.history })

    if (maxPressure?.let { it >= maxPressure(valves, timeLeft, openers) + pressure } == true) return
    if (timeLeft == 0 || valves.values.all { it.isOpen }) return
    if (openers.any { opener -> opener.history.size >= 3 && opener.history.takeLast(3).toSet().size == 2 }) return
    if (openers.any { opener -> opener.history.size >= 5 && opener.history.takeLast(5).toSet().size == 3 }) return

    if (openers.all { it.canOpen(valves) } && openers.distinctBy { it.currentValve }.size == openers.size) {
        openValves(openers.map { it.open() }, valves.open(openers), timeLeft - 1, pressure + openers.pressure(valves, timeLeft))
    }

    if (openers.size == 2) {
        openAndMove(openers.first(), openers.last(), valves, timeLeft, pressure)
        openAndMove(openers.last(), openers.first(), valves, timeLeft, pressure)

        openers[0].leadsTo(valves).forEach { leadsTo0 ->
            openers[1].leadsTo(valves).forEach { leadsTo1 ->
                openValves(listOf(openers[0].move(leadsTo0), openers[1].move(leadsTo1)), valves, timeLeft - 1, pressure)
            }
        }
    } else {
        openers[0].leadsTo(valves).forEach { leadsTo -> openValves(listOf(openers[0].move(leadsTo)), valves, timeLeft - 1, pressure) }
    }
}

private fun openAndMove(a: Opener, b: Opener, valves: Map<String, Valve>, timeLeft: Int, pressure: Int) {
    if (a.canOpen(valves)) {
        b.leadsTo(valves).forEach { leadsTo ->
            openValves(listOf(a.open(), b.move(leadsTo)), valves.open(a), timeLeft - 1, pressure + a.pressure(valves, timeLeft))
        }
    }
}

private fun maxPressure(valves: Map<String, Valve>, timeLeft: Int, openers: List<Opener>): Int {
    val notOpen = valves.entries.filter { !it.value.isOpen }
    if (notOpen.isEmpty()) return 0
    val shortestRoute = notOpen.minOf { v -> openers.minOf { getShortestRoute(v.key, it.currentValve) } }
    if (shortestRoute >= timeLeft) return 0
    var t = timeLeft - shortestRoute
    var max = 0
    notOpen.sortedByDescending { it.value.rate }.chunked(openers.size) { chunk ->
        if (t > 1) { max += chunk.sumOf { it.value.rate } * (t - 1); t -= 2 }
    }
    return max
}

private fun Map<String, Valve>.getValve(s: String) = this[s] ?: throw IllegalArgumentException("Cannot find $s")

private fun populateShortestRoutes(valves: Map<String, Valve>) {
    valves.entries.forEach { a ->
        a.value.leadsTo.forEach { b ->
            addShortestRoute(a.key, b, 1)
            populateShortestRoutesInternal(a.key, b, valves, 1)
        }
    }
}

private fun populateShortestRoutesInternal(a: String, b: String, valves: Map<String, Valve>, currentDist: Int) {
    valves[b]?.leadsTo?.forEach { c ->
        if (addShortestRoute(a, c, currentDist + 1)) populateShortestRoutesInternal(a, c, valves, currentDist + 1)
    }
}

private fun getShortestRoute(a: String, b: String): Int {
    if (a == b) return 0
    return shortestRoutes[Pair(minString(a, b), maxString(a, b))] ?: throw IllegalArgumentException("Can't find route from $a to $b")
}

private fun addShortestRoute(a: String, b: String, dist: Int): Boolean {
    if (a == b) return false
    val p = Pair(minString(a, b), maxString(a, b))
    if (shortestRoutes[p]?.let { it >= dist } != false) { shortestRoutes[p] = dist; return true }
    return false
}
private fun minString(a: String, b: String) = if (a <= b) a else b
private fun maxString(a: String, b: String) = if (a >= b) a else b

private fun Map<String, Valve>.open(openers: List<Opener>) = openers.fold(this) { total, it -> total.open(it) }
private fun Map<String, Valve>.open(opener: Opener) = map { it.key to if (it.key == opener.currentValve) it.value.copy(isOpen = true) else it.value }.toMap()
private fun List<Opener>.pressure(valves: Map<String, Valve>, timeLeft: Int) = sumOf { it.pressure(valves, timeLeft) }

private data class Opener(val currentValve: String, val history: List<String>) {
    constructor(currentValve: String) : this(currentValve, listOf(currentValve))
    fun move(valve: String) = copy(currentValve = valve, history = history + listOf(valve))
    fun canOpen(valves: Map<String, Valve>) = valves[currentValve]?.let { !it.isOpen } ?: false
    fun leadsTo(valves: Map<String, Valve>) = valves.getValve(currentValve).leadsTo.filter { valves.keys.contains(it) }
    fun pressure(valves: Map<String, Valve>, timeLeft: Int) = (valves[currentValve]?.rate ?: 0) * (timeLeft - 1)
    fun open() = copy(history = history + listOf("open>$currentValve"))
}
private data class Valve(val name: String, val rate: Int, val leadsTo: List<String>, val isOpen: Boolean) {
    constructor(name: String, rate: Int, leadsTo: List<String>) : this(name, rate, leadsTo, rate == 0)
}
