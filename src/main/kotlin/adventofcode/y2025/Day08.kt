package adventofcode.y2025 // ktlint-disable filename

import adventofcode.Coord3D
import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val coords = readFile(MethodHandles.lookup()).map { l -> matchNumbers(l).let { Coord3D(it[0], it[1], it[2]) } }
    val distances = coords.indices.flatMap { a ->
        ((a + 1) until coords.size).map { b ->
            (coords[a] to coords[b]) to coords[a].distanceEuclidean(coords[b])
        }
    }.sortedBy { it.second }

    val circuits = distances.take(1000).map { it.first }.fold(emptyList<Set<Coord3D>>()) { acc, i -> addConnection(acc, i) }
    val part1 = circuits.map { it.size }.sortedDescending().take(3).reduce { a, b -> a * b }
    println("part1=$part1")

    distances.map { it.first }.fold(emptyList<Set<Coord3D>>()) { acc, i ->
        addConnection(acc, i).also {
            if (it.size == 1 && it.first().size == coords.size) {
                println("part2=" + (i.first.x * i.second.x))
                return
            }
        }
    }
}

private fun addConnection(circuits: List<Set<Coord3D>>, add: Pair<Coord3D, Coord3D>): List<Set<Coord3D>> {
    val a = circuits.firstOrNull { add.first in it }
    val b = circuits.firstOrNull { add.second in it }
    return when {
        a != null && b != null -> circuits.filter { it !in listOf(a, b) } + listOf(a + b)
        a != null -> circuits.filter { it != a } + listOf(a + add.second)
        b != null -> circuits.filter { it != b } + listOf(b + add.first)
        else -> circuits + listOf(setOf(add.first, add.second))
    }
}
