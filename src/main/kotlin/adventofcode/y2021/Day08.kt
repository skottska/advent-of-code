package adventofcode.y2021 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.split

fun main() {
    val lines = readFile("src/main/resources/y2021/day08.txt")
    println(
        "part1=" + lines.sumOf { line ->
            split(line.split('|').last()).sumOf { if (it.length in listOf(2, 3, 4, 7)) 1L else 0L }
        }
    )
    val segmentDisplay = mapOf(
        0 to listOf('a', 'b', 'c', 'e', 'f', 'g'),
        1 to listOf('c', 'f'),
        2 to listOf('a', 'c', 'd', 'e', 'g'),
        3 to listOf('a', 'c', 'd', 'f', 'g'),
        4 to listOf('b', 'c', 'd', 'f'),
        5 to listOf('a', 'b', 'd', 'f', 'g'),
        6 to listOf('a', 'b', 'd', 'e', 'f', 'g'),
        7 to listOf('a', 'c', 'f'),
        8 to listOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        9 to listOf('a', 'b', 'c', 'd', 'f', 'g')
    )
    val res = lines.map { line ->
        val before = split(line.split('|').first()).map { it.toSortedSet().fold("") { total, i -> total + i } }
        val after = split(line.split('|').last()).map { it.toSortedSet().fold("") { total, i -> total + i } }
        val all = (before + after).toSet().associateWith { (0..9).map { it } }
            .map { it.key to segmentDisplay.filter { seg -> seg.value.size == it.key.length }.map { seg -> seg.key } }.toMap()
        val segmentMap = ('a'..'g').associateWith { ('a'..'g').map { it } }
            .fixSegments(segmentDisplay.getValue(1), all.keys)
            .fixSegments(segmentDisplay.getValue(4), all.keys)
            .fixSegments(segmentDisplay.getValue(7), all.keys)

        recurse(segmentDisplay, after, all, segmentMap)
    }
    println("part2=" + res.sumOf { it ?: 0 })
}

private fun Map<Char, List<Char>>.fixSegments(real: List<Char>, all: Set<String>): Map<Char, List<Char>> {
    val match = all.filter { it.length == real.size }
    if (match.isEmpty()) return this
    val charMatch = match.first().map { it }
    val newMap = toMutableMap()
    charMatch.forEach { newMap[it] = newMap.getValue(it).filter { seg -> seg in real } }
    newMap.filter { it.key !in charMatch }.forEach { newMap[it.key] = it.value.filter { seg -> seg !in real } }
    return newMap
}

private fun recurse(segmentDisplay: Map<Int, List<Char>>, after: List<String>, all: Map<String, List<Int>>, segmentMap: Map<Char, List<Char>>): Int? {
    if (after.any { all.getValue(it).isEmpty() } || segmentMap.any { it.value.isEmpty() } || all.any { it.value.isEmpty() }) return null
    if (segmentMap.all { it.value.size == 1 }) return after.map { all.getValue(it).first() }.fold("") { total, i -> total + i }.toInt()
    val key = segmentMap.filter { it.value.size != 1 }.keys.first()
    segmentMap.getValue(key).forEach { value ->
        val newSegmentMap = segmentMap.map { it.key to if (it.key == key) listOf(value) else it.value }.toMap()
        val newAll = all.map { af ->
            af.key to af.value.filter { seg ->
                val posSegs = af.key.map { newSegmentMap.getValue(it) }.flatten().toSet()
                val definedSegs = newSegmentMap.filter { it.value.size == 1 }.map { it.key to it.value.first() }.filter { it.first in af.key }.map { it.second }
                segmentDisplay.getValue(seg).all { it in posSegs } && definedSegs.all { it in segmentDisplay.getValue(seg) }
            }
        }.toMap()
        recurse(segmentDisplay, after, newAll, newSegmentMap)?.let { return it }
    }
    return null
}
