package adventofcode.y2022 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    var lines = readFile("src/main/resources/y2022/day20.txt").mapIndexed { i, it -> Pair(it.toLong(), i) }.toMutableList()
    val numLines = lines.size

    (0 until lines.size).forEach { i ->
        val index = lines.indexOfFirst { it.second == i }
        val value = lines[index]
        val toPos = toPos(value.first, index, numLines)
        lines = insertInto(lines, value, index, toPos)
    }
    println("part1="+ findAnswer(lines))

    lines = readFile("src/main/resources/y2022/day20.txt").mapIndexed { i, it -> Pair(it.toLong() * 811589153L, i) }.toMutableList()
    repeat(10) {
        (0 until lines.size).forEach { i ->
            val index = lines.indexOfFirst { it.second == i }
            val value = lines[index]
            val toPos = toPos(value.first, index, numLines)
            lines = insertInto(lines, value, index, toPos)
        }
    }
    println("part2="+ findAnswer(lines))
}

private fun findAnswer(lines: List<Pair<Long, Int>>): Long {
    val lines2 = (1..(1 +3000/lines.size)).fold(lines.toList()) { total, _ -> total + lines}
    val indexOf0 = lines.indexOfFirst { it.first == 0L }
    return lines2[indexOf0 + 1000].first + lines2[indexOf0 + 2000].first + lines2[indexOf0 + 3000].first
}

private fun <T> insertInto(l: MutableList<T>, x: T, from: Int, to: Int): MutableList<T> {
    if (from == to) return l
    if (from < to) {
        l.removeAt(from)
        return (l.subList(0, to) + listOf(x) + l.subList(to, l.size)).toMutableList()
    }
    return if (to == 0) (l + listOf(x)).toMutableList().also { it.removeAt(from) }
    else (l.subList(0, to) + listOf(x) + l.subList(to, l.size)).toMutableList().also { it.removeAt(from + 1) }
}

// As first and last pos are effectively the same, we need to add or remove 1 as appropriate to account for that
private fun toPos(value: Long, index: Int, numLines: Int): Int {
    val mod = (value % (numLines - 1)).toInt()
    val newPos = mod + index
    return when {
        newPos >= numLines -> newPos - numLines + 1
        newPos >= 1 -> newPos
        else -> numLines + newPos - 1
    }
}
