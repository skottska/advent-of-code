package adventofcode.y2025 // ktlint-disable filename

import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    var splits = 0
    lines.fold(setOf(lines.first().indexOf('S'))) { beam, cur ->
        cur.mapIndexed { index, c ->
            if (index in beam && c == '^') {
                splits++
                setOf(index + 1, index - 1)
            } else if (index in beam) setOf(index)
            else emptySet()
        }.flatten().toSet()
    }

    println("part1=$splits")

    val part2 = lines.fold(listOf(lines.first().indexOf('S') to 1L)) { beam, cur ->
        val unc = beam.map { (b, num) ->
            if (cur[b] == '^') listOf(b - 1 to num, b + 1 to num)
            else listOf(b to num)
        }.flatten()
        unc.map { it.first }.toSet().map { b ->
            b to unc.filter { it.first == b }.sumOf { it.second }
        }
    }
    println("part2=" + part2.sumOf { it.second })
}
