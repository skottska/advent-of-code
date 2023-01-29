package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.split

val happinessMap = mutableMapOf<Pair<String, String>, Int>()
var seatingPlans = mutableSetOf<Int>()

fun main() {
    val lines = readFile("src/main/resources/y2015/day13.txt")
    val people = mutableSetOf<String>()
    lines.forEach { line ->
        split(line).let {
            val gain = it[3].toInt() * (if (it[2] == "gain") 1 else -1)
            val person2 = it[10].let { to -> to.substring(0, to.length - 1) }
            happinessMap[Pair(it[0], person2)] = gain
            people.add(it[0])
            people.add(person2)
        }
    }
    seatingPlan(emptyList(), people.toList())
    println("part1=" + seatingPlans.max())

    people.forEach { happinessMap[Pair(it, "me")] = 0; happinessMap[Pair("me", it)] = 0 }
    people.add("me")
    seatingPlans.clear()
    seatingPlan(emptyList(), people.toList())
    println("part2=" + seatingPlans.max())
}

fun seatingPlan(soFar: List<String>, left: List<String>) {
    if (left.isEmpty()) seatingPlans.add(totalHappiness(soFar))
    else left.forEach { seatingPlan(soFar + it, left - it) }
}

fun happiness(from: String, to: String) = (happinessMap[Pair(from, to)] ?: 0) + (happinessMap[Pair(to, from)] ?: 0)

fun totalHappiness(list: List<String>): Int {
    var total = happiness(list.first(), list.last())
    list.windowed(2, 1) { window -> total += happiness(window.first(), window.last()) }
    return total
}
