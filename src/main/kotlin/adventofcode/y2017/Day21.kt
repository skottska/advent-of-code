package adventofcode.y2017 // ktlint-disable filename

import adventofcode.asString
import adventofcode.concat
import adventofcode.readFile
import adventofcode.rotate
import adventofcode.split
import java.lang.IllegalArgumentException

fun main() {
    val rules = readFile("src/main/resources/y2017/day21.txt").map {
        val split = split(it)
        split[0].split("/") to split[2].split("/")
    }.flatMap { pair ->
        val left = pair.first.map { it.toList() }
        val rotate = rotate(left)
        val rotate2 = rotate(rotate)
        val rotate3 = rotate(rotate2)
        listOf(left, rotate, rotate2, rotate3).flatMap { listOf(it, it.reversed()) }.map { it.fold("") { total, i -> total + i.asString() } to pair.second }
    }.toMap()

    println("part1=" + iterate(5, rules))
    println("part2=" + iterate(18, rules))
}

private fun iterate(numTime: Int, rules: Map<String, List<String>>) = (1..numTime).fold(listOf(".#.", "..#", "###")) { grid, _ -> splitGrid(grid, rules) }.sumOf { row -> row.count { it == '#' } }
private fun splitGrid(grid: List<String>, rules: Map<String, List<String>>): List<String> {
    val size = when {
        grid.size % 2 == 0 -> 2
        grid.size % 3 == 0 -> 3
        else -> throw IllegalArgumentException("cannot handle grid size of " + grid.size)
    }
    return (0 until grid.size / size).flatMap { i ->
        val sublist = grid.subList(i * size, (1 + i) * size)
        val layer = (0 until grid.size / size).map { j ->
            sublist.map { it.substring(j * size, (1 + j) * size) }
        }.map { rules.getValue(it.concat()) }
        layer.first().indices.map { row -> layer.map { inside -> inside[row] } }.map { it.concat() }
    }
}
