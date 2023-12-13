package adventofcode.y2023 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.transposeStrings
import kotlin.math.min

fun main() {
    val lines = readFile("src/main/resources/y2023/day13.txt")
    val blank = listOf(-1) + lines.mapIndexedNotNull { index, i -> if (i.isBlank()) index else null} + lines.size
    val grids = blank.windowed(size = 2).map { lines.subList(it.first()+1, it.last()) }

    val rows = grids.sumOf { grid -> findMirror(grid) }
    val cols = grids.sumOf { grid -> findMirror(transposeStrings(grid)) }


    grids.forEach {
        val row = findMirror(it)
        val col = findMirror(transposeStrings(it))
        if (row == 0 && col == 0) {
            it.forEach { x -> println(x) }; println()
            transposeStrings(it).forEach { x -> println(x) }; println()
            println("nah man")
        }
    }
    println("part1="+(cols + 100 * rows))

    val fixed = grids.map { grid ->
        val smudge = findSmudge(grid)
        if (smudge != null) {
            grid.mapIndexed { index, row -> if (index == smudge.first) grid.get(smudge.second) else row }
        }
        else {
            val transpose = transposeStrings(grid)
            val smudge2 = findSmudge(transpose)
            if (smudge2 != null) {
                transpose.mapIndexed { index, row -> if (index == smudge2.first) transpose.get(smudge2.second) else row }
                transposeStrings(transpose)
            }
            else {
                throw IllegalArgumentException("nooo")
            }
        }
    }

    /*fixed.forEach {
        val row = findMirror(it)
        val col = findMirror(transposeStrings(it))
        if (row == 0 && col == 0) {
            it.forEach { x -> println(x) }; println()
            transposeStrings(it).forEach { x -> println(x) }; println()
            println("nah man")
        }
    }*/

    val rows2 = grids.sumOf { grid ->
        /*val smudge = findSmudge(grid)
        if (smudge != null) {
            findMirror(grid.mapIndexed { index, row -> if (index == smudge.first) grid.get(smudge.second) else row })
        } else 0*/
        fixSmudge(grid)
    }
    val cols2 = grids.sumOf {
        /*val grid = transposeStrings(it)
        val smudge = findSmudge(grid)
        if (smudge != null) {
            findMirror(grid.mapIndexed { index, row -> if (index == smudge.first) grid.get(smudge.second) else row })
        } else 0*/
        fixSmudge(transposeStrings(it))
    }
    println("part2="+(cols2 + 100 * rows2))



    val rows3 = grids.map { grid ->
        fixSmudge(grid)
    }
    val cols3 = grids.map {
        fixSmudge(transposeStrings(it))
    }
    rows3.zip(cols3).forEachIndexed { index, it ->
        if (it.first == 0 && it.second == 0) {
            println(index)
        }
    }
}

private fun fixSmudge(list: List<String>): Int {
    val currentMirror = findMirror(list)
    (0 until list.size - 1).forEach { indexA ->
        (indexA + 1 until list.size).forEach { indexB ->
            if ((indexB - indexA) % 2 != 0 && list.first().indices.count { list[indexA][it] != list[indexB][it] } == 1) {
                val replaceFirst = findMirror(list.mapIndexed { index, row -> if (index == indexA) list[indexB] else row }, listOf(currentMirror))
                if (replaceFirst !in listOf(0, currentMirror)) return replaceFirst
                val replaceLast = findMirror(list.mapIndexed { index, row -> if (index == indexB) list[indexA] else row }, listOf(currentMirror))
                if (replaceLast !in listOf(0, currentMirror)) return replaceLast
            }
        }
    }
    return 0
}


private fun findSmudge(list: List<String>): Pair<Int, Int>? {
    (0 until list.size - 1).forEach { indexA ->
        (indexA + 1 until list.size).forEach { indexB ->
            if ((indexB - indexA) % 2 != 0) {
                val mid = indexA + ((indexB - indexA - 1) / 2) + 1
                val before = if (indexA + 1 == indexB) emptyList() else list.subList(indexA + 1, mid).reversed()
                val after = if (indexA + 1 == indexB) emptyList() else list.subList(mid, indexB)
                if (before.zip(after).all { it.first == it.second }) {
                    val diff = list.first().indices.count { list[indexA][it] != list[indexB][it] }
                    if (diff == 1) {
                        return indexA to indexB
                    }
                }
            }
        }
    }
    return null
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


// 25976 low
// 31997 low
// 43290 low