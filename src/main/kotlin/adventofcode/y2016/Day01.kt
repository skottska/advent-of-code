package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile
import kotlin.math.abs

fun main() {
    val facing = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))
    var curFacing = 0
    var pos = Pair(0, 0)
    val visited = mutableSetOf(pos)
    var firstDuplicate: Pair<Int, Int>? = null
    matches(readFile("src/main/resources/y2016/day01.txt")[0], "[L|R][0-9]+").forEach {
        if (it[0] == 'R') { if (++curFacing == facing.size) curFacing = 0 }
        else { if (--curFacing == -1) curFacing = facing.size - 1 }

        val amount = it.substring(1).toInt()
        (1..amount).forEach { _ ->
            pos = Pair(pos.first + 1 * facing[curFacing].first, pos.second + 1 * facing[curFacing].second)
            if (visited.contains(pos) && firstDuplicate == null) firstDuplicate = pos
            visited.add(pos)
        }
    }
    println("part1=" + (abs(pos.first) + abs(pos.second)))
    println("part2=" + (abs(firstDuplicate?.first ?: 0) + abs(firstDuplicate?.second ?: 0)))
}
