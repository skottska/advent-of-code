package adventofcode.y2018 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbers
import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2018/day03.txt").map { matchNumbers(it) }
    val claims = lines.map {
        (0 until it[3]).flatMap { row -> (0 until it[4]).map { col -> Coord(it[1] + row, it[2] + col) } }
    }
    println("part1="+claims.flatten().groupBy { it }.count { it.value.size > 1 })
    val unique = claims.flatten().groupBy { it }.filter { it.value.size == 1 }.keys
    val answerCoord = claims.first { unique.containsAll(it) }.first()
    println("part2="+ lines.first { Coord(it[1], it[2]) == answerCoord }.first())
}
