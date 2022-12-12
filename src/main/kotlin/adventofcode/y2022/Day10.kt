package adventofcode.y2022 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day10.txt")

    var result = 0
    var sprite = 1
    var index = 0
    lines.forEach { line ->
        if (++index % 40 == 20) result += sprite * index
        val moves = matches(line, "[-0-9]+")
        if (moves.isNotEmpty()) {
            if (++index % 40 == 20) result += sprite * index
            sprite += moves.first().toInt()
        }
    }
    println("part1=$result")

    var screen = ""
    index = 0
    sprite = 1
    lines.forEach { line ->
        screen += if ((sprite..(sprite + 2)).contains(++index % 40)) "#" else "."
        val moves = matches(line, "[-0-9]+")
        if (moves.isNotEmpty()) {
            screen += if ((sprite..(sprite + 2)).contains(++index % 40)) "#" else "."
            sprite += moves.first().toInt()
        }
    }
    println("part2=")
    screen.windowed(40, 40).forEach { println(it) }
}
