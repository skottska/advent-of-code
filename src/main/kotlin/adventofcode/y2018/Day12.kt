package adventofcode.y2018 // ktlint-disable filename

import adventofcode.asString
import adventofcode.findOffsetAndLoop
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2018/day12.txt")
    val initial = lines[0].let { it.substring(it.indexOf('#'), it.length) }
    val rules = (2 until lines.size).associate { i -> lines[i].take(5) to lines[i][9] }

    val num = 20
    val end = (1..num).fold(pad(initial, 0)) { total, _ -> iterate(total, rules) }
    val part1 = end.first.mapIndexed { index: Int, c: Char -> if (c == '#') index - end.second else 0 }.sum()
    println("part1=$part1")

    val func = { s: String -> iterate(s to 0, rules).first }
    val offsetAndLoop = findOffsetAndLoop(func, pad(initial, 0).first)

    val posAtOffset = (1..offsetAndLoop.first).fold(pad(initial, 0)) { total, _ -> iterate(total, rules) }
    val posAtLoopEnd = (1..offsetAndLoop.second).fold(posAtOffset) { total, _ -> iterate(total, rules) }

    val diff = posAtLoopEnd.second - posAtOffset.second
    val offsetFromZero = posAtOffset.second + (50_000_000_000L - offsetAndLoop.first) / offsetAndLoop.second * diff
    val part2 = posAtLoopEnd.first.mapIndexed { index: Int, c: Char -> if (c == '#') index - offsetFromZero else 0 }.sum()
    println("part2=$part2")
}

private fun pad(s: String, zeroIndex: Int): Pair<String, Int> {
    val firstHash = s.indexOf('#')
    val lastHash = s.lastIndexOf('#')
    return "..." + s.substring(firstHash, lastHash + 1) + "..." to zeroIndex - firstHash + 3
}

private fun iterate(pair: Pair<String, Int>, rules: Map<String, Char>): Pair<String, Int> {
    val result = pair.first.windowed(size = 5).map { rules.getValue(it) }.asString()
    return pad(result, pair.second - 2)
}
