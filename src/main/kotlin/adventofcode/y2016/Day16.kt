package adventofcode.y2016 // ktlint-disable filename

import adventofcode.asString
import adventofcode.readFile

fun main() {
    val input = readFile("src/main/resources/y2016/day16.txt")[0].map { it }
    println("part1=" + generateChecksum(generateFile(input, fileLength = 272)).asString())
    println("part1=" + generateChecksum(generateFile(input, fileLength = 35651584)).asString())
}

private fun generateFile(input: List<Char>, fileLength: Int): List<Char> {
    if (input.size >= fileLength) return input.subList(0, fileLength)
    return generateFile(input + listOf('0') + input.reversed().map { if (it == '0') '1' else '0' }, fileLength)
}

private fun generateChecksum(input: List<Char>): List<Char> {
    val reduce = input.windowed(size = 2, step = 2).map { if (it.toSet().size == 1) '1' else '0' }
    return if (reduce.size % 2 == 0) generateChecksum(reduce) else reduce
}
