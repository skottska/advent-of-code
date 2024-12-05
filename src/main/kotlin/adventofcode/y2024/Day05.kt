package adventofcode.y2024 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val split = lines.indexOf("")
    val order = lines.subList(0, split).map { line -> matchNumbers(line) }.map { it.first() to it.last() }
    val printed = lines.subList(split + 1, lines.size).map { matchNumbers(it) }

    val ok = printed.filter { print ->
        print.mapIndexed { i, a -> before(a, print.drop(i), order) }.all { it }
    }
    println("part1="+ok.sumOf { it[it.size / 2] })

    val part2 = printed.filter { it !in ok }.map { line ->
        line.fold(emptyList<Int>()) { t, _ -> t + (line - t).let { l -> l.first { before(it, l, order) } } }
    }
    println("part2="+part2.sumOf { it[it.size / 2] })
}

private fun before(a: Int, l: List<Int>, order: List<Pair<Int, Int>>) = l.all { b -> order.none { it.first == b && it.second == a } }
