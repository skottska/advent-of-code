package adventofcode.y2022

import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day3.txt")
    println("part1=" + lines.sumOf { calculatePrio(it) })
    var total = 0
    lines.forEachIndexed { index, line -> if (index % 3 == 0) total += prio(inCommon(line, lines[index + 1], lines[index + 2])) }
    println("part2=$total")
}

fun inCommon(elf1: String, elf2: String, elf3: String) = elf1.asSequence().filter { elf2.contains(it) }.filter { elf3.contains(it) }.first()

fun calculatePrio(line: String): Int {
    val firstHalf = line.substring(0, line.length / 2)
    val secondHalf = line.substring(line.length / 2, line.length)

    return firstHalf.toSet().sumOf {
        if (secondHalf.contains(it)) prio(it)
        else 0
    }
}

fun prio(char: Char) = when (char) {
    in 'a'..'z' -> char.code - 'a'.code + 1
    else -> char.code - 'A'.code + 27
}
