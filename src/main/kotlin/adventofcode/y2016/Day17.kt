package adventofcode.y2016 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.md5
import adventofcode.readFile

fun main() {
    val input = readFile("src/main/resources/y2016/day17.txt")[0]
    val result = iterate(input, listOf(Coord(0, 0) to ""))
    println("part1=" + result.minBy { it.length })
    println("part2=" + result.maxBy { it.length }.length)
}

private fun iterate(input: String, paths: List<Pair<Coord, String>>): List<String> {
    val result = mutableListOf<String>()
    var activePaths = paths
    while (activePaths.isNotEmpty()) {
        result += activePaths.filter { it.first == Coord(3, 3) }.map { it.second }
        val notFinished = activePaths.filter { it.first != Coord(3, 3) }
        activePaths = notFinished.flatMap { cur ->
            val md5 = md5(input + cur.second)
            listOf(
                Triple(cur.first.up(), md5[0], 'U'),
                Triple(cur.first.down(), md5[1], 'D'),
                Triple(cur.first.left(), md5[2], 'L'),
                Triple(cur.first.right(), md5[3], 'R')
            ).filter { it.first.row in 0..3 && it.first.col in 0..3 && it.second in listOf('b', 'c', 'd', 'e', 'f') }
                .map { it.first to cur.second + it.third }
        }
    }
    return result
}
