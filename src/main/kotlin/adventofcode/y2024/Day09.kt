package adventofcode.y2024 // ktlint-disable filename

import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val line = readFile(MethodHandles.lookup()).first()
    val filesystem = line.mapIndexed { index, c ->
        val o = if (index % 2 == 0) index / 2 else null
        (1..c.digitToInt()).map { o }
    }.flatten()
    var forward = 0
    var backward = filesystem.size - 1
    val part1 = mutableListOf<Int>()
    while (forward <= backward) {
        val next = filesystem[forward++]
        val nextBackward = filesystem[backward]
        when {
            next != null -> part1 += next
            nextBackward != null -> { part1 += nextBackward; backward-- }
            else -> { backward--; forward-- }
        }
    }
    println("part1="+part1.mapIndexed { index, i -> (index.toLong() * i.toLong()) }.sum())

    val part2List = filesystem.toMutableList()
    var part2Index = part2List.size - 1
    while (part2Index != -1) {
        part2Index = pack(part2List, part2Index)
    }
    println("part2="+part2List.mapIndexed { index, i -> if (i == null) 0 else (index.toLong() * i.toLong()) }.sum())
}

private fun pack(list: MutableList<Int?>, index: Int): Int {
    var curIndex = index
    while (curIndex >= 0 && list[curIndex] == null) curIndex--
    if (curIndex == -1) return curIndex
    var startIndex = curIndex
    while (startIndex >= 0 && list[startIndex] == list[curIndex]) startIndex--
    val size = curIndex - startIndex
    val space = (0..startIndex).firstOrNull { i -> list.subList(i, i + size).all { it == null } }
    if (space != null) {
        (0 until size).forEach {
            list[space + it] = list[curIndex]
            list[startIndex + it + 1] = null
        }
    }
    return startIndex
}
