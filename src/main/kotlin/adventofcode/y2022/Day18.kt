package adventofcode.y2022 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import kotlin.math.abs

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day18.txt")
    val coords = lines.map { line -> matchNumbers(line).let { Coord3D(it[0], it[1], it[2]) } }
    val sides = coords.fold(0) { total, it -> total + freeSides(it, coords) }
    println("part1=$sides")

    val totalBubbles = mutableListOf<Coord3D>()
    val zs = coords.map { it.z }
    (zs.min()..zs.max()).forEach { z ->
        val xs = coords.filter { it.z == z }.map { it.x }
        (xs.min()..xs.max()).forEach { x ->
            val ys = coords.filter { it.z == z && it.x == x }.map { it.y }
            if (ys.isNotEmpty()) {
                (ys.min()..ys.max()).forEach { y ->
                    val c = Coord3D(x, y, z)
                    if (!coords.contains(c)) totalBubbles.add(c)
                }
            }
        }
    }

    val insideBubbles = groupBubbles(totalBubbles).filter {
        when (it.size) {
            1 -> it.any { b -> freeSides(b, coords) == 0 }
            2 -> it.all { b -> freeSides(b, coords) == 1 }
            3 -> it.filter { b -> freeSides(b, coords) == 1 }.size == 2
            else -> true
        }
    }

    val bubbleSides = insideBubbles.sumOf { group -> group.sumOf { 6 - freeSides(it, coords) } }
    println("part2=" + (sides - bubbleSides))
}

private fun groupBubbles(bubbles: List<Coord3D>): Set<Set<Coord3D>> {
    val restBubbles = bubbles.toMutableSet()
    val bubbleGroups = mutableSetOf<MutableSet<Coord3D>>()
    while (restBubbles.isNotEmpty()) {
        val bubble = restBubbles.first()
        val bubbleGroup = mutableSetOf(bubble)
        restBubbles.remove(bubble)
        bubbleGroup.addAll(groupBubblesInner(bubble, bubbleGroup, restBubbles))
        restBubbles.removeAll(bubbleGroup)
        bubbleGroups.add(bubbleGroup)
    }
    return bubbleGroups
}

private fun groupBubblesInner(bubble: Coord3D, bubbleGroup: Set<Coord3D>, rest: Set<Coord3D>): Set<Coord3D> {
    val newBubbles = rest.filter { it.isAdjacent(bubble) }
    return newBubbles.fold(bubbleGroup + newBubbles) { total, i -> groupBubblesInner(i, total, rest - total) }
}

private fun freeSides(c: Coord3D, cs: List<Coord3D>) = cs.fold(6) { total, it -> total - if (c.isAdjacent(it)) 1 else 0 }

private data class Coord3D(val x: Int, val y: Int, val z: Int) {
    fun isAdjacent(c: Coord3D) =
        when {
            listOf(x == c.x, y == c.y, z == c.z).filter { it }.size != 2 -> false
            else -> abs((x + y + z) - (c.x + c.y + c.z)) == 1
        }
}
