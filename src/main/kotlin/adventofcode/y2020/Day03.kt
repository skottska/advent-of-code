package adventofcode.y2020 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    println("part1=" + slope(right = 3, down = 1))
    println("part2=" + listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2).map { slope(it.first, it.second) }.fold(1L) { total, i -> total * i })
}

private fun slope(right: Int, down: Int): Long {
    val lines = readFile(MethodHandles.lookup()).mapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, col ->
            Coord(rowIndex, colIndex) to (col == '#')
        }
    }
    val puzzleMap = lines.flatten().toMap()
    var cur = Coord(0, 0)
    var trees = 0L
    while (cur.row < lines.size) {
        if (puzzleMap.getValue(cur)) trees++
        cur = Coord(cur.row + down, (cur.col + right) % lines.first().size)
    }
    return trees
}
