package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split

fun main() {
    val discs = readFile("src/main/resources/y2017/day07.txt").associate { line ->
        val split = split(line)
        split[0] to Disc(
            split[0],
            matchNumbers(split[1]).first(),
            (3 until split.size).map { split[it].filter { c -> c != ',' } }
        )
    }
    val allAbove = discs.values.map { it.above }.flatten().toSet()
    val part1 = discs.values.first { !allAbove.contains(it.name) }.name
    println("part1=$part1")

    val totalWeights = calculateTotalWeights(part1, discs)
    val subTowers = discs.values.map {
        it.name to it.above.map { a -> totalWeights.getValue(a) }
    }.filter { it.second.toSet().size > 1 }.minBy { it.second.sum() }

    val misMatchedGrouping = subTowers.second.groupBy { it }.map { it.value }.sortedBy { it.size }
    val misMatchedTower = misMatchedGrouping.first().first()
    val diff = misMatchedTower - misMatchedGrouping.last().first()
    val misMatchedTowerName = discs.getValue(subTowers.first).above.first { totalWeights.getValue(it) == misMatchedTower }
    println("part2=" + (discs.getValue(misMatchedTowerName).weight - diff))
}

private fun calculateTotalWeights(disc: String, discs: Map<String, Disc>, totalWeights: MutableMap<String, Int> = mutableMapOf()): Map<String, Int> {
    val above = discs.getValue(disc).above.sumOf {
        if (!totalWeights.contains(it)) calculateTotalWeights(it, discs, totalWeights)
        totalWeights.getValue(it)
    }
    totalWeights[disc] = discs.getValue(disc).weight + above
    return totalWeights
}

private data class Disc(val name: String, val weight: Int, val above: List<String>)
