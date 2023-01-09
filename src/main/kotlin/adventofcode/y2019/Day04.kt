package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchPositiveNumbers
import adventofcode.readFile

fun main(args: Array<String>) {
    val line = matchPositiveNumbers(readFile("src/main/resources/y2019/day04.txt")[0])
    println("part1=" + validPassword(line) { l -> l.any { it > 1 } })
    println("part2=" + validPassword(line) { l -> l.contains(2) })
}

private fun validPassword(line: List<Int>, validGroupFunc: (List<Int>) -> Boolean) =
    (line.first()..line.last()).count { num ->
        val numList = num.toString().toList()
        numList.sorted() == numList && validGroupFunc(numList.groupBy { it }.values.map { it.size })
    }
