package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile

var bucketList = mutableListOf<List<Int>>()

fun main() {
    val lines = readFile("src/main/resources/y2015/day17.txt")
    val buckets = lines.map { it.toInt() }
    println("part1=" + fillBucket(buckets, 150))
    println("part2=" + bucketList.filter { it.size == bucketList.minOf { min -> min.size } }.size)
}

fun fillBucket(buckets: List<Int>, space: Int, soFar: List<Int> = emptyList()): Int {
    if (space == 0) { bucketList.add(soFar); return 1 }
    if (space < 0) return 0
    return buckets.mapIndexed { index, i -> fillBucket(buckets.subList(index + 1, buckets.size), space - i, soFar + i) }.sum()
}
