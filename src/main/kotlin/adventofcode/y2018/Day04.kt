package adventofcode.y2018 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
    val lines = readFile("src/main/resources/y2018/day04.txt").map {
        LocalDateTime.parse(it.substring(1, 17), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) as LocalDateTime to it
    }.sortedBy { it.first }.map { it.second.substring(15, 17).toInt() to it.second.substring(18) }
    val guardSleeps = mutableMapOf<Int, List<Int>>()
    var guard: Int? = null
    var lastSleepBegan: Int? = null
    lines.forEach {
        when {
            it.second.contains("falls asleep") -> lastSleepBegan = it.first
            it.second.contains("wakes up") -> {
                guard?.let { g ->
                    val newSleep = if (it.first > lastSleepBegan!!) lastSleepBegan!!..it.first else (lastSleepBegan!!..59) + (0..it.first)
                    guardSleeps[g] = guardSleeps.getOrDefault(g, emptyList()) + newSleep
                }
            }
            it.second.contains("Guard") -> guard = matchNumbers(it.second).first()
        }
    }
    val func = { g: Map.Entry<Int, List<Int>> -> g.key * g.value.groupBy { it }.maxBy { it.value.size }.key }
    println("part1=" + func(guardSleeps.maxBy { it.value.size }))
    println("part2=" + func(guardSleeps.maxBy { g -> g.value.groupBy { it }.maxOfOrNull { it.value.size } ?: 0 }))
}
