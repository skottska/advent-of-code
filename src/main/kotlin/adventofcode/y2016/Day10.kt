package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2016/day10.txt")
    val bots = mutableMapOf<Int, Bot>()
    val outputs = mutableMapOf<Int, Int>()
    lines.forEach { line ->
        val nums = matchNumbers(line)
        when {
            line.startsWith("value") -> bots[nums.last()] = bots.getOrDefault(nums.last(), Bot()).addChip(nums.first())
            else -> {
                bots[nums.first()] = bots.getOrDefault(nums.first(), Bot()).copy(
                    lower = if (line.contains("low to bot")) nums[1] else null,
                    higher = if (line.contains("high to bot")) nums[2] else null,
                    lowerOutput = if (line.contains("low to output")) nums[1] else null,
                    higherOutput = if (line.contains("high to output")) nums[2] else null
                )
            }
        }
    }
    while (bots.filter { it.value.chips.size == 2 }.isNotEmpty()) {
        val cascade = bots.filter { it.value.chips.size == 2 }
        val found = cascade.filter { it.value.chips.containsAll(listOf(61, 17)) }
        if (found.isNotEmpty()) println("part1=" + found.keys.first())
        cascade.forEach { c ->
            val chips = c.value.chips.sorted()
            c.value.lower?.let { lower -> bots[lower] = bots.getOrDefault(lower, Bot()).addChip(chips.first()) }
            c.value.lowerOutput?.let { lower -> outputs[lower] = chips.first() }
            c.value.higher?.let { higher -> bots[higher] = bots.getOrDefault(higher, Bot()).addChip(chips.last()) }
            c.value.higherOutput?.let { higher -> outputs[higher] = chips.last() }
            bots[c.key] = c.value.copy(chips = emptyList())
        }
    }
    println("part2=" + (outputs.getValue(0) * outputs.getValue(1) * outputs.getValue(2)))
}

private data class Bot(val lower: Int? = null, val higher: Int? = null, val lowerOutput: Int? = null, val higherOutput: Int? = null, val chips: List<Int> = emptyList()) {
    fun addChip(chip: Int) = copy(chips = chips + listOf(chip))
}
