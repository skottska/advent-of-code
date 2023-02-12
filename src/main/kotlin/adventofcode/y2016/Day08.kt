package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2016/day08.txt")
    val maxCol = 50; val maxRow = 6
    val grid = (0 until maxRow).map { (0 until maxCol).map { false }.toMutableList() }.toMutableList()

    lines.forEach { line ->
        val nums = matchNumbers(line)
        when {
            line.startsWith("rect") -> {
                (0 until nums.first()).forEach { col ->
                    (0 until nums.last()).forEach { row ->
                        grid[row][col] = true
                    }
                }
            }
            line.startsWith("rotate row") -> grid[nums.first()] = (0 until maxCol).map { grid[nums.first()][rotate(it, nums.last(), maxCol)] }.toMutableList()
            else -> {
                val rotated = (0 until maxRow).map { grid[rotate(it, nums.last(), maxRow)][nums.first()] }
                (0 until maxRow).forEach { grid[it][nums.first()] = rotated[it] }
            }
        }
    }
    println("part1="+grid.map { row -> row.filter { it } }.flatten().size)
    println("part2=")
    grid.forEach { row -> row.forEach { print(if (it) "# " else ". ") }; println() }
}

private fun rotate(i: Int, rotate: Int, max: Int): Int {
    var x = i - rotate
    while (x < 0) x += max
    return x
}
