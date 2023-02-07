package adventofcode.y2021 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val fish = matchNumbers(readFile("src/main/resources/y2021/day06.txt")[0]).groupBy { it }.map { it.key to it.value.size.toLong() }
    println("part1=" + spawnFish(fish, 80))
    println("part2=" + spawnFish(fish, 256))
}

private fun spawnFish(inFish: List<Pair<Int, Long>>, days: Int) =
    (1..days).fold(inFish) { fish, _ ->
        fish.map {
            when (it.first) {
                0 -> listOf(6 to it.second, 8 to it.second)
                else -> listOf((it.first - 1) to it.second)
            }
        }.flatten().groupBy { it.first }.map { it.key to it.value.sumOf { v -> v.second } }
    }.sumOf { it.second }
