package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val files = readFile("src/main/resources/y2015/day18.txt")
    var grid = files.map { it.map { c -> c } }
    repeat(100) { grid = gridMap(grid, brokenCorners = false) }
    println("part1=" + grid.sumOf { it.filter { c -> c == '#' }.size })

    grid = breakCorners(files.map { it.map { c -> c } })
    repeat(100) { grid = gridMap(grid, brokenCorners = true) }
    println("part2=" + grid.sumOf { it.filter { c -> c == '#' }.size })
}

fun breakCorners(grid: List<List<Char>>) = grid.mapIndexed { x, row -> row.mapIndexed { y, c -> if (isCorner(x) && isCorner(y)) '#' else c } }

fun gridMap(grid: List<List<Char>>, brokenCorners: Boolean) = grid.mapIndexed { x, row -> List(row.size) { y -> animate(x, y, grid, brokenCorners) } }

fun animate(x: Int, y: Int, grid: List<List<Char>>, brokenCorners: Boolean): Char {
    val c = grid[x][y]
    val surrounding = surrounding(x, y, grid)
    return when {
        brokenCorners && isCorner(x) && isCorner(y) -> '#'
        c == '#' && surrounding in 2..3 -> '#'
        c == '.' && surrounding == 3 -> '#'
        else -> '.'
    }
}

fun surrounding(x: Int, y: Int, grid: List<List<Char>>): Int {
    val dir = listOf(-1, 0, 1)
    var num = 0
    dir.forEach { xOffset ->
        dir.forEach { yOffset ->
            when {
                xOffset == 0 && yOffset == 0 -> Unit
                (x + xOffset).let { it < 0 || it >= 100 } -> Unit
                (y + yOffset).let { it < 0 || it >= 100 } -> Unit
                grid[x + xOffset][y + yOffset] == '#' -> num++
                else -> Unit
            }
        }
    }
    return num
}

fun isCorner(c: Int) = c == 0 || c == 99
