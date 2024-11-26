package adventofcode.y2020 // ktlint-disable filename

import adventofcode.matchPositiveNumbers
import adventofcode.readFile
import adventofcode.split
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    println(
        "part1=" + lines.count { line ->
            val split = split(line)
            val limits = matchPositiveNumbers(split[0])
            val num = split[2].count { it == split[1][0] }
            num >= limits.first() && num <= limits.last()
        }
    )
    println(
        "part2=" + lines.count { line ->
            val split = split(line)
            val limits = matchPositiveNumbers(split[0])
            limits.count { split[2][it - 1] == split[1][0] } == 1
        }
    )
}
