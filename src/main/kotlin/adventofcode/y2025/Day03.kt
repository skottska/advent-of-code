package adventofcode.y2025 // ktlint-disable filename

import adventofcode.power
import adventofcode.readFile
import java.lang.invoke.MethodHandles
import java.math.BigInteger

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { line -> line.map { it.digitToInt() } }
    println("part1=" + lines.sumOf { maxNum(it, 2) })
    println("part2=" + lines.sumOf { maxNum(it, 12) })
}

private fun maxNum(list: List<Int>, left: Int): Long =
    if (left == 1) list.max().toLong()
    else (9 downTo 0).firstNotNullOf { num ->
        val index = list.dropLast(left - 1).indexOf(num)
        if (index == -1) null else num * power(10L, left - 1L) + maxNum(list.drop(index + 1), left - 1)
    }
