package adventofcode.y2024 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.transpose
import java.lang.invoke.MethodHandles

fun main() {
    val keys = mutableListOf<List<Int>>()
    val locks = mutableListOf<List<Int>>()
    readFile(MethodHandles.lookup()).windowed(size = 8, step = 8, partialWindows = true).forEach { window ->
        val result = transpose(window.dropLast(1).map { it.toList() }).map { line -> line.count { it == '#' } }
        when (window.first().first()) {
            '#' -> locks += result
            else -> keys += result
        }
    }
    val part1 = keys.sumOf { key ->
        locks.count { lock ->
            (0..4).all { key[it] + lock[it] <= 7 }
        }
    }
    println("part1=$part1")
}

