package adventofcode.y2024 // ktlint-disable filename

import adventofcode.concat
import adventofcode.matchNumbers
import adventofcode.matches
import adventofcode.product
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    println("part1=" + sumMult(lines))

    var active = true
    val activeLines = mutableListOf<String>()
    lines.concat().split(")").forEach {
        val line = "$it)"
        if (line.endsWith("do()")) active = true
        if (line.endsWith("don't()")) active = false
        if (matches(line, regex).isNotEmpty() && active) activeLines += line
    }
    println("part2=" + sumMult(activeLines))
}
private const val regex = "mul\\(\\d+,\\d+\\)"
private fun sumMult(l: List<String>) = matches(l.concat(), regex).sumOf { matchNumbers(it).product() }
