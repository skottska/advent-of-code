package adventofcode.y2020 // ktlint-disable filename

import adventofcode.matchPositiveNumbers
import adventofcode.readFile
import adventofcode.split

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2020/day02.txt")
    println("part1="+lines.count { line ->
        val split = split(line)
        val limits = matchPositiveNumbers(split[0])
        val num = split[2].count { it == split[1][0] }
        num >= limits.first() && num <= limits.last()
    })
    println("part2="+lines.count { line ->
        val split = split(line)
        val limits = matchPositiveNumbers(split[0])
        limits.count { split[2][it - 1] == split[1][0] } == 1
    })
}
