package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.asString
import adventofcode.printCoords
import adventofcode.readFile

fun main() {
    val map = readFile("src/main/resources/y2019/day20.txt").mapIndexed { row, line ->
        line.mapIndexed { col, c -> Coord(row, col) to if (c == '#') ' ' else c }
    }.flatten().toMap().toMutableMap()

    var deadEnds = map.filter { c -> c.value == '.' && c.key.around().count { map.getOrDefault(it, ' ') == ' ' } >= 3 }
    while (deadEnds.isNotEmpty()) {
        deadEnds.forEach { map[it.key] = ' ' }
        deadEnds = map.filter { c -> c.value == '.' && c.key.around().count { map.getOrDefault(it, ' ') == ' ' } >= 3 }
    }

    val portals = findPortal(map, Coord::left, Coord::right) + findPortal(map, Coord::up, Coord::down)
    printCoords(map.keys) { map.getOrDefault(it, " ").toString() }

    val distancesUngrouped = portals.map { portal ->
        val seen = mutableListOf(portal.coord)
        var around = portal.coord.around().filter { map.getValue(it) == '.' }
        var steps = 1
        val foundPortals = mutableListOf<Pair<String, Int>>()
        while (around.isNotEmpty()) {
            seen += around
            foundPortals += around.filter { map.getOrDefault(it, ' ').isUpperCase() }
                .map {
                    val name = listOf(it.left(), it.up(), it, it.right(), it.down()).map { c -> map.getOrDefault(c, ' ') }.filter { c -> c.isUpperCase() }.asString()
                    name to steps - 1
                }

            around = around.filter { map.getOrDefault(it, ' ') == '.' }.flatMap { it.around() }.filter { it !in seen }
            steps++
        }
        portal.name to foundPortals
    }
    val distances = distancesUngrouped.map { it.first }.toSet().associateWith { d -> distancesUngrouped.filter { it.first == d }.flatMap { it.second } }
    println("part1=" + (iterateOld(distances) - 1))
}

private fun iterate(distances: Map<String, List<Pair<Portal, Int>>>, path: List<String> = listOf("AA"), distance: Int = 0): Int {
    if (path.last() == "ZZ") return distance
    return distances.getValue(path.last()).filter { it.first.name != path.last() }.minOfOrNull {
        iterate(distances, path + it.first.name, distance + it.second + 1)
    } ?: Int.MAX_VALUE
}

private fun iterateOld(distances: Map<String, List<Pair<String, Int>>>, path: List<String> = listOf("AA"), distance: Int = 0): Int {
    if (path.last() == "ZZ") return distance
    return distances.getValue(path.last()).filter { it.first !in path }.minOfOrNull {
        iterateOld(distances, path + it.first, distance + it.second + 1)
    } ?: Int.MAX_VALUE
}

private fun findPortal(map: Map<Coord, Char>, dir1: (Coord) -> Coord, dir2: (Coord) -> Coord): List<Portal> = map
    .filter { c -> c.value == '.' }
    .mapNotNull { c ->
        val outsideName = listOf(dir1(dir1(c.key)), dir1(c.key)).map { map.getOrDefault(it, ' ') }.filter { it.isUpperCase() }.asString()
        val insideName = listOf(dir2(c.key), dir2(dir2(c.key))).map { map.getOrDefault(it, ' ') }.filter { it.isUpperCase() }.asString()
        when {
            outsideName.length == 2 -> Portal(c.key, outsideName, isInside = false)
            insideName.length == 2 -> Portal(c.key, insideName, isInside = true)
            else -> null
        }
    }

private data class Portal(val coord: Coord, val name: String, val isInside: Boolean)
