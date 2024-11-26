package adventofcode.y2023 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.Facing
import adventofcode.lagoonSize
import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { split(it) }
    println("part1=" + lagoonSize(generateCorners(lines.map { it.first().first() to matchNumbers(it[1]).first() })))

    val corners = generateCorners(
        lines.map { line ->
            val hex = line.last().substring(2..6).toInt(16)
            val dir = when (line.last()[7]) {
                '0' -> 'R'
                '1' -> 'D'
                '2' -> 'L'
                '3' -> 'U'
                else -> throw IllegalArgumentException("Unknown direction " + line.last()[7])
            }
            dir to hex
        }
    )
    println("part2=" + lagoonSize(corners))
}

private fun generateCorners(list: List<Pair<Char, Int>>): List<Coord> = list.fold(listOf(Coord(0, 0))) { total, line ->
    val num = line.second
    total + when (line.first) {
        'R' -> Facing.RIGHT
        'D' -> Facing.DOWN
        'L' -> Facing.LEFT
        'U' -> Facing.UP
        else -> throw IllegalArgumentException("Don't know what to do with ${line.first}")
    }.move(total.last(), num)
}
