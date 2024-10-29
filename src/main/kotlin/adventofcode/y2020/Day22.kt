package adventofcode.y2020 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2020/day22.txt")
    val player1 = lines.subList(1, lines.indexOf("")).flatMap { matchNumbers(it) }
    val player2 = lines.drop(lines.indexOf("") + 2).flatMap { matchNumbers(it) }
    iterate(player1, player2, true)
    iterate(player1, player2, false)
}

private fun iterate(player1: List<Int>, player2: List<Int>, isPart1: Boolean) {
    val result = game(player1.toMutableList(), player2.toMutableList(), isPart1)
    println("part" + (if (isPart1) "1" else "2") + "=" + result.second.reversed().mapIndexed { index, i -> (index + 1) * i }.sum())
}

private fun game(player1: MutableList<Int>, player2: MutableList<Int>, isPart1: Boolean): Pair<Boolean, List<Int>> {
    val seenGames = mutableSetOf<Pair<List<Int>, List<Int>>>()
    while (player1.isNotEmpty() && player2.isNotEmpty()) {
        if ((player1 to player2) in seenGames) return true to player1
        seenGames += (player1 to player2)
        val round = round(player1, player2)
        val a = player1.removeAt(0)
        val b = player2.removeAt(0)
        val comparison = if (isPart1) a > b else round
        when {
            comparison -> { player1.add(a); player1.add(b) }
            else -> { player2.add(b); player2.add(a) }
        }
    }
    return if (player1.isEmpty()) false to player2 else true to player1
}

private fun round(player1: List<Int>, player2: List<Int>): Boolean = when {
    player1.first() >= player1.size || player2.first() >= player2.size -> player1.first() > player2.first()
    else -> game(
        player1 = player1.subList(1, player1.first() + 1).toMutableList(),
        player2 = player2.subList(1, player2.first() + 1).toMutableList(),
        isPart1 = false
    ).first
}
