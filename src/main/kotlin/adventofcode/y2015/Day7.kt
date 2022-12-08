package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile
import adventofcode.split

val values = mutableMapOf<String, UShort>()

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2015/day7.txt")
    println("part1=" + findValue("a", lines))
    values.clear()
    values["b"] = 16076.toUShort()
    println("part2=" + findValue("a", lines))
}

fun findValue(toFind: String, lines: List<String>) = values[toFind] ?: findValueInternal(toFind, lines)
fun findValueInternal(toFind: String, lines: List<String>): UShort {
    matches(toFind, "[0-9]+").let { if (it.isNotEmpty()) return it.first().toUShort() }
    lines.forEach { line ->
        val split = split(line)
        if (split[split.size - 1] == toFind) {
            return when {
                split[0] == "NOT" -> findValue(split[1], lines).inv()
                split[1] == "AND" -> findValue(split[0], lines) and findValue(split[2], lines)
                split[1] == "OR" -> findValue(split[0], lines) or findValue(split[2], lines)
                split[1] == "RSHIFT" -> (findValue(split[0], lines).toInt() shr split[2].toInt()).toUShort()
                split[1] == "LSHIFT" -> (findValue(split[0], lines).toInt() shl split[2].toInt()).toUShort()
                split[1] == "->" -> findValue(split[0], lines)
                else -> throw IllegalArgumentException("What is " + split[1] + " on [" + line + "] ?")
            }.also { values[toFind] = it }
        }
    }
    throw IllegalArgumentException("Cannot find a value for $toFind")
}
