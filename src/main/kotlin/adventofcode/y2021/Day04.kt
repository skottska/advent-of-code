package adventofcode.y2021 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.transpose

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2021/day04.txt")
    val winningNumbers = matchNumbers(lines[0])
    val boards = (0..(lines.size - 7)/6).map { index -> BingoBoard((1..5).map { matchNumbers(lines[1 + index * 6 + it]) }) }
    val results = boards.map { board ->
        board to winningNumbers.mapIndexedNotNull { index, _ -> winningNumbers.take(index + 1).let { if (board.isWinning(it)) it else null }}.first()
    }
    println("part1="+results.minBy { it.second.size }.let { it.first.nonMatching(it.second).sum() * it.second.last() })
    println("part2="+results.maxBy { it.second.size }.let { it.first.nonMatching(it.second).sum() * it.second.last() })
}

private data class BingoBoard(val numbers: List<List<Int>>) {
    fun isWinning(winningNumbers: List<Int>) = numbers.any { winningNumbers.containsAll(it) } || transpose(numbers).any { winningNumbers.containsAll(it) }
    fun nonMatching(winningNumbers: List<Int>) = numbers.flatten().filter { !winningNumbers.contains(it) }
}

