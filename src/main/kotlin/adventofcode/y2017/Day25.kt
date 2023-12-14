package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2017/day25.txt")

    val blueprints = (0 until (lines.size - 2) / 10).map { i ->
        val state = lines[i * 10 + 3].let { it[it.length - 2] }
        val a = parseIfClause(lines, i * 10 + 5)
        val b = parseIfClause(lines, i * 10 + 9)
        state to Pair(a, b)
    }.toMap()

    val checksum = matchNumbers(lines[1]).first()
    var state = 'A'
    val tape = MutableList(checksum) { 0 }
    var pos = tape.size / 2

    (1..checksum).forEach { r ->
        val blueprint = blueprints.getValue(state)
        val clause = if (tape[pos] == 0) blueprint.first else blueprint.second
        tape[pos] = clause.value
        pos += clause.move
        state = clause.nextState
    }

    println("part1=" + tape.count { it == 1 })
}

private fun parseIfClause(lines: List<String>, index: Int) = IfClause(
    value = lines[index].let { it[it.length - 2].digitToInt() },
    move = if (lines[index + 1].contains("right")) 1 else -1,
    nextState = lines[index + 2].let { it[it.length - 2] },
)
private data class IfClause(val value: Int, val move: Int, val nextState: Char)
