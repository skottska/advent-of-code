package adventofcode.y2022

import adventofcode.readFile
import adventofcode.split

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day02.txt")
    println("part1=" + lines.sumOf { points(it) })
    println("part2=" + lines.sumOf { pointsPart2(it) })
}

fun pointsPart2(line: String): Int {
    val them = convert(split(line)[0])
    val you = convert(split(line)[1], them)

    return resultPoints(them, you) + you.points
}

fun points(line: String): Int {
    val them = convert(split(line)[0])
    val you = convert(split(line)[1])

    return resultPoints(them, you) + you.points
}

fun convert(char: String) = when (char) {
    "A" -> Play.ROCK
    "B" -> Play.PAPER
    "C" -> Play.SCISSORS
    "X" -> Play.ROCK
    "Y" -> Play.PAPER
    "Z" -> Play.SCISSORS
    else -> throw IllegalArgumentException("Don't know what $char is!")
}

fun convert(char: String, them: Play) = when {
    char == "X" && them == Play.ROCK -> Play.SCISSORS
    char == "X" && them == Play.PAPER -> Play.ROCK
    char == "X" && them == Play.SCISSORS -> Play.PAPER
    char == "Y" -> them
    char == "Z" && them == Play.ROCK -> Play.PAPER
    char == "Z" && them == Play.PAPER -> Play.SCISSORS
    char == "Z" && them == Play.SCISSORS -> Play.ROCK
    else -> throw IllegalArgumentException("Don't know what $char is!")
}

fun resultPoints(them: Play, you: Play) = when {
    you == Play.ROCK && them == Play.SCISSORS -> 6
    you == Play.PAPER && them == Play.ROCK -> 6
    you == Play.SCISSORS && them == Play.PAPER -> 6
    you == them -> 3
    else -> 0
}

enum class Play(val points: Int) {
    ROCK(1), PAPER(2), SCISSORS(3);
}
