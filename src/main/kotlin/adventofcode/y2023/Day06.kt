package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.matchNumbersLong
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val nums1 = lines.map { matchNumbers(it) }
    val races = nums1.first().size
    (0 until races).fold(1) { total, race ->
        val time = nums1.first()[race]
        val results = (1 until time).count { (time - it) * it > nums1.last()[race] }
        total * (if (results == 0) 1 else results)
    }.also { println("part1=$it") }

    val nums2 = lines.map { it.filter { c -> c.isDigit() } }.map { matchNumbersLong(it).first() }
    val time = nums2.first()
    (1 until time).count { (time - it) * it > nums2.last() }.also { println("part2=$it") }
}
