package adventofcode.y2023 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.mapCoord
import adventofcode.readFile

fun main() {
    val grid = readFile("src/main/resources/y2023/day21.txt").mapCoord()

    val start = grid.filter { it.value == 'S' }.keys.first()

    val odd = mutableSetOf<Coord>()
    val even = mutableSetOf<Coord>()

    (0..64).fold(setOf(start)) { cur, i ->
        if (i % 2 == 0) even += cur
        else odd += cur
        cur.flatMap { it.around() }.filter { it !in even && it !in odd && grid.getOrDefault(it, '#') == '.' }.toSet()
    }
    println("part1=" + even.size)
}
