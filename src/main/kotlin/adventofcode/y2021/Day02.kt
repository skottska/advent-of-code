package adventofcode.y2021 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2021/day02.txt")
    val func = { name: String -> lines.filter { it.contains(name) }.sumOf { matchNumbers(it).first() } }
    println("part1="+(func("forward") * (func("down") - func("up"))))
    val part2 = lines.fold(Triple(0L, 0L, 0L)) { total, line ->
        val num = matchNumbers(line).first()
        when {
            line.contains("down") -> total.copy(second = total.second + num)
            line.contains("up") -> total.copy(second = total.second - num)
            else -> total.copy(first = num + total.first, third = num * total.second + total.third)
        }
    }
    println("part2="+part2.first * part2.third)
}
