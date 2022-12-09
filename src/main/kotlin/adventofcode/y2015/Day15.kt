package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

var maxCookieProp = 0

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2015/day15.txt")
    val cookieProps = lines.map { line -> CookieProp(matches(line, "[-0-9]+").map { it.toInt() }) }
    // mix(MixingBowl(), cookieProps)
    joe(100, cookieProps)
    println(maxCookieProp)
}

fun totalScore(counts: List<Int>, props: List<CookieProp>): Int {
    val mixingBowl = MixingBowl2()
    for (i in counts.indices) {
        mixingBowl.map[props[i]] = counts[i]
    }
    return mixingBowl.totalScore()
}

fun joe(max: Int, props: List<CookieProp>) {
    val start = mutableListOf(0, 0, 0, max)
    while (start != listOf(max, 0, 0, 0)) {
        maxCookieProp = maxOf(maxCookieProp, totalScore(start, props))
        inc(start, max)
        while (start.sumOf { it } != max) inc(start, max)
    }
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

fun mix(soFar: MixingBowl, ingredients: List<CookieProp>) {
    if (soFar.size() == 100) maxCookieProp = maxOf(maxCookieProp, soFar.totalScore())
    else ingredients.forEach {
        val combo = soFar + it
        if (combo.totalScore() > 0) mix(combo, ingredients)
    }
}

data class MixingBowl(
    val map: Map<CookieProp, Int> = mapOf()
) {
    fun size() = map.values.sumOf { it }

    operator fun plus(add: CookieProp) = MixingBowl(map.toMap() + mapOf(add to (map[add] ?: 0) + 1))

    fun totalScore() = total { it.cap } * total { it.dur } * total { it.fla } * total { it.tex }
    private fun total(func: (CookieProp) -> Int) = maxOf(0, map.keys.sumOf { func(it) * (map[it] ?: 0) })
}

data class MixingBowl2(
    val map: MutableMap<CookieProp, Int> = mutableMapOf()
) {
    fun size() = map.values.sumOf { it }

    fun totalScore() = total { it.cap } * total { it.dur } * total { it.fla } * total { it.tex }
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
