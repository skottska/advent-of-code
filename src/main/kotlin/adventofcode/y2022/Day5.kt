package adventofcode.y2022 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    println("part1=" + stacks(multiplePickup = false))
    println("part2=" + stacks(multiplePickup = true))
}

fun stacks(multiplePickup: Boolean): String {
    val lines = readFile("src/main/resources/y2022/day5.txt")
    var index = 0
    val stacks = mutableMapOf<Int, String>()
    while (index < lines.size) {
        val line = lines[index++]
        if (line.contains("1")) break
        (1..(line.length / 4) + 1).forEach { stack ->
            val pos = (stack - 1) * 4
            val crate = line.substring(pos, minOf(pos + 4, line.length)).firstOrNull { it in 'A'..'Z' }
            if (crate != null) stacks[stack] = (stacks[stack] ?: "") + crate
        }
    }
    index++
    while (index < lines.size) {
        val line = lines[index++]
        val (num, from, to) = Regex("[0-9]+").findAll(line).map { it.groupValues[0] }.toList().let { Triple(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
        val toMove = (stacks[from]?.substring(0, num) ?: "").let { if (multiplePickup) it else it.reversed()}
        stacks[from] = stacks[from]?.substring(num) ?: ""
        stacks[to] = toMove + (stacks[to] ?: "")
    }
    return stacks.keys.sorted().map { stacks[it]?.get(0) ?: "" }.fold("") { total, item -> total + item }
}
