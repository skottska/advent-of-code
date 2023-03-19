package adventofcode.y2019 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.string

fun main() {
    val size = 6 * 25
    val layers = readFile("src/main/resources/y2019/day08.txt")[0].map { it.digitToInt() }.windowed(size = size, step = size)
    val part1 = layers.minBy { layer -> layer.count { it == 0 } }
    println("part1=" + (part1.count { it == 1 } * part1.count { it == 2 }))
    val part2 = layers.reduce { acc, b -> acc.mapIndexed { index, i -> if (i == 2) b[index] else i } }
    part2.windowed(25, 25).map { layer -> layer.map { if (it == 1) '#' else ' ' } }.forEach { println(it.string()) }
}
