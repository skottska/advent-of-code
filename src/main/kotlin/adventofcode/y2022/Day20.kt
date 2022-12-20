package adventofcode.y2022 // ktlint-disable filename

import adventofcode.readFile
import java.util.*

fun main(args: Array<String>) {
    var lines = readFile("src/main/resources/y2022/day20.txt").map { Pair(it.toInt(), false) }.toMutableList()
    val numLines = lines.size
    println("0,5 "+lines.subList(0,5))
    println("5,7 "+lines.subList(5,7))

    while (lines.any { !it.second }) {
        val index = lines.indexOfFirst { !it.second }
        val value = lines[index]
        val toPos = toPos(value.first, index, numLines)
        lines = insertInto(lines, Pair(value.first, true), index, toPos)
        println(lines.map { it.first })
    }
    println(lines.map { it.first })
    val func = { value: Int -> toPos(value, lines.indexOfFirst { it.first == 0 }, numLines).let { lines[it].first }}
    println(""+func(1000) + " "+func(2000) + " "+func(3000))
    println(func(1000) + func(2000) + func(3000))

    val lines2 = (1..(1 +3000/numLines)).fold(lines.toList()) { total, _ -> total + lines}
    val indexOf0 = lines.indexOfFirst { it.first == 0 }
    println(""+lines2[indexOf0 + 1000].first + " "+lines2[indexOf0 + 2000].first + " "+lines2[indexOf0 + 3000].first)
    println(lines2[indexOf0 + 1000].first + lines2[indexOf0 + 2000].first + lines2[indexOf0 + 3000].first)
}

private fun <T> insertInto(l : MutableList<T>, x: T, from: Int, to: Int): MutableList<T> {
    if (from == to) {
        l[from] = x
        return l
    }
    if (from < to ) {
        l.removeAt(from)
        val r = (l.subList(0, to) + listOf(x) + l.subList(to, l.size)).toMutableList()
        return r
    }
    val r = if (to == 0) (l + listOf(x)).toMutableList().also { it.removeAt(from) }
    else (l.subList(0, to) + listOf(x) + l.subList(to, l.size)).toMutableList().also { it.removeAt(from + 1) }
    return r
}

private fun toPos(value: Int, index: Int, numLines: Int): Int {
    val mod = value % (numLines - 1)
    val newPos = mod + index
    return when {
        //newPos == 0 -> numLines - 1
        newPos > numLines -> newPos - numLines + 1
        newPos >= 1 -> newPos
        else -> numLines + newPos - 1
    }
}
