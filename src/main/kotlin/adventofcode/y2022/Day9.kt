package adventofcode.y2022

import adventofcode.readFile
import adventofcode.split
import kotlin.math.abs

fun main(args: Array<String>) {
    println("part1=" + move(1))
    println("part2=" + move(9))
}

fun move(numTails: Int): Int {
    val lines = readFile("src/main/resources/y2022/day9.txt")
    var posH = Pair(0, 0)
    val tailPoss = mutableListOf<Pair<Int, Int>>()
    repeat(numTails) { tailPoss.add(Pair(0, 0)) }
    val posVisited = mutableSetOf(Pair(0, 0))
    lines.forEach { line ->
        val split = split(line)
        repeat(split[1].toInt()) {
            posH = when (split[0]) {
                "L" -> posH.copy(first = posH.first - 1)
                "R" -> posH.copy(first = posH.first + 1)
                "U" -> posH.copy(second = posH.second + 1)
                else -> posH.copy(second = posH.second - 1)
            }
            var follow = posH
            tailPoss.forEachIndexed { index, posT ->
                val xDiff = follow.first - posT.first
                val yDiff = follow.second - posT.second
                tailPoss[index] = when {
                    abs(xDiff) <= 1 && abs(yDiff) <= 1 -> posT
                    else -> Pair(posT.first + limit(xDiff), posT.second + limit(yDiff))
                }
                follow = tailPoss[index]
            }
            posVisited.add(tailPoss.last())
        }
    }
    return posVisited.size
}

fun limit(i: Int) = when {
    i >= 2 -> 1
    i <= -2 -> -1
    else -> i
}
