package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.readFile
import kotlin.math.min

fun main() {
    val map = readFile("src/main/resources/y2019/day18.txt").mapIndexed { row, line ->
        line.mapIndexed { col, c -> Coord(row, col) to c }
    }.flatten().toMap()

    val start = map.filter { it.value == '@' }.keys.first()
    val numKeys = map.values.count { it.isLowerCase() }
    println("part1=" + iterate(listOf(start to findAvailableDoorsAndKeys(start, map)), map, numKeys))

    seen.clear(); cache.clear(); minMoves = Int.MAX_VALUE
    val part2Changes = mapOf(
        start to '#',
        Coord(start.row - 1, start.col - 1) to '@',
        Coord(start.row - 1, start.col + 1) to '@',
        Coord(start.row + 1, start.col - 1) to '@',
        Coord(start.row + 1, start.col + 1) to '@'
    ) + start.around().map { it to '#' }
    val mapPart2 = map.map {
        it.key to if (part2Changes.containsKey(it.key)) part2Changes.getValue(it.key) else it.value
    }.toMap()

    val startPart2 = mapPart2.filter { it.value == '@' }.map { it.key }.map { c -> c to findAvailableDoorsAndKeys(c, mapPart2) }
    println("part2=" + iterate(startPart2, mapPart2, numKeys))
}

private var minMoves = Int.MAX_VALUE
private val seen = mutableMapOf<Pair<Coord, Set<Char>>, Int>()

private val iterateCache: MutableMap<Pair<List<Coord>, Set<Char>>, Int> = mutableMapOf()
private fun iterate(start: List<Pair<Coord, Set<Char>>>, map: Map<Coord, Char>, numKeys: Int, keys: Set<Char> = emptySet(), moves: Int = 0): Int {
    if (moves >= minMoves) return Int.MAX_VALUE
    val context = start.map { it.first } to keys
    if (iterateCache.getOrDefault(context, Int.MAX_VALUE) <= moves) return Int.MAX_VALUE
    iterateCache[context] = moves

    if (keys.size == numKeys) {
        minMoves = min(minMoves, moves)
        return moves
    }
    return start.minOf { c ->
        val others = start.filter { it != c }
        findReachableKeys(c.first, map, keys.filter { it in c.second }.toSet()).minOfOrNull {
            iterate(others + (it.first to c.second), map, numKeys, keys + map.getValue(it.first), moves + it.second)
        } ?: Int.MAX_VALUE
    }
}

private val cache: MutableMap<Pair<Coord, Set<Char>>, Set<Pair<Coord, Int>>> = mutableMapOf()
private fun findReachableKeys(start: Coord, map: Map<Coord, Char>, keys: Set<Char>): Set<Pair<Coord, Int>> = cache.getOrPut(start to keys) {
    var activeCoords = start.around()
    val result = mutableSetOf<Pair<Coord, Int>>()
    val been = mutableSetOf<Coord>()
    var moves = 1
    while (activeCoords.isNotEmpty()) {
        been.addAll(activeCoords)
        activeCoords = activeCoords.mapNotNull { coord ->
            val cur = map.getOrDefault(coord, '#')
            when {
                cur == '#' -> null
                cur.isUpperCase() && cur.lowercase().first() !in keys -> null
                cur.isLowerCase() && cur !in keys -> {
                    if (coord !in result.map { it.first }) result.add(coord to moves)
                    null
                }
                else -> coord.around().filter { c -> c !in been }
            }
        }.flatten()
        moves++
    }
    result
}

private fun findAvailableDoorsAndKeys(start: Coord, map: Map<Coord, Char>): Set<Char> {
    var activeCoords = start.around()
    val result = mutableSetOf<Char>()
    val been = mutableSetOf<Coord>()
    while (activeCoords.isNotEmpty()) {
        been.addAll(activeCoords)
        activeCoords = activeCoords.mapNotNull { coord ->
            when (val cur = map.getOrDefault(coord, '#')) {
                '#' -> null
                else -> {
                    result.add(cur.lowercaseChar())
                    coord.around().filter { c -> c !in been }
                }
            }
        }.flatten()
    }
    return result
}
