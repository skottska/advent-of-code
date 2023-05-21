package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbersToBigInt
import adventofcode.readFile
import java.math.BigInteger

fun main() {
    val line = matchNumbersToBigInt(readFile("src/main/resources/y2019/day19.txt")[0])
    val map = (0..49).map { row ->
        (0..49).map { col ->
            runIntCodeProgram(line.toMutableList(), listOf(row, col).map { it.toBigInteger() }, ProgramContext()).output?.toInt() ?: 0
        }
    }
    println("part1=" + map.flatten().count { it == 1 })
    println("part2=" + part2(line).let { it.row * 10_000 + it.col })
}

private fun part2(program: List<BigInteger>): Coord {
    val cols = mutableMapOf<Int, Int>()
    var row = 5
    var startCol = 7
    while (true) {
        val readRow = readRow(row, startCol, program)
        readRow.forEach { cols[it.col] = cols.getOrDefault(it.col, 0) + 1 }
        if (readRow.size >= 100) {
            val first = readRow.firstOrNull { cols.getOrDefault(it.col, 0) >= 100 }
            if (first != null) {
                val last = readRow.last { cols.getOrDefault(it.col, 0) >= 100 }
                if (last.col - first.col + 1 >= 100) return first.copy(row = first.row - 99)
            }
        }
        startCol = readRow.minOf { it.col }; row++
    }
}

private fun readRow(row: Int, startCol: Int, program: List<BigInteger>): List<Coord> {
    val result = mutableListOf<Coord>()
    var col = startCol
    var lastRead = 1
    while (lastRead == 1 || result.isEmpty()) {
        val coord = Coord(row, col++)
        lastRead = runIntCodeProgram(program.toMutableList(), listOf(coord.row, coord.col).map { it.toBigInteger() }, ProgramContext()).output?.toInt() ?: 0
        if (lastRead == 1) result.add(coord)
    }
    return result
}
