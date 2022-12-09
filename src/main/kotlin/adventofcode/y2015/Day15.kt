package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2015/day15.txt")
    val cookieProps = lines.map { line -> CookieProp(matches(line, "[-0-9]+").map { it.toInt() }) }
    println("part1=" + maxCookieScore(100, cookieProps))
    println("part2=" + maxCookieScore(100, cookieProps, 500))
}

fun totalScore(counts: List<Int>, props: List<CookieProp>): Int {
    val mixingBowl = MixingBowl()
    for (i in counts.indices) {
        mixingBowl.map[props[i]] = counts[i]
    }
    return mixingBowl.totalScore()
}

fun totalCalories(counts: List<Int>, props: List<CookieProp>): Int {
    val mixingBowl = MixingBowl()
    for (i in counts.indices) {
        mixingBowl.map[props[i]] = counts[i]
    }
    return mixingBowl.totalCalories()
}

fun maxCookieScore(max: Int, props: List<CookieProp>, calorieLimit: Int? = null): Int {
    var maxCookieProp = 0
    val start = mutableListOf(0, 0, 0, max)
    while (start != listOf(max, 0, 0, 0)) {
        if (calorieLimit == null || calorieLimit == totalCalories(start, props)) maxCookieProp = maxOf(maxCookieProp, totalScore(start, props))
        inc(start, max)
        while (start.sumOf { it } != max) inc(start, max)
    }
    return maxCookieProp
}

fun inc(l: MutableList<Int>, max: Int) {
    var bump = true
    for (index in (l.size - 1)downTo 0) {
        if (bump) {
            when {
                l[index] == max -> l[index] = 0
                index != 0 && l[index] >= max - l.first() -> l[index] = 0
                else -> {
                    l[index] = l[index] + 1
                    bump = false
                }
            }
        }
    }
}

data class MixingBowl(
    val map: MutableMap<CookieProp, Int> = mutableMapOf()
) {
    fun totalScore() = total { it.cap } * total { it.dur } * total { it.fla } * total { it.tex }
    fun totalCalories() = total { it.calories }
    private fun total(func: (CookieProp) -> Int) = maxOf(0, map.keys.sumOf { func(it) * (map[it] ?: 0) })
}

data class CookieProp(
    val cap: Int,
    val dur: Int,
    val fla: Int,
    val tex: Int,
    val calories: Int
) {
    constructor(line: List<Int>) : this(line[0], line[1], line[2], line[3], line[4])
}
