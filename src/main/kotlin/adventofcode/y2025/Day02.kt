package adventofcode.y2025 // ktlint-disable filename

import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).first().split(",")
    val ranges = lines.map { it.split('-') }.map { it.first().toLong()..it.last().toLong() }

    val part1 = ranges.flatMap { r ->
        r.filter {
            val s = it.toString()
            s.length % 2 == 0 && s.take(s.length / 2) == s.takeLast(s.length / 2)
        }
    }.sum()
    println("part1=$part1")

    val part2 = ranges.flatMap { r ->
        r.filter { match ->
            val s = match.toString()
            (1..s.length / 2).any { subLength ->
                val sub = s.substring(0, subLength)
                s.length % subLength == 0 && s.chunked(subLength).all { it == sub }
            }
        }
    }.sum()
    println("part2=$part2")
}
