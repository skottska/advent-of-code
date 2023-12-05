package adventofcode.y2017 // ktlint-disable filename

import adventofcode.LinkedNode
import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val input = matchNumbers(readFile("src/main/resources/y2017/day17.txt").first()).first()
    part1(input)
    part2(input)
}

private fun part1(input: Int) {
    var size = 1
    var node = LinkedNode(0)
    (1..2017).forEach { i ->
        node = node.forward(input % size)
        size++
        node.addAfter(LinkedNode(i))
        node = node.next
    }
    println("part1=" + node.next.value)
}

private fun part2(input: Int) {
    var size = 1
    var pos = 0
    var afterZero = 1
    (1..50_000_000).forEach { i ->
        pos = (pos + input) % size + 1
        if (pos == 1) afterZero = i
        size++
    }
    println("part2=$afterZero")
}
