package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val matches = readFile("src/main/resources/y2023/day04.txt").map { line ->
        val nums = matchNumbers(line)
        val winningNumbers = nums.subList(1, 11)
        val scratchcard = nums.subList(11, nums.size)
        scratchcard.count { it in winningNumbers }
    }

    val totalPoints = matches.map { (1..it).fold(0) { total, _ -> if (total == 0) 1 else total * 2 } }.sum()
    println("part1=$totalPoints")

    val extras = mutableMapOf<Int, Int>()
    val totalCards = matches.mapIndexed { index: Int, match: Int ->
        val extra = extras.getOrDefault(index, 0)
        (1..match).forEach { extras[index + it] = extras.getOrDefault(index + it, 0) + 1 + extra }
        (extra + 1)
    }.sum()
    println("part2=$totalCards")
}
