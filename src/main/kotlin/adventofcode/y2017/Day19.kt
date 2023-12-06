package adventofcode.y2017 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.mapCoord
import adventofcode.readFile
import java.util.*

fun main() {
    val grid = readFile("src/main/resources/y2017/day19.txt").mapCoord()
    var curDirection = Direction.DOWN
    var curPos = grid.filter { it.key.row == 0 && it.value == '|' }.keys.first()
    var curChar = grid.getValue(curPos)
    var found = ""
    var steps = 0
    while (curChar != ' ') {
        when {
            curChar.isLetter() -> found += curChar
            curChar == '+' -> {
                curDirection = Direction.values().filter { it.cardinality != curDirection.cardinality }.first { grid.getOrDefault(it.move(curPos), ' ') != ' ' }
            }
            else -> Unit
        }
        curPos = curDirection.move(curPos)
        curChar = grid.getOrDefault(curPos, ' '); steps++
    }
    println("part1=$found")
    println("part2=$steps")
}

private enum class Cardinality { VERTICAL, HORIZONTAL }
private enum class Direction(val move: (Coord) -> Coord, val cardinality: Cardinality) {
    DOWN(Coord::down, Cardinality.VERTICAL), UP(Coord::up, Cardinality.VERTICAL), LEFT(Coord::left, Cardinality.HORIZONTAL), RIGHT(Coord::right, Cardinality.HORIZONTAL)
}
