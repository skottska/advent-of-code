package adventofcode.y2022 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile
import adventofcode.split

fun main() {
    val lines = readFile("src/main/resources/y2022/day11.txt")
    println("part1=" + monkeyBusiness(lines, 3, 20))
    println("part2=" + monkeyBusiness(lines, 1, 10000))
}

private fun monkeyBusiness(lines: List<String>, divisor: Int, rounds: Int): Long {
    val monkeys = lines.windowed(7, 7, true).map { monkey ->
        Monkey(
            items = matches(monkey[1], "[0-9]+").map { it.toLong() },
            operation = parseOperation(monkey[2]),
            throwTo = parseThrowTo(monkey),
            divisor = split(monkey[3])[3].toInt()
        )
    }

    val lowestCommonDivisor = divisor * monkeys.map { it.divisor }.fold(1L) { total, i -> total * i }

    repeat(rounds) { _ ->
        monkeys.forEach { m ->
            m.items.map { m.operation(it) / divisor }.map { it % lowestCommonDivisor }.forEach { monkeys[m.throwTo(it)].receive(it) }
            m.throwItems()
        }
    }
    return monkeys.map { it.inspected }.sortedByDescending { it }.let { it[0] * it[1] }
}

private fun parseThrowTo(lines: List<String>): (Long) -> Int {
    val divisible = split(lines[3])[3].toLong()
    val toMonkeyTrue = split(lines[4])[5].toInt()
    val toMonkeyFalse = split(lines[5])[5].toInt()
    return { x -> if (x % divisible == 0L) toMonkeyTrue else toMonkeyFalse }
}

private fun parseOperation(s: String): (Long) -> Long = split(s).let { split ->
    when {
        split[5] == "old" && split[4] == "+" -> { x -> x + x }
        split[5] == "old" -> { x -> x * x }
        split[4] == "+" -> { x -> x + split[5].toLong() }
        else -> { x -> x * split[5].toLong() }
    }
}

private data class Monkey(var items: List<Long>, val operation: (Long) -> Long, val throwTo: (Long) -> Int, val divisor: Int) {
    var inspected = 0L
    fun receive(item: Long) { items += item }
    fun throwItems() { inspected += items.size; items = listOf() }
}
