package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

var cookieProps: List<CookieProp> = emptyList()

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2015/day15.txt")
    cookieProps = lines.map { line -> CookieProp(matches(line, "[-0-9]+").map { it.toInt() }) }
    println("part1=" + maxCookieScore(100))
    println("part2=" + maxCookieScore(100, 500))
}

fun maxCookieScore(max: Int, calorieLimit: Int? = null): Int {
    var result = 0
    val start = mutableListOf(0, 0, 0, max)
    while (start != listOf(max, 0, 0, 0)) {
        MixingBowl(start).let { if (calorieLimit == null || calorieLimit == it.calories()) result = maxOf(result, it.score()) }
        inc(start, max)
        while (start.sumOf { it } != max) inc(start, max)
    }
    return result
}

fun inc(l: MutableList<Int>, max: Int) {
    for (index in (l.size - 1)downTo 0) {
        when {
            l[index] == max -> l[index] = 0
            index != 0 && l[index] >= max - l.first() -> l[index] = 0
            else -> {
                l[index] = l[index] + 1
                return
            }
        }
    }
}

data class MixingBowl(val map: MutableMap<CookieProp, Int> = mutableMapOf()) {
    constructor(counts: List<Int>) : this() {
        for (i in counts.indices) {
            map[cookieProps[i]] = counts[i]
        }
    }
    fun score() = total { it.cap } * total { it.dur } * total { it.fla } * total { it.tex }
    fun calories() = total { it.calories }
    private fun total(func: (CookieProp) -> Int) = maxOf(0, map.keys.sumOf { func(it) * (map[it] ?: 0) })
}

data class CookieProp(val cap: Int, val dur: Int, val fla: Int, val tex: Int, val calories: Int) {
    constructor(line: List<Int>) : this(line[0], line[1], line[2], line[3], line[4])
}
