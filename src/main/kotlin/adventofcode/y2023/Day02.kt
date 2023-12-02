package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.matches
import adventofcode.readFile

fun main() {
    val games = readFile("src/main/resources/y2023/day02.txt").map { line ->
        Game(matchNumbers(line).first(), matches(line, "\\d+ [a-z]+").map { it.substring(it.indexOf(' ') + 1) to matchNumbers(it).first() })
    }
    val availableCubes = mapOf("red" to 12, "green" to 13, "blue" to 14)
    println("part1=" + games.filter { it.isPossible(availableCubes) }.sumOf { it.id })
    println("part2=" + games.sumOf { it.power() })
}

private data class Game(val id: Int, val cubes: List<Pair<String, Int>>) {
    fun isPossible(availableCubes: Map<String, Int>) = cubes.all { it.second <= availableCubes.getOrDefault(it.first, 0) }
    fun power() = cubes.map { it.first }.distinct().map { col -> cubes.filter { it.first == col }.maxOf { it.second } }.fold(1) { total, i -> total * i }
}
