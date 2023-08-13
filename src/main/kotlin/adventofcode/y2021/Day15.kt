package adventofcode.y2021 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.readFile

fun main() {
    val map = readFile("src/main/resources/y2021/day15.txt").mapIndexed { row, i ->
        i.mapIndexed { col, c -> Coord(row, col) to c.digitToInt() }
    }.flatten().toMap()

    iterate(map)
    println("part1=" + best.getValue(end(map)))

    val multiply = 5
    val maxRow = map.keys.maxOf { it.row } + 1
    val maxCol = map.keys.maxOf { it.col } + 1
    val mapPart2 = map.flatMap { c ->
        (0 until multiply).map { c.key.copy(row = c.key.row + maxRow * it) to (c.value + it).let { v -> if (v > 9) v - 9 else v } }
    }.flatMap { c ->
        (0 until multiply).map { c.first.copy(col = c.first.col + maxCol * it) to (c.second + it).let { v -> if (v > 9) v - 9 else v } }
    }.toMap()
    totalBest = Int.MAX_VALUE
    best.clear()
    iterate(mapPart2)
    println("part2=" + best.getValue(end(mapPart2)))
}

private var totalBest = Int.MAX_VALUE
private val best = mutableMapOf<Coord, Int>()
private val comparator: Comparator<Coord> = Comparator { o1, o2 ->
    when {
        o1 == null && o2 == null -> 0
        o1 == null -> -1
        o2 == null -> 1
        o1.row == o2.row -> o2.col.compareTo(o1.col)
        else -> o2.row.compareTo(o1.row)
    }
}

private fun end(map: Map<Coord, Int>) = Coord(map.keys.maxOf { it.row }, map.keys.maxOf { it.col })
private fun iterate(map: Map<Coord, Int>) = iterateInternal(Coord(0, 0), 0, end(map), map)
private fun iterateInternal(cur: Coord, pathLength: Int, end: Coord, map: Map<Coord, Int>) {
    if (best.getOrDefault(cur, Int.MAX_VALUE) <= pathLength) return
    best[cur] = pathLength
    if (totalBest <= pathLength + end.row - cur.row + end.col - cur.col) return
    if (cur == end) {
        totalBest = pathLength
        println("New best=$pathLength")
        return
    }
    cur.around().filter { it in map.keys }.sortedWith(comparator).forEach { iterateInternal(it, pathLength + map.getValue(it), end, map) }
}
