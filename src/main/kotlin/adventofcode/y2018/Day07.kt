package adventofcode.y2018 // ktlint-disable filename

import adventofcode.asString
import adventofcode.readFile
import adventofcode.split

fun main() {
    val steps = readFile("src/main/resources/y2018/day07.txt").map { line -> split(line).let { it[1].first() to it[7].first() } }
    println("part1=" + sort(steps))
    println("part2=" + work(steps, numWorkers = 5, delay = 60))
}

private fun work(steps: List<Pair<Char, Char>>, numWorkers: Int, delay: Int): Int {
    val size = steps.map { setOf(it.first, it.second) }.flatten().toSet().size
    var time = 0
    var result = ""
    var workers = (1..numWorkers).map { ' ' to 0 }
    while (result.length != size) {
        val minTime = workers.filter { it.second > 0 }.minOfOrNull { it.second } ?: 0
        workers = workers.map { it.first to it.second - minTime }
        time += minTime
        val finishedWorkers = workers.filter { it.second <= 0 }
        val activeWorkers = workers - finishedWorkers.toSet()
        result += finishedWorkers.map { it.first }.filter { it != ' ' }.asString()
        val available = available(result, steps) - activeWorkers.map { it.first }.toSet()
        workers = workers.filter { it.second > 0 } + finishedWorkers.mapIndexed { index, _ ->
            if (index >= available.size) ' ' to 0
            else available[index].let { it to it.code - 'A'.code + 1 + delay }
        }
    }
    return time
}

private fun sort(steps: List<Pair<Char, Char>>): String {
    var result = ""
    var available = available(result, steps)
    while (available.isNotEmpty()) {
        result += available.min()
        available = available(result, steps)
    }
    return result
}

private fun available(result: String, steps: List<Pair<Char, Char>>) = steps
    .asSequence()
    .map { setOf(it.first, it.second) }.flatten()
    .filter { step -> step !in result && steps.filter { step == it.second }.all { a -> a.first in result } }
    .toSet().toList()
