package adventofcode.y2020 // ktlint-disable filename

import adventofcode.firstIndexed
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { it.toLong() }
    val part1 = lines.windowed(size = 26, step = 1).first { additionMissing(it.dropLast(1), it.last()) }.last()
    println("part1=$part1")

    lines.firstIndexed { i, _ ->
        val total = mutableListOf<Long>()
        var index = i + 1
        while (total.sum() < part1) total += lines[index++]
        if (total.sum() == part1) println("part2=" + (total.sorted().let { it.first() + it.last() }))
        total.sum() == part1
    }
}

private fun additionMissing(base: List<Long>, total: Long) = base.none {
    when (val find = total - it) {
        it -> false
        else -> base.contains(find)
    }
}
