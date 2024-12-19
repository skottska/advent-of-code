package adventofcode.y2024 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val avail = matches(readFile(MethodHandles.lookup())[0], "[a-z]+")
    val toMake = readFile(MethodHandles.lookup()).drop(2)

    println("part1="+toMake.count { makeTowel(avail, it) != 0L })
    println("part2="+toMake.sumOf { makeTowel(avail, it) })
}

private val cache: MutableMap<String, Long> = mutableMapOf()
private fun makeTowel(avail: List<String>, toMake: String): Long {
    if (toMake.isEmpty()) return 1L
    return cache.getOrPut(toMake) {
        avail.filter { toMake.startsWith(it) }.sumOf { makeTowel(avail, toMake.drop(it.length)) }
    }
}
