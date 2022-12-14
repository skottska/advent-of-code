package adventofcode.y2022 // ktlint-disable filename

import adventofcode.anyRange
import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day14.txt")
    val stones = mutableSetOf<Pair<Int, Int>>()

    lines.forEach { line ->
        split(line).windowed(3, 2).map { window ->
            val s = matchNumbers(window[0]).let { Pair(it.first(), it.last()) }
            val e = matchNumbers(window[2]).let { Pair(it.first(), it.last()) }
            if (s.first == e.first) anyRange(s.second, e.second).forEach { stones.add(Pair(s.first, it)) }
            else anyRange(s.first, e.first).forEach { stones.add(Pair(it, s.second)) }
        }
    }
    println("part1=" + moveSand(stones))

    val lowestStone = stones.maxByOrNull { it.second }?.second ?: 0
    val xRange = stones.sortedBy { it.first }.let { listOf(it.first().first - 200, (it.last().first + 200)) }
    anyRange(xRange).forEach { stones.add(Pair(it, lowestStone + 2)) }
    println("part2=" + moveSand(stones))
}

private fun moveSand(initialStones: Set<Pair<Int, Int>>): Int {
    val stones = initialStones.toMutableSet()
    val startPos = Pair(500, 0)
    val lowestStone = stones.maxByOrNull { it.second }?.second ?: 0
    var numSand = 0
    var sandPos = startPos
    while (true) {
        sandPos = when {
            sandPos.second == lowestStone + 1 -> break
            !stones.contains(Pair(sandPos.first, sandPos.second + 1)) -> Pair(sandPos.first, sandPos.second + 1)
            !stones.contains(Pair(sandPos.first - 1, sandPos.second + 1)) -> Pair(sandPos.first - 1, sandPos.second + 1)
            !stones.contains(Pair(sandPos.first + 1, sandPos.second + 1)) -> Pair(sandPos.first + 1, sandPos.second + 1)
            sandPos == startPos -> { numSand++; break }
            else -> { numSand++; stones.add(sandPos); startPos }
        }
    }
    return numSand
}
