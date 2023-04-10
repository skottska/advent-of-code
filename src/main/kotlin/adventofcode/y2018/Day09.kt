package adventofcode.y2018 // ktlint-disable filename

import adventofcode.LinkedNode
import adventofcode.matchNumbers
import adventofcode.readFile
import java.math.BigInteger
import java.util.*

fun main() {
    val line = matchNumbers(readFile("src/main/resources/y2018/day09.txt")[0])
    println("part1=" + marbleRun(players = line.first(), numMarbles = line.last()))
    println("part2=" + marbleRun(players = line.first(), numMarbles = line.last() * 100))
}

private fun marbleRun(players: Int, numMarbles: Int): BigInteger {
    val points = mutableMapOf<Int, BigInteger>()
    var curPlayer = 1
    var curMarble = LinkedNode(0)
    (1..numMarbles).forEach { marble ->
        when {
            marble % 23 == 0 -> {
                val toRemove = curMarble.back(7)
                curMarble = toRemove.forward(1)
                toRemove.remove()
                points[curPlayer] = points.getOrDefault(curPlayer, 0.toBigInteger()) + (marble + toRemove.value).toBigInteger()
            }
            else -> {
                val newMarble = LinkedNode(marble)
                curMarble.forward(1).addAfter(newMarble)
                curMarble = newMarble
            }
        }
        curPlayer = if (curPlayer == players) 1 else curPlayer + 1
    }
    return points.values.max()
}
