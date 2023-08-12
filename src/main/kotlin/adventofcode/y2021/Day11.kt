package adventofcode.y2021 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.readFile

fun main() {
    val map = readFile("src/main/resources/y2021/day11.txt").mapIndexed { row, i ->
        i.mapIndexed { col, c -> Coord(row, col) to c.digitToInt() }
    }.flatten().toMap()

    val part1Map = map.toMutableMap()
    println("part1=" + (1..100).fold(0) { total, _ -> total + iterate(part1Map) })
    val part2Map = map.toMutableMap()
    var flashed = 0
    var result = 0
    while (flashed != part2Map.size) {
        result++
        flashed = iterate(part2Map)
    }
    println("part2=$result")
}

private fun iterate(map: MutableMap<Coord, Int>): Int {
    val flashed = mutableListOf<Coord>()
    map.keys.forEach { map[it] = map.getValue(it) + 1 }
    var flashes = map.filter { it.value >= 10 && it.key !in flashed }
    while (flashes.isNotEmpty()) {
        flashed += flashes.keys
        flashes.keys.forEach { c -> c.aroundDiag().forEach { diag -> map[diag]?.let { map[diag] = it + 1 } } }
        flashes = map.filter { it.value >= 10 && it.key !in flashed }
    }
    flashed.forEach { map[it] = 0 }
    return flashed.size
}
