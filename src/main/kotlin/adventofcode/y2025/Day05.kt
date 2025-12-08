package adventofcode.y2025 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val space = lines.indexOf("")
    val freshList = lines.take(space).map { l -> matchNumbersLong(l).let { it.first()..it.last() } }.sortedBy { it.first }
    val ing = lines.drop(space + 1).map { it.toLong() }
    val part1 = ing.count { i -> freshList.any { i in it } }

    println("part1=$part1")
    val join = freshList.drop(1).fold(listOf(freshList.first())) { l, c ->
        when {
            l.last().last >= c.last -> l
            l.last().last + 1 < c.first -> l + listOf(c)
            else -> l.dropLast(1) + listOf(l.last().first..c.last)
        }
    }
    println("part2=" + join.sumOf { it.last - it.first + 1 })
}
