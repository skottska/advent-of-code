package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.split

val tripMap = mutableMapOf<Pair<String, String>, Int>()
var trips = mutableSetOf<Int>()

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2015/day9.txt")
    val destinations = mutableSetOf<String>()
    lines.forEach { line ->
        split(line).let {
            tripMap[Pair(it[0], it[2])] = it[4].toInt()
            destinations.add(it[0])
            destinations.add(it[2])
        }
    }
    trip(emptyList(), destinations.toList())
    println("part1=" + trips.min())
    println("part1=" + trips.max())
}

fun trip(soFar: List<String>, left: List<String>) {
    if (left.isEmpty()) trips.add(totalDistance(soFar))
    else left.forEach { trip(soFar + it, left - it) }
}

fun distance(from: String, to: String) = tripMap[Pair(from, to)] ?: tripMap[Pair(to, from)] ?: 0

fun totalDistance(list: List<String>): Int {
    var total = 0
    list.windowed(2, 1) { window -> total += distance(window.first(), window.last()) }
    return total
}
