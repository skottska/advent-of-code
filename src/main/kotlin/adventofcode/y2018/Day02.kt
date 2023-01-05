package adventofcode.y2018 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2018/day02.txt")
    val exactlyTwo = lines.count { line -> line.groupBy { it }.any { it.value.size == 2 } }
    val exactlyThree = lines.count { line -> line.groupBy { it }.any { it.value.size == 3 } }
    println("part1=" + (exactlyTwo * exactlyThree))
    lines.flatMap { a ->
        lines.filter { b ->
            a.filterIndexed { index, c -> c == b[index] }.count() == lines.first().length - 1
        }
    }.let { it.first().filterIndexed { index, c -> c == it.last()[index] } }.let { println("part2=$it") }
}
