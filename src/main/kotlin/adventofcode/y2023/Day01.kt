package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2023/day01.txt")
    val nums = lines.sumOf { l -> l.filter { it.isDigit() }.let { matchNumbers(it.first() + "" + it.last()).first() } }
    println("part1=$nums")
    val digits = lines.sumOf { matchNumbers(firstDigit(it, true) + firstDigit(it, false)).first() }
    println("part2=$digits")
}

private fun firstDigit(s: String, direction: Boolean): String {
    val replace = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val indices = if (direction) s.indices else s.indices.reversed()
    indices.forEach { index ->
        if (s[index].isDigit()) return s[index].toString()
        replace.forEachIndexed { rIndex, r ->
            if (s.substring(index).startsWith(r)) {
                return "" + (rIndex + 1)
            }
        }
    }
    return ""
}
