package adventofcode.y2021 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles
import kotlin.math.min

fun main() {
    val startPos = readFile(MethodHandles.lookup()).map { matchNumbers(it).last() }
    var player1 = Player(0, startPos.first())
    var player2 = Player(0, startPos.last())
    var diceRolls = 0
    var diceNum = 0
    val endScore = 1000
    while (player1.score < endScore && player2.score < endScore) {
        rollDie(diceNum).let {
            diceNum = it.first
            player1 = player1.move(it.second)
            diceRolls += 3
        }
        if (player1.score >= endScore) break
        rollDie(diceNum).let {
            diceNum = it.first
            player2 = player2.move(it.second)
            diceRolls += 3
        }
    }
    println("part1="+(diceRolls * min(player1.score, player2.score)))

    val player1Dirac = diracResult(startPos.first())
    val player2Dirac = diracResult(startPos.last())

    println(player1Dirac)
    println(player2Dirac)
}

private fun diracResult(startPos: Int): Map<Int, Long> {
    var players = listOf(Player(0, startPos) to 1L)
    var moves = 0
    val results = mutableListOf<Pair<Int, Long>>()
    while (players.isNotEmpty()) {
        moves++
        println("moves=$moves")
        val movedPlayers = players.flatMap { player ->
            diracRoll().map { player.first.move(it) to player.second }
        }.groupBy { it.first }.map { it.key to it.value.sumOf { num -> num.second } }
        players = movedPlayers.filter { it.first.score < 21 }.map { it.first to it.second * 27L }
        results += moves to movedPlayers.filter { it.first.score >= 21 }.sumOf { it.second }
    }
    return results.toMap()
}

private fun diracRoll() = (1..3).flatMap { a -> (1..3).flatMap { b -> (1..3).map { c -> a + b + c } } }

private fun rollDie(init: Int): Pair<Int, Int> {
    val one = modPlusOne(init, 1, 100)
    val two = modPlusOne(one, 1, 100)
    val three = modPlusOne(two, 1, 100)
    return three to (one + two + three)
}

private fun modPlusOne(i: Int, inc: Int, mod: Int): Int {
    val temp = (i + inc) % mod
    return if (temp == 0) mod else temp
}

private data class Player(val score: Int, val pos: Int) {
    fun move(i: Int): Player {
        val finalPos = modPlusOne(pos, i, 10)
        return Player(score + finalPos, finalPos)
    }
}
