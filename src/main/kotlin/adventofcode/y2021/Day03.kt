package adventofcode.y2021 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.transpose

fun main() {
    val lines = readFile("src/main/resources/y2021/day03.txt").map { line -> line.map { it } }
    val transpose = transpose(lines)
    val minFunc = { l: List<Char> -> if (l.count { it == '0' } <= l.count { it == '1' }) '0' else '1' }
    val maxFunc = { l: List<Char> -> if (l.count { it == '0' } > l.count { it == '1' }) '0' else '1' }
    val gamma = transpose.map { maxFunc(it) }.toCharArray().concatToString()
    val epsilon = transpose.map { minFunc(it) }.toCharArray().concatToString()
    println("part1=" + gamma.toInt(2) * epsilon.toInt(2))
    println("part2=" + iterate(lines, 0, maxFunc) * iterate(lines, 0, minFunc))
}

private fun iterate(lines: List<List<Char>>, index: Int, compareFunc: (List<Char>) -> Char): Int {
    if (lines.size == 1) return lines.first().toCharArray().concatToString().toInt(2)
    val max = compareFunc(transpose(lines)[index])
    return iterate(lines.filter { it[index] == max }, index + 1, compareFunc)
}
