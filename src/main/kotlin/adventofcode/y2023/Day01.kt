package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2023/day01.txt")
    val nums = lines.map { line ->
        val digits = line.filter { it.isDigit() }
        matchNumbers("" + digits.first() + "" + digits.last()).first()
    }
    println("part1=" + nums.sum())

    val digits = lines.map { matchNumbers(findFirstDigit(it, true) + findFirstDigit(it, false)).first() }
    println("part2=" + digits.sum())
}

private fun findFirstDigit(s: String, direction: Boolean): String {
    val replace = listOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )
    val indices = if (direction) s.indices else s.indices.reversed()
    indices.forEach { index ->
        if (s[index].isDigit()) return s[index].toString()
        replace.forEach { r ->
            if (s.substring(index).startsWith(r.first)) {
                return "" + r.second
            }
        }
    }
    return ""
}
