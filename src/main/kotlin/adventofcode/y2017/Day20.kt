package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import kotlin.math.abs
import kotlin.math.sqrt

fun main() {
    val grid = readFile("src/main/resources/y2017/day20.txt").mapIndexed { index, i -> index to matchNumbers(i) }

    val longTerm = grid.sortedWith { a, b ->
        val func = { list: Pair<Int, List<Int>>, from: Int, to: Int -> list.second.subList(from, to).sumOf { abs(it) } }
        val acc = func(a, 6, 9).compareTo(func(b, 6, 9))
        val vel = func(a, 3, 6).compareTo(func(b, 3, 6))
        val start = func(a, 0, 3).compareTo(func(b, 0, 3))
        when {
            acc != 0 -> acc
            vel != 0 -> vel
            else -> start
        }
    }
    println("part1=" + longTerm.first().first)

    val particles = grid.map {
        Particle(
            it.first,
            Quadratic(it.second[6], it.second[3], it.second[0]),
            Quadratic(it.second[7], it.second[4], it.second[1]),
            Quadratic(it.second[8], it.second[5], it.second[2]),
        )
    }.toMutableList()

    var collisions = particles.mapIndexed { index: Int, a: Particle ->
        (index + 1 until particles.size).mapNotNull { indexB ->
            val b = particles[indexB]
            a.earliestCollision(b)?.let { Pair(a, b) to it }
        }
    }.flatten().toMap()

    while (collisions.isNotEmpty()) {
        val min = collisions.values.min()
        val toBeRemoved = collisions.filter { it.value == min }.flatMap { listOf(it.key.first, it.key.second) }.toSet()
        particles.removeAll(toBeRemoved)
        collisions = collisions.filter { it.key.first !in toBeRemoved && it.key.second !in toBeRemoved }
    }

    println("part2=" + particles.size)
}

private data class Quadratic(val acc: Int, val vel: Int, val coord: Int) {
    fun merge(q: Quadratic) = Quadratic(acc - q.acc, vel - q.vel, coord - q.coord)
    fun solve() = when (acc) {
        0 -> solveNonQuadratic()
        else -> solveQuadratic()
    }

    private fun solveNonQuadratic(): Set<Int>? = when {
        vel == 0 && coord == 0 -> null
        vel == 0 || coord * vel > 0 -> emptySet()
        else -> (-coord).toDouble().div(vel).let { if (it == kotlin.math.round(it)) setOf(it.toInt()) else emptySet() }
    }

    private fun solveQuadratic(): Set<Int> {
        val a = acc.toDouble().div(2)
        val b = a + vel.toDouble()
        val discriminant = b * b - 4 * a * coord
        return when {
            discriminant < 0 -> emptySet() // Only complex number solutions
            else -> {
                val sqrt = sqrt(discriminant)
                setOf((-b + sqrt) / (2 * a), (-b - sqrt) / (2 * a)).filter { it == kotlin.math.round(it) && it.toInt() >= 0 }.map { it.toInt() }.toSet()
            }
        }
    }
}

private data class Particle(val id: Int, val x: Quadratic, val y: Quadratic, val z: Quadratic) {
    fun earliestCollision(p: Particle): Int? {
        val intersect = intersect(intersect(x.merge(p.x).solve(), y.merge(p.y).solve()), z.merge(p.z).solve())
        return if (intersect == null) 0 else intersect.minOrNull()
    }

    private fun intersect(a: Set<Int>?, b: Set<Int>?): Set<Int>? = when {
        a != null && b != null -> a.intersect(b)
        a == null && b == null -> null
        a == null -> b
        else -> a
    }
}
