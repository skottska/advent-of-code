package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val input = matchNumbers(readFile("src/main/resources/y2016/day19.txt")[0]).first()

    val elves = (1..input).map { it }
    println("part1=" + stealPresents(elves))
    println("part2=" + stealPresentsPart2(elves))
}

private fun stealPresents(elves: List<Int>): Int {
    return when {
        elves.size == 1 -> elves.first()
        elves.size % 2 == 0 -> stealPresents(elves.filterIndexed { index, _ -> index % 2 == 0 })
        else -> stealPresents(elves.filterIndexed { index, _ -> index % 2 == 0 && index != 0 })
    }
}

private fun stealPresentsPart2(elves: List<Int>): Int {
    var count = 0
    var index = 0
    var time = System.currentTimeMillis()
    val elvesReduce = elves.toMutableList()
    var size = elvesReduce.size
    while (size > 1) {
        val facing = (index + (size - size % 2) / 2) % size
        elvesReduce.removeAt(facing); size--
        val newIndex = if (facing > index) index else index - 1
        index = (newIndex + 1) % size
        if (size % 5000 == 0) { // Added because the solution is pretty slow but it will give an answer within 30 mins.
            count++
            val time2 = System.currentTimeMillis()
            println("" + size + " time=" + (time2 - time) + " count=" + count)
            time = time2
        }
    }
    return elvesReduce.first()
}
