package adventofcode.y2020 // ktlint-disable filename

import adventofcode.readFile
import kotlin.math.min

fun main() {
    val directions = readFile("src/main/resources/y2020/day24.txt").map { parseDirection(it) }

    val groupedDirections = directions.map { d -> d.groupBy { it }.mapValues { it.value.size } }.map { reduce(it) }
    val part1 = groupedDirections.filter { g -> groupedDirections.count { it == g } % 2 != 0 }.toSet()
    println("part1=${part1.count()}")

    val result = (1..100).fold(part1) { blackTiles, i ->
        val stayBlack = blackTiles.filter { blackTile ->
            around(blackTile).count { it in blackTiles } in listOf(1, 2)
        }
        val aroundWhites = blackTiles.flatMap { around(it) }.filter { it !in blackTiles }.toSet()
        val changeBlack = aroundWhites.filter { whiteTile ->
            around(whiteTile).count { it in blackTiles } == 2
        }
        (stayBlack + changeBlack).toSet()
    }
    println("part2=" + result.size)
}

private val aroundCache: MutableMap<Map<Direction, Int>, List<Map<Direction, Int>>> = mutableMapOf()
private fun around(directions: Map<Direction, Int>): List<Map<Direction, Int>> = aroundCache.getOrPut(directions) {
    Direction.values().map {
        val mutableMap = directions.toMutableMap()
        val add = mutableMap.getOrDefault(it, 0) + 1
        mutableMap[it] = add
        reduce(mutableMap)
    }
}

private val reduceCache: MutableMap<Map<Direction, Int>, Map<Direction, Int>> = mutableMapOf()
private fun reduce(directionsIn: Map<Direction, Int>): Map<Direction, Int> = reduceCache.getOrPut(directionsIn) {
    val directions = directionsIn.toMutableMap()
    reduceOpposites(directions, Direction.E, Direction.W)
    reduceOpposites(directions, Direction.SE, Direction.NW)
    reduceOpposites(directions, Direction.NE, Direction.SW)
    shorten(directions, Direction.NE, Direction.SE, Direction.E)
    shorten(directions, Direction.NW, Direction.SW, Direction.W)
    shorten(directions, Direction.NW, Direction.E, Direction.NE)
    shorten(directions, Direction.NE, Direction.W, Direction.NW)
    shorten(directions, Direction.SW, Direction.E, Direction.SE)
    shorten(directions, Direction.SE, Direction.W, Direction.SW)
    if (directionsIn == directions.toMap()) directions else reduce(directions)
}

private fun shorten(directions: MutableMap<Direction, Int>, a: Direction, b: Direction, c: Direction) {
    val numA = directions.getOrDefault(a, 0)
    val numB = directions.getOrDefault(b, 0)
    val numC = directions.getOrDefault(c, 0)
    val num = min(numA, numB)
    directions[a] = numA - num
    directions[b] = numB - num
    directions[c] = numC + num
}

private fun reduceOpposites(directions: MutableMap<Direction, Int>, a: Direction, b: Direction) {
    val numA = directions.getOrDefault(a, 0)
    val numB = directions.getOrDefault(b, 0)
    val num = min(numA, numB)
    directions[a] = numA - num
    directions[b] = numB - num
}

private fun parseDirection(s: String): List<Direction> = when {
    s.isEmpty() -> emptyList()
    s.first() == 'e' -> listOf(Direction.E) + parseDirection(s.drop(1))
    s.first() == 'w' -> listOf(Direction.W) + parseDirection(s.drop(1))
    s.substring(0, 2) == "se" -> listOf(Direction.SE) + parseDirection(s.drop(2))
    s.substring(0, 2) == "sw" -> listOf(Direction.SW) + parseDirection(s.drop(2))
    s.substring(0, 2) == "ne" -> listOf(Direction.NE) + parseDirection(s.drop(2))
    s.substring(0, 2) == "nw" -> listOf(Direction.NW) + parseDirection(s.drop(2))
    else -> throw IllegalArgumentException("What is this=$s")
}
private enum class Direction { E, SE, NE, W, SW, NW }
