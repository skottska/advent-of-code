package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import kotlin.math.min

fun main() {
    val line = readFile("src/main/resources/y2017/day10.txt")[0]
    println("part1=" + round(input = matchNumbers(line), numRounds = 1).let { it[0] * it[1] })
    println("part2=" + Year2017.knotHash(line))
}

private fun round(input: List<Int>, numRounds: Int): List<Int> {
    var index = 0
    var skip = 0
    val list = (0..255).toMutableList()
    (1..numRounds).forEach { _ ->
        input.forEach { i ->
            val preSub = list.subList(index, min(list.size, index + i))
            val postSub = if (index + i < list.size) emptyList() else list.subList(0, (index + i) % list.size)
            (preSub + postSub).reversed().forEachIndexed { subIndex, subI -> list[(index + subIndex) % list.size] = subI }
            index = (index + i + skip++) % list.size
        }
    }
    return list
}
