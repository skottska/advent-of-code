package adventofcode.y2020 // ktlint-disable filename

import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val seats = lines.map { findRow(it) to findCol(it) }
    println("part1=" + seats.map { it.first * 8 + it.second }.max())
    val myRow = seats.groupBy { it.first }.map { seat -> seat.key to seat.value.map { it.second } }.filter { it.second.size != 8 }[1]
    val myCol = (0..7).first { it !in myRow.second }
    println("part2=" + (myRow.first * 8 + myCol))
}

private fun findRow(s: String) = findPos(l = s.filter { it == 'B' || it == 'F' }.map { it == 'F' }, max = 127)
private fun findCol(s: String) = findPos(l = s.filter { it == 'L' || it == 'R' }.map { it == 'L' }, max = 7)

private fun findPos(l: List<Boolean>, min: Int = 0, max: Int): Int {
    return when {
        max == min -> max
        l.first() -> findPos(l.drop(1), min, (1 + max - min) / 2 + min - 1)
        else -> findPos(l.drop(1), (1 + max - min) / 2 + min, max)
    }
}
