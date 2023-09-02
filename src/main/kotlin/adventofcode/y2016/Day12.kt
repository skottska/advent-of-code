package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split

fun main() {
    val lines = readFile("src/main/resources/y2016/day12.txt")
    println("part1=" + iterate(lines, mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)))
    println("part2=" + iterate(lines, mutableMapOf("a" to 0, "b" to 0, "c" to 1, "d" to 0)))
}

private fun iterate(lines: List<String>, map: MutableMap<String, Int>): Int {
    var pos = 0
    while (pos < lines.size) {
        val line = split(lines[pos])
        when (line[0]) {
            "inc" -> map[line[1]] = map.getValue(line[1]) + 1
            "dec" -> map[line[1]] = map.getValue(line[1]) - 1
            "cpy" -> map[line[2]] = decode(map, line[1])
            else -> {
                val decode = decode(map, line[1])
                pos += if (decode == 0) 0 else decode(map, line[2]) - 1
            }
        }
        pos += 1
    }
    return map.getValue("a")
}

private fun decode(map: Map<String, Int>, decode: String): Int {
    val num = matchNumbers(decode)
    return when {
        num.isEmpty() -> map.getValue(decode)
        else -> num.first()
    }
}
