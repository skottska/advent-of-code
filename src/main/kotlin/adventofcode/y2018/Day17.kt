package adventofcode.y2018 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbers
import adventofcode.printCoords
import adventofcode.readFile
import adventofcode.split

fun main() {
    val clayCoords = readFile("src/main/resources/y2018/day17.txt").flatMap { line ->
        val split = split(line).sorted().map { s ->
            matchNumbers(s).let {
                when (it.size) {
                    1 -> it.first()..it.first()
                    else -> it.first()..it.last()
                }
            }
        }
        split.first().flatMap { x ->
            split.last().map { y ->
                Coord(y, x)
            }
        }
    }

    val maxDepth = clayCoords.maxOf { it.row }
    val minDepth = clayCoords.minOf { it.row }
    val grid = clayCoords.associateWith { '#' }.toMutableMap()
    val start = Coord(0, 500)
    iterate(start, grid, maxDepth)

    printCoords(grid.keys) { grid.getOrDefault(it, '.').toString() }
    println("part1=" + (grid.filter { it.key.row in minDepth..maxDepth }.values.count { it in water }))
    println("part2=" + (grid.filter { it.key.row in minDepth..maxDepth }.values.count { it == '~' }))
}

private fun iterate(start: Coord, grid: MutableMap<Coord, Char>, maxDepth: Int) {
    var lastSoftBelow = lastSoftBelow(start, grid, maxDepth)
    while (lastSoftBelow != start) {
        val boxes = listOf(boxed(lastSoftBelow, Coord::left, grid), boxed(lastSoftBelow, Coord::right, grid))
        val setStandingWater = { c: Char -> (boxes.first().first.col..boxes.last().first.col).forEach { grid[Coord(lastSoftBelow.row, it)] = c } }
        if (boxes.any { it.second == Boxed.DROP }) {
            boxes.filter { it.second == Boxed.DROP }.forEach { iterate(it.first, grid, maxDepth) }
        } else {
            when {
                grid.getOrDefault(lastSoftBelow.down(), '.') !in hard -> grid[lastSoftBelow] = '|'
                boxes.all { it.second == Boxed.HARD_BOXED } -> setStandingWater('~')
                boxes.all { it.second == Boxed.SOFT_BOXED } -> setStandingWater('|')
                else -> setStandingWater('|')
            }
            lastSoftBelow = lastSoftBelow(start, grid, maxDepth)
        }
    }
    grid[start] = '|'
}
private val hard = setOf('#', '~')
private val soft = setOf('.', '|')
private val water = setOf('|', '~')
private enum class Boxed { HARD_BOXED, SOFT_BOXED, DROP }
private fun boxed(start: Coord, direction: (Coord) -> Coord, grid: MutableMap<Coord, Char>): Pair<Coord, Boxed> {
    var prev = start
    var cur = direction(prev)
    while (grid.getOrDefault(cur, '.') in soft && grid.getOrDefault(prev.down(), '.') in hard) {
        prev = cur
        cur = direction(prev)
    }
    return prev to when {
        grid.getOrDefault(cur, '.') in hard -> Boxed.HARD_BOXED
        grid.getOrDefault(prev, '.') == '|' -> Boxed.SOFT_BOXED
        else -> Boxed.DROP
    }
}
private fun lastSoftBelow(start: Coord, grid: MutableMap<Coord, Char>, maxDepth: Int): Coord {
    var prev = start
    var cur = start.down()
    while (grid.getOrDefault(cur, '.') == '.' && cur.row <= maxDepth) {
        prev = cur
        cur = cur.down()
    }
    return prev
}
