package adventofcode.y2024 // ktlint-disable filename

import adventofcode.concat
import adventofcode.matchNumbers
import adventofcode.matches
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    println("part1=" + sumMult(lines))

    var enabled = true
    val activeLines = mutableListOf<String>()
    lines.concat().split(")").forEach {
        val line = "$it)"
        if (matches(line, "do\\(\\)").isNotEmpty()) enabled = true
        if (matches(line, "don't\\(\\)").isNotEmpty()) enabled = false
        if (matches(line, regex).isNotEmpty() && enabled) activeLines += line
    }
    println("part2=" + sumMult(activeLines))
}
private const val regex = "mul\\([0-9]+,[0-9]+\\)"
private fun sumMult(l: List<String>) = matches(l.concat(), regex).sumOf { matchNumbers(it).fold(1L) { total, i -> total * i } }
