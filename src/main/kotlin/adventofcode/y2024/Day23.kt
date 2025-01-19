package adventofcode.y2024 // ktlint-disable filename

import adventofcode.concat
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val connections = readFile(MethodHandles.lookup()).map { it.substring(0, 2) to it.substring(3, 5) }
        .map { if (it.first < it.second) it.first to it.second else it.second to it.first }
    val computers = connections.flatMap { listOf(it.first, it.second) }.toSet()
    val map = computers.associateWith { c -> connections.mapNotNull { if (it.first == c) it.second else null } }
    val threeCons = map.keys.flatMap { a ->
        val bs = map.getValue(a)
        bs.flatMap { b -> map.getValue(b).filter { it in bs }.map { listOf(a, b, it) } }
    }
    val ts = computers.filter { it.startsWith("t") }
    println("part1=" + threeCons.count { it.any { c -> c in ts } })

    val part2 = computers.map { listOf(it) + longestAllConnected(it, map.getValue(it), map) }.maxBy { it.size }
    println("part2=" + part2.map { "$it," }.concat().dropLast(1))
}

private fun longestAllConnected(cur: String, rest: List<String>, map: Map<String, List<String>>): List<String> {
    val viable = rest.filter { it in map.getValue(cur) }
    return when {
        rest.isEmpty() -> emptyList()
        rest.size == 1 -> if (viable.isNotEmpty()) listOf(rest.first()) else emptyList()
        else -> {
            viable.map { con -> listOf(con) + longestAllConnected(con, viable.filter { it > con }, map) }.maxByOrNull { it.size } ?: emptyList()
        }
    }
}
