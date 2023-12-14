package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import kotlin.math.max

fun main() {
    val lines = readFile("src/main/resources/y2017/day24.txt").map { line -> matchNumbers(line).let { it[0] to it[1] } }

    println("part1=" + iteratePart1(0, lines))
    iteratePart2(0, emptyList(), lines)
    println("part2=$maxStrength")
}

private fun iteratePart1(curEnd: Int, components: List<Pair<Int, Int>>): Int {
    val front = components.filter { it.first == curEnd }
    val end = components.filter { it !in front && it.second == curEnd }
    return when {
        front.isEmpty() && end.isEmpty() -> 0
        else -> {
            val maxFront = front.maxOfOrNull { f -> iteratePart1(f.second, components.filter { it != f }) + f.first + f.second } ?: 0
            val maxEnd = end.maxOfOrNull { e -> iteratePart1(e.first, components.filter { it != e }) + e.first + e.second } ?: 0
            maxOf(maxFront, maxEnd)
        }
    }
}

private var maxLength = 0
private var maxStrength = 0

private fun iteratePart2(curEnd: Int, bridge: List<Pair<Int, Int>>, components: List<Pair<Int, Int>>) {
    val front = components.filter { it.first == curEnd }
    val end = components.filter { it !in front && it.second == curEnd }
    when {
        front.isEmpty() && end.isEmpty() -> {
            if (maxLength <= bridge.size) {
                maxLength = bridge.size
                maxStrength = max(maxStrength, bridge.sumOf { it.first + it.second })
            }
        }
        else -> {
            front.forEach { f -> iteratePart2(f.second, bridge + f, components.filter { it != f }) }
            end.forEach { f -> iteratePart2(f.first, bridge + f, components.filter { it != f }) }
        }
    }
}
