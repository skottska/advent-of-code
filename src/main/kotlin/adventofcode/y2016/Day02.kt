package adventofcode.y2016 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val func = { start: Pair<Int, Int>, digitMap: Map<Pair<Int, Int>, Char>, part: Int ->
        readFile("src/main/resources/y2016/day02.txt")
            .map { it.fold(start) { total, i -> move(total, i, digitMap) } }
            .map { digitMap.getValue(it) }
            .toCharArray().concatToString()
            .also { println("part$part=$it") }
    }

    val digitMapPart1 = mapOf(
        Pair(0, 2) to '1', Pair(1, 2) to '2', Pair(2, 2) to '3',
        Pair(0, 1) to '4', Pair(1, 1) to '5', Pair(2, 1) to '6',
        Pair(0, 0) to '7', Pair(1, 0) to '8', Pair(2, 0) to '9'
    )
    func(Pair(1, 1), digitMapPart1, 1)

    val digitMapPart2 = mapOf(
        Pair(2, 4) to '1', Pair(1, 3) to '2', Pair(2, 3) to '3',
        Pair(3, 3) to '4', Pair(0, 2) to '5', Pair(1, 2) to '6',
        Pair(2, 2) to '7', Pair(3, 2) to '8', Pair(4, 2) to '9',
        Pair(1, 1) to 'A', Pair(2, 1) to 'B', Pair(3, 1) to 'C', Pair(2, 0) to 'D'
    )
    func(Pair(0, 2), digitMapPart2, 2)
}

private fun move(pos: Pair<Int, Int>, c: Char, digitMap: Map<Pair<Int, Int>, Char>) = when (c) {
    'U' -> pos.copy(second = pos.second + 1)
    'D' -> pos.copy(second = pos.second - 1)
    'R' -> pos.copy(first = pos.first + 1)
    else -> pos.copy(first = pos.first - 1)
}.let {
    if (digitMap.contains(it)) it else pos
}
