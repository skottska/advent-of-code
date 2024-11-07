package adventofcode.y2021 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.rangeOverlaps
import adventofcode.readFile

fun main() {
    val line = matchNumbers(readFile("src/main/resources/y2021/day17.txt").first())
    val xRange = line[0]..line[1]
    val yRange = line[2]..line[3]

    val xOptions = (1..xRange.last).mapNotNull { x -> optionForXRange(x, xRange)?.let { x to it } }
    val yOptions = (-1_000..1_000).mapNotNull { y -> optionForYRange(y, yRange)?.let { y to it } }

    val permutations = xOptions.flatMap { x ->
        yOptions.mapNotNull { y ->
            if (rangeOverlaps(x.second, y.second)) x.first to y.first else null
        }
    }
    val maxY = permutations.map { it.second }.max()
    println("part1=" + (1..maxY).sum())
    println("part2=" + permutations.size)
}

private fun optionForXRange(x: Int, xRange: IntRange): IntRange? {
    var cur = x
    var total = 0
    var range: IntRange? = null
    while (cur != 0 && total <= xRange.last) {
        total += cur--
        if (total in xRange) range = range?.let { IntRange(it.first, x - cur) } ?: IntRange(x - cur, x - cur)
    }
    return if (cur == 0 && range != null) IntRange(range.first, Int.MAX_VALUE) else range
}

private fun optionForYRange(y: Int, yRange: IntRange): IntRange? {
    var cur = y
    var total = 0
    var range: IntRange? = null
    while (total >= yRange.first) {
        total += cur--
        if (total in yRange) range = range?.let { IntRange(it.first, y - cur) } ?: IntRange(y - cur, y - cur)
    }
    return range
}
