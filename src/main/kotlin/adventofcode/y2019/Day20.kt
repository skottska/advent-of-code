package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.asString
import adventofcode.readFile

fun main() {
    val map = readFile("src/main/resources/y2019/day20.txt").mapIndexed { row, line ->
        line.mapIndexed { col, c -> Coord(row, col) to if (c == '#') ' ' else c }
    }.flatten().toMap().toMutableMap()

    /*var deadEnds = map.filter { c -> c.value == '.' && c.key.around().count { map.getOrDefault(it, ' ') == ' ' } >= 3 }
    while (deadEnds.isNotEmpty()) {
        deadEnds.forEach { map[it.key] = ' ' }
        deadEnds = map.filter { c -> c.value == '.' && c.key.around().count { map.getOrDefault(it, ' ') == ' ' } >= 3 }
    }*/

    val portals = findPortal(map, Coord::left, Coord::right) + findPortal(map, Coord::up, Coord::down)

    val distances = portals.associate { portal ->
        val seen = mutableListOf(portal.first)
        var around = portal.first.around().filter { map.getValue(it) == '.' }
        var steps = 1
        val foundPortals = mutableListOf<Pair<Portal, Int>>()
        while (around.isNotEmpty()) {
            seen += around
            foundPortals += around.filter { map.getOrDefault(it, ' ').isUpperCase() }
                .map {
                    val name =
                        listOf(it.left(), it.up(), it, it.right(), it.down()).map { c -> map.getOrDefault(c, ' ') }
                            .filter { c -> c.isUpperCase() }.asString()
                    Portal(name, isOutside(it, map)) to steps - 1
                }

            around = around.filter { map.getOrDefault(it, ' ') == '.' }.flatMap { it.around() }.filter { it !in seen }
            steps++
        }
        portal.second to foundPortals
    }
    println("part1=" + (iteratePart1(distances) - 1))
    println("part2=" + (iteratePart2(distances) - 1))
}

private fun iteratePart2(distances: Map<Portal, List<Pair<Portal, Int>>>, path: List<Portal> = listOf(Portal("AA", isOutside = false)), distance: Int = 0, depth: Int = 0): Int {
    if (path.last().name == "ZZ" && depth != -1) return Int.MAX_VALUE
    if (path.last().name == "ZZ") return distance
    if (depth < -1 || depth > 30 || distance > 10000) return Int.MAX_VALUE

    return distances.getValue(path.last().copy(isOutside = !path.last().isOutside)).filter { it.first.name != "AA" }.minOfOrNull {
        iteratePart2(distances, path + it.first, distance + it.second + 1, depth + if (it.first.isOutside) -1 else 1)
    } ?: Int.MAX_VALUE
}

private fun iteratePart1(distances: Map<Portal, List<Pair<Portal, Int>>>, path: List<Portal> = listOf(Portal("AA", isOutside = false)), distance: Int = 0): Int {
    if (path.last().name == "ZZ") return distance
    return distances.getValue(path.last().copy(isOutside = !path.last().isOutside)).filter { it.first !in path && it.first.name != "AA" }.minOfOrNull {
        iteratePart1(distances, path + it.first, distance + it.second + 1)
    } ?: Int.MAX_VALUE
}

private fun isOutside(c: Coord, map: Map<Coord, Char>) = c.col !in 3..map.maxOf { it.key.col } - 3 || c.row !in 3..map.maxOf { it.key.row } - 3

private fun findPortal(map: Map<Coord, Char>, dir1: (Coord) -> Coord, dir2: (Coord) -> Coord): List<Pair<Coord, Portal>> = map
    .filter { c -> c.value == '.' }
    .mapNotNull { c ->
        val name = listOf(dir1(dir1(c.key)), dir1(c.key), dir2(c.key), dir2(dir2(c.key))).map { map.getOrDefault(it, ' ') }.filter { it.isUpperCase() }.asString()
        if (name.length != 2) null
        else c.key to Portal(name, isOutside(c.key, map))
    }

private data class Portal(val name: String, val isOutside: Boolean)
