package adventofcode.y2020 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())

    val configPre = lines.subList(0, lines.indexOfFirst { it.isEmpty() }).map { line ->
        val split = line.split(':')
        matchNumbers(split.first()).first() to split.last().trim()
    }

    val knownConfig = configPre.filter { it.second.contains("\"") }.associate { it.first to it.second[1].toString() }
    val unknownConfig = configPre.filter { !it.second.contains("\"") }.associate { config ->
        config.first to config.second.split("|").map { matchNumbers(it) }
    }.toMutableMap()

    val messages = lines.drop(lines.indexOfFirst { it.isEmpty() } + 1)
    println("part1=" + messages.count { iterate(0, knownConfig, unknownConfig, it, 0).contains(it.length) })

    unknownConfig[8] = listOf(listOf(42), listOf(42, 8))
    unknownConfig[11] = listOf(listOf(42, 31), listOf(42, 11, 31))
    println("part2=" + messages.count { iterate(0, knownConfig, unknownConfig, it, 0).contains(it.length) })
}

private fun iterate(pos: Int, knownConfig: Map<Int, String>, unknownConfig: Map<Int, List<List<Int>>>, message: String, curConfig: Int): List<Int> {
    val knownConfigVal = knownConfig[curConfig]
    return when {
        knownConfigVal != null -> {
            when {
                pos + knownConfigVal.length > message.length -> emptyList()
                knownConfigVal == message.substring(pos, pos + knownConfigVal.length) -> listOf(pos + knownConfigVal.length)
                else -> emptyList()
            }
        }
        else -> {
            val unknownConfigVal = unknownConfig.getValue(curConfig)
            unknownConfigVal.flatMap { option ->
                val first = iterate(pos, knownConfig, unknownConfig, message, option.first())
                option.drop(1).fold(first) { total, i ->
                    total.flatMap { iterate(it, knownConfig, unknownConfig, message, i) }
                }
            }
        }
    }
}
