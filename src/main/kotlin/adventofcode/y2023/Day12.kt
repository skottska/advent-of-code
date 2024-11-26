package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { line -> split(line).let { it.first() to matchNumbers(it.last()) } }
    println("part1=" + lines.sumOf { iterate(it.first, it.second) })

    val lines2 = lines.map { (2..5).fold(it) { total, _ -> total.first + "?" + it.first to total.second + it.second } }
    println("part2=" + lines2.sumOf { iterate(it.first, it.second) })
}

private val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

private fun iterate(line: String, record: List<Int>): Long {
    cache[(line to record)]?.let { return it }
    if (record.isEmpty()) return if (line.none { it == '#' }) 1 else 0

    var index = 0
    var found = 0L
    while (line.length - index >= record.sum() + record.size - 1) {
        if (index != 0 && line[index - 1] == '#') break
        if (line.substring(index, index + record.first()).none { it == '.' }) {
            found += when {
                record.size == 1 && index + record.first() == line.length -> 1
                line[index + record.first()] == '#' -> 0
                else -> iterate(line.substring(index + record.first() + 1), record.drop(1))
            }
        }
        index++
    }
    return found.also { cache[line to record] = found }
}
