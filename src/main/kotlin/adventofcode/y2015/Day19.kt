package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.split

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2015/day19.txt")

    val placements = mutableListOf<Pair<String, String>>()
    var search = ""
    lines.forEach { line ->
        val split = split(line)
        when (split.size) {
            3 -> placements.add(Pair(split[0], split[2]))
            1 -> search = split[0]
            else -> Unit
        }
    }

    val results = mutableSetOf<String>()
    placements.forEach { pair ->
        (0..search.length - pair.first.length).forEach { index ->
            if (search.substring(index, index + pair.first.length) == pair.first) {
                results.add(search.substring(0, index) + pair.second + search.substring(index + pair.first.length))
            }
        }
    }
    println("part1=" + results.size)
    println("part2=" + replaceWords(search, 0, placements.map { Pair(it.second, it.first) }))
}

var seenWords: MutableSet<String> = mutableSetOf()
var bestResult: Int? = null

fun replaceWords(currentWord: String, rounds: Int, replacements: List<Pair<String, String>>): Int? {
    if (currentWord == "e") return rounds
    if (seenWords.contains(currentWord) || bestResult?.let { it >= rounds } == true) return null
    return replacements.mapNotNull { pair ->
        (0..currentWord.length - pair.first.length).mapNotNull { index ->
            if (currentWord.substring(index, index + pair.first.length) == pair.first) {
                val newWord = currentWord.substring(0, index) + pair.second + currentWord.substring(index + pair.first.length)
                replaceWords(newWord, rounds + 1, replacements)
            } else null
        }.minOfOrNull { it }?.also { bestResult = minOf(bestResult ?: it, it) }
    }.minOfOrNull { it }
}
