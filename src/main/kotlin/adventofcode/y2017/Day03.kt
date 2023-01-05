package adventofcode.y2017 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbers
import adventofcode.readFile
import kotlin.math.abs

fun main(args: Array<String>) {
    val line = readFile("src/main/resources/y2017/day03.txt")[0].let { matchNumbers(it).first() }
    var i = 1
    while (i * i < line) i += 2
    val half = (i - 1) / 2
    val middles = (i * i - half).let { (0..3).map { num -> it - (i - 1) * num } }
    val dist = middles.minOf { abs(line - it) }
    println("part1=" + (dist + half))

    val map = mutableMapOf(Coord(0, 0) to 1, Coord(0, 1) to 1)
    var cur = Coord(0, 1)
    val moveFunc = listOf(
        { c: Coord -> c.copy(col = c.col - 1) },
        { c: Coord -> c.copy(row = c.row - 1) },
        { c: Coord -> c.copy(col = c.col + 1) },
        { c: Coord -> c.copy(row = c.row + 1) }
    )
    var funcIndex = 0

    while (true) {
        val first = moveFunc[funcIndex](cur)
        if (map.contains(first)) {
            cur = moveFunc[(funcIndex + 1) % 4](cur)
        } else {
            cur = first
            funcIndex = if (funcIndex == 0) 3 else funcIndex - 1
        }
        val around = listOf(-1, 0, 1)
        val total = around.sumOf { row -> around.sumOf { col -> map.getOrDefault(cur.copy(row = cur.row + row, col = cur.col + col), 0)} }
        if (total > line) { println("part2=$total"); return }
        map[cur] = total
    }
}
