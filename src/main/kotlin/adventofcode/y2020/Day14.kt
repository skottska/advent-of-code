package adventofcode.y2020 // ktlint-disable filename

import adventofcode.asString
import adventofcode.matchNumbersLong
import adventofcode.readFile
import java.lang.invoke.MethodHandles
import java.math.BigInteger

fun main() {
    val lines = readFile(MethodHandles.lookup())
    println("part1="+ iterate(lines, isPart1 = true))
    println("part2="+ iterate(lines, isPart1 = false))
}

private fun iterate(lines: List<String>, isPart1: Boolean): Long {
    val map = mutableMapOf<Long, Long>()
    var mask = ""
    lines.forEach { line ->
        val split = line.split("=")
        when {
            split[0].startsWith("mask") -> mask = split[1].trim()
            else -> {
                val numbers = matchNumbersLong(line)
                if (isPart1) map[numbers.first()] = valueMask(mask, numbers.last())
                else addressMask(mask, numbers.first()).forEach { map[it] = numbers.last() }
            }
        }
    }
    return map.values.sum()
}

private fun valueMask(mask: String, value: Long): Long {
     val valueString = value.toString(radix = 2).reversed()
    return mask.reversed().mapIndexed { index, c ->
        when {
            c == 'X' && index < valueString.length -> valueString[index]
            c == 'X' -> '0'
            else -> c
        }
    }.asString().reversed().let { BigInteger(it, 2).toLong() }
}

private fun addressMask(mask: String, value: Long): List<Long> {
    val valueString = value.toString(radix = 2).reversed()
    return mask.reversed().mapIndexed { index, c ->
        when {
            c == '0' && index < valueString.length -> valueString[index]
            c == '1' -> '1'
            else -> c
        }
    }.fold(listOf("")) { total, c ->
        when (c) {
            'X' -> listOf('0', '1')
            else -> listOf(c)
        }.flatMap { explode -> total.map { "" + explode + it } }
    }.map { BigInteger(it, 2).toLong() }
}