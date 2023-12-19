package adventofcode.y2023 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.Facing
import adventofcode.lagoonSize
import adventofcode.matchNumber
import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split

fun main() {
    val lines = readFile("src/main/resources/y2023/day19.txt")
    val workflows = lines.filter { it.isNotEmpty() && it[0].isLetter() }.map { line ->
        val name = line.substring(0..line.indexOf('{'))
        val instructions = line.substring(line.indexOf('{')).dropLast(1).split(",").map {  }
    }
    val parts = lines.filter { it.isNotEmpty() && it[0] == '{' }.map { line ->
        line.drop(1).split(",").associate { it[0] to matchNumber(it) }
    }
    println(parts)
}

private sealed interface Instruction
private data class Comparison()