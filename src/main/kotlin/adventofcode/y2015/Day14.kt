package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2015/day14.txt")
    println("part1=" + travel(lines, 2503).max())

    val totalResults = MutableList(lines.size) { 0 }
    (1..2503).forEach {
        val results = travel(lines, it)
        results.forEachIndexed { index, i -> if (i == results.max()) totalResults[index] += 1 }
    }
    println("part2=" + totalResults.max())
}

fun travel(lines: List<String>, time: Int) = lines.map { line ->
    val (speed, speedSecs, restSecs) = matches(line, "[0-9]+").map { it.toInt() }
    speed * timeTravelling(time, speedSecs, restSecs)
}

fun timeTravelling(time: Int, speedSecs: Int, restSecs: Int): Int {
    val iterations = time / (speedSecs + restSecs)
    val timeLeft = time - (iterations * (speedSecs + restSecs))
    return iterations * speedSecs + minOf(timeLeft, speedSecs)
}
