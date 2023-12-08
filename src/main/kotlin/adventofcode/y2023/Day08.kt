package adventofcode.y2023 // ktlint-disable filename

import adventofcode.lcm
import adventofcode.readFile
import adventofcode.split

fun main() {
    val lines = readFile("src/main/resources/y2023/day08.txt")
    val directions = lines[0].toList()

    val paths = (2 until lines.size).associate { i ->
        split(lines[i]).let {
            it[0] to Pair(it[2].substring(1..3), it[3].substring(0..2))
        }
    }

    println("part1=" + movePath(paths, directions))
    println("part2=" + lcm(paths.keys.filter { it.last() == 'A' }.map { findPathLoop(paths, directions, it) }))
}

private fun movePath(paths: Map<String, Pair<String, String>>, directions: List<Char>): Int {
    var steps = 0
    var curPos = "AAA"
    var curDirection = 0
    while (curPos != "ZZZ") {
        curPos = paths.getValue(curPos).let { if (directions[curDirection] == 'L') it.first else it.second }
        curDirection = (curDirection + 1) % directions.size
        steps++
    }
    return steps
}

private fun findPathLoop(paths: Map<String, Pair<String, String>>, directions: List<Char>, start: String): Long {
    var steps = 0L
    var curPos = start
    var curDirection = 0
    val seenZs = mutableMapOf<String, Long>()
    while (curPos !in seenZs.keys) {
        if (curPos.last() == 'Z') seenZs[curPos] = steps
        curPos = paths.getValue(curPos).let { if (directions[curDirection] == 'L') it.first else it.second }
        curDirection = (curDirection + 1) % directions.size
        steps++
    }
    return seenZs.getValue(curPos)
}
