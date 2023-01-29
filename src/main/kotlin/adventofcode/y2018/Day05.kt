package adventofcode.y2018 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val line = readFile("src/main/resources/y2018/day05.txt")[0].toList()
    println("part1=" + iterate(line))
    println("part2=" + ('a'..'z').minOf { iterate(line.filter { l -> l.lowercase().first() != it }) })
}

private fun iterate(cs: List<Char>): Int {
    var cur = cs
    while (true) {
        val newCurEven = removePairs(cur)
        val newCurOdd = listOf(newCurEven.first()) + removePairs(newCurEven.drop(1))
        if (newCurOdd == cur) break else cur = newCurOdd
    }
    return cur.size
}

private fun removePairs(cs: List<Char>) = cs.chunked(2).mapNotNull {
    if (it.size == 2 && it.first() != it.last() && it.first().lowercase() == it.last().lowercase()) null else it
}.flatten()

