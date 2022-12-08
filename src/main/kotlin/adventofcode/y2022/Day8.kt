package adventofcode.y2022

import adventofcode.readFile

fun main(args: Array<String>) {
    val grid = mutableListOf<List<Int>>()
    readFile("src/main/resources/y2022/day8.txt").forEach { line -> grid.add(line.asSequence().map { it.code - '0'.code }.toList()) }

    val transpose = mutableListOf<MutableList<Int>>()
    grid.forEach { line ->
        line.forEachIndexed { y, i ->
            if (transpose.getOrNull(y) == null) transpose.add(y, mutableListOf())
            transpose[y].add(i)
        }
    }
    println("part1=" + (numVisible(grid, false) + numVisible(transpose, true)).size)

    var highestScore = 0
    grid.forEachIndexed { x, line ->
        line.forEachIndexed { y, _ ->
            highestScore = maxOf(highestScore, score(x, y, grid))
        }
    }
    println("part2=$highestScore")
}

fun score(x: Int, y: Int, grid: List<List<Int>>): Int {
    val func = { xDir: Int, yDir: Int -> scoreDirection(grid[x][y], x + xDir, y + yDir, xDir, yDir, grid) }
    return func(1, 0) * func(-1, 0) * func(0, 1) * func(0, -1)
}

fun scoreDirection(compareTo: Int, x: Int, y: Int, xDir: Int, yDir: Int, grid: List<List<Int>>): Int {
    val i = grid.getOrNull(x)?.getOrNull(y) ?: return 0
    if (i >= compareTo) return 1
    return 1 + scoreDirection(compareTo, x + xDir, y + yDir, xDir, yDir, grid)
}

fun numVisible(grid: List<List<Int>>, isTransposed: Boolean): Set<Pair<Int, Int>> {
    val answer = mutableSetOf<Pair<Int, Int>>()
    grid.forEachIndexed { x, line ->
        var processed = mutableSetOf<Int>()
        line.forEachIndexed { y, i ->
            if (processed.all { it < i }) {
                if (isTransposed) answer.add(Pair(y, x))
                else answer.add(Pair(x, y))
            }
            processed.add(i)
        }
        processed = mutableSetOf()
        line.reversed().forEachIndexed { y, i ->
            if (processed.all { it < i }) {
                if (isTransposed) answer.add(Pair(line.size - 1 - y, x))
                else answer.add(Pair(x, line.size - 1 - y))
            }
            processed.add(i)
        }
    }
    return answer
}
