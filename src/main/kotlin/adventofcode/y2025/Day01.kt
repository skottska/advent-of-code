package adventofcode.y2025 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())

    var part1 = 0
    lines.fold(50) { cur, line -> posMod(cur + rotate(line), 100).also { if (it == 0) part1++ } }
    println("part1=$part1")

    var part2 = 0
    lines.fold(50) { cur, line ->
        val initZero = if (cur == 0) -1 else 0
        val grossRotate = (cur + rotate(line)).also { part2 += if (it <= 0) -it / 100 + 1 + initZero else it / 100 }
        posMod(grossRotate, 100)
    }
    println("part2=$part2")
}

private fun rotate(line: String) = matchNumbers(line).first().let { if (line.first() == 'L') -it else it }
private fun posMod(num: Int, mod: Int): Int = (num % mod).let { if (it < 0) it + mod else it }
