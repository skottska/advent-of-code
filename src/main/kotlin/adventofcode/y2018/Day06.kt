package adventofcode.y2018 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val coords = readFile("src/main/resources/y2018/day06.txt").map { matchNumbers(it) }.map { Coord(it.first(), it.last()) }.toSet()
    val topLeft = Coord(coords.minOf { it.row }, coords.minOf { it.col })
    val bottomRight = Coord(coords.maxOf { it.row }, coords.maxOf { it.col })
    val outerRing = (
        (topLeft.col..bottomRight.col).map { listOf(Coord(topLeft.row, it), Coord(bottomRight.row, it)) } +
            (topLeft.row..bottomRight.row).map { listOf(Coord(it, topLeft.col), Coord(it, bottomRight.col)) }
        )
        .flatten()

    val outerCoords = outerRing.mapNotNull { nearest(it, coords) }.toSet()
    val inside = ((topLeft.row + 1) until bottomRight.row).map { row ->
        ((topLeft.col + 1) until bottomRight.col).mapNotNull { col ->
            nearest(Coord(row, col), coords)
        }
    }.flatten()
    println("part1=" + (coords - outerCoords).maxOf { inner -> inside.count { it == inner } })

    val part2 = ((topLeft.row + 1) until bottomRight.row).sumOf { row ->
        ((topLeft.col + 1) until bottomRight.col).count { col ->
            coords.sumOf { it.distance(Coord(row, col)) } < 10000
        }
    }
    println("part2=$part2")
}

private fun nearest(a: Coord, s: Set<Coord>) = s.groupBy { it.distance(a) }.let { it.getValue(it.keys.min()) }.let { if (it.size == 1) it.first() else null }
