package adventofcode.y2024 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles
import kotlin.math.abs

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val listA = lines.map { matchNumbers(it).first() }.sorted()
    val listB = lines.map { matchNumbers(it).last() }.sorted()
    println("part1=" + listA.zip(listB).sumOf { abs(it.first - it.second) })
    println("part2=" + listA.sumOf { it * listB.count { b -> b == it } })
}
