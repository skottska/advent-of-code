package adventofcode.y2020 // ktlint-disable filename

import adventofcode.LinkedNode
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val line = readFile(MethodHandles.lookup()).first().map { it.digitToInt() }
    part1(line)
    part2(line)
}

private fun part1(line: List<Int>) {
    val cache: MutableMap<Int, LinkedNode<Int>> = mutableMapOf()
    val start = line.map { LinkedNode(it).also { n -> cache[n.value] = n } }.reduce { a, b -> a.addAfter(b); b }.next
    (1..100).fold(start) { total, _ -> iterate(total, 9, cache) }
    var curCup = cache.getValue(1).next
    var part1 = ""
    while (curCup.value != 1) {
        part1 += curCup.value.toString()
        curCup = curCup.next
    }
    println("part1=$part1")
}

private fun part2(line: List<Int>) {
    val cache: MutableMap<Int, LinkedNode<Int>> = mutableMapOf()
    val prefilled = line.map { LinkedNode(it).also { n -> cache[n.value] = n } }.reduce { a, b -> a.addAfter(b); b }
    val start = (10..1_000_000).map { LinkedNode(it).also { n -> cache[n.value] = n } }.fold(prefilled) { total, i -> total.addAfter(i); i }.next
    (1..10_000_000).fold(start) { total, _ -> iterate(total, 1_000_000, cache) }
    val next = cache.getValue(1).next
    val nextnext = next.next
    println("part2=" + (next.value.toLong() * nextnext.value.toLong()))
}

private fun iterate(cups: LinkedNode<Int>, max: Int, cache: Map<Int, LinkedNode<Int>>): LinkedNode<Int> {
    val pickup = (1..3).map { cups.next.also { it.remove() } }
    val destination = cache.getValue(calculateDestination(cups.value, pickup.map { it.value }, max))
    pickup.reversed().forEach { destination.addAfter(it) }
    return cups.next
}

private fun calculateDestination(start: Int, pickup: List<Int>, max: Int): Int {
    var cur = if (start == 1) max else start - 1
    while (cur in pickup) cur = if (cur == 1) max else cur - 1
    return cur
}
