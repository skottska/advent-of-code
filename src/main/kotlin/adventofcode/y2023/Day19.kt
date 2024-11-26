package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumber
import adventofcode.readFile
import adventofcode.split
import java.lang.invoke.MethodHandles
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val workflows = lines.filter { it.isNotEmpty() && it[0].isLetter() }.associate { line ->
        val name = line.substring(0 until line.indexOf('{'))
        val instructions = line.substring(line.indexOf('{')).drop(1).dropLast(1).split(",").map {
            if (!it.contains(':')) InstructionSimple(it)
            else {
                val rating = it[0]
                val value = matchNumber(it)
                val range = when (it[1]) {
                    '>' -> (value + 1)..4000
                    '<' -> 1 until value
                    else -> throw IllegalArgumentException("Can't handle " + it[1])
                }
                InstructionComparison(rating, range, it.substring(it.indexOf(':') + 1))
            }
        }
        name to instructions
    }
    val parts = lines.filter { it.isNotEmpty() && it[0] == '{' }.map { line ->
        line.drop(1).split(",").associate { it[0] to matchNumber(it) }
    }

    println("part1=" + parts.filter { evaluate(it, workflows) }.sumOf { it.values.sum() })

    val part2 = part2(listOf('a', 'm', 's', 'x').associateWith { 1..4000 }, workflows)
    println("part2=" + part2.sumOf { it.values.fold(1L) { total, i -> total * (i.last - i.first + 1) } })
}

private fun part2(possiblePart: Map<Char, IntRange>, workflows: Map<String, List<Instruction>>, cur: String = "in"): List<Map<Char, IntRange>> {
    val nexts = workflows.getValue(cur).fold(listOf<Pair<String?, Map<Char, IntRange>>>(null to possiblePart)) { total, i ->
        total + if (total.last().first == null) {
            i.applyRange(total.last().second)
        } else emptyList()
    }

    val accepted = nexts.filter { it.first == "A" }.map { it.second }
    val others = nexts.mapNotNull { next ->
        next.first?.let {
            if (it.first().isUpperCase()) null
            else part2(next.second, workflows, it)
        }
    }.flatten()
    return accepted + others
}

private fun evaluate(part: Map<Char, Int>, workflows: Map<String, List<Instruction>>, cur: String = "in"): Boolean {
    return when (val next = workflows.getValue(cur).firstNotNullOf { it.apply(part) }) {
        "A" -> true
        "R" -> false
        else -> evaluate(part, workflows, next)
    }
}

private sealed interface Instruction {
    fun apply(part: Map<Char, Int>): String?
    fun applyRange(part: Map<Char, IntRange>): List<Pair<String?, Map<Char, IntRange>>>
}
private data class InstructionSimple(val to: String) : Instruction {
    override fun apply(part: Map<Char, Int>) = to
    override fun applyRange(part: Map<Char, IntRange>): List<Pair<String?, Map<Char, IntRange>>> = listOf(to to part)
}
private data class InstructionComparison(val rating: Char, val range: IntRange, val to: String) : Instruction {
    override fun apply(part: Map<Char, Int>) = if (part.getValue(rating) in range) to else null
    override fun applyRange(part: Map<Char, IntRange>): List<Pair<String?, Map<Char, IntRange>>> {
        val inRange = part.getValue(rating)
        return when {
            inRange.last < range.first || inRange.first > range.last -> listOf(null to part)
            range.first <= inRange.first && range.last >= inRange.last -> listOf(to to part)
            else -> {
                val inside = max(inRange.first, range.first)..min(inRange.last, range.last)
                val outside = if (range.first == 1) (range.last + 1)..inRange.last else inRange.first until range.first
                val removed = part.filter { it.key != rating }
                listOf(to to removed + (rating to inside), null to removed + (rating to outside))
            }
        }
    }
}
