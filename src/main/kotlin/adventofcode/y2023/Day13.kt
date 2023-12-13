package adventofcode.y2023 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.transposeStrings
import kotlin.math.min

fun main() {
    val lines = readFile("src/main/resources/y2023/day13.txt")
    val blank = listOf(-1) + lines.mapIndexedNotNull { index, i -> if (i.isBlank()) index else null } + lines.size
    val grids = blank.windowed(size = 2).map { lines.subList(it.first() + 1, it.last()) }

    val part1 = grids.sumOf { findMirror(transposeStrings(it)) } + 100 * grids.sumOf { findMirror(it) }
    println("part1=$part1")

    val part2 = grids.sumOf { fixSmudge(transposeStrings(it)) } + 100 * grids.sumOf { fixSmudge(it) }
    println("part2=$part2")
}

private fun fixSmudge(list: List<String>): Int {
    val currentMirror = listOf(findMirror(list))
    (0 until list.size - 1).forEach { indexA ->
        (indexA + 1 until list.size).forEach { indexB ->
            if ((indexB - indexA) % 2 != 0 && list.first().indices.count { list[indexA][it] != list[indexB][it] } == 1) {
                val replaceFirst = findMirror(list.mapIndexed { index, row -> if (index == indexA) list[indexB] else row }, currentMirror)
                if (replaceFirst != 0) return replaceFirst
                val replaceLast = findMirror(list.mapIndexed { index, row -> if (index == indexB) list[indexA] else row }, currentMirror)
                if (replaceLast != 0) return replaceLast
            }
        }
    }
    return 0
}

private fun findMirror(list: List<String>, ignore: List<Int> = emptyList()): Int {
    (0 until list.size - 1).forEach { index ->
        if (list[index] == list[index + 1] && (index + 1) !in ignore) {
            val min = min(index, list.size - index - 2)
            if (min == 0) return index + 1
            val before = list.subList(index - min, index).reversed()
            val after = list.subList(index + 2, index + 2 + min)
            if (before.zip(after).all { it.first == it.second }) {
                return index + 1
            }
        }
    }
    return 0
}
