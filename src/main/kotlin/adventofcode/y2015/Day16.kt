package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile
import adventofcode.split

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2015/day16.txt")
    val toFind = mutableMapOf(
        "children:" to eq(3), "cats:" to eq(7), "samoyeds:" to eq(2), "pomeranians:" to eq(3), "akitas:" to eq(0),
        "vizslas:" to eq(0), "goldfish:" to eq(5), "trees:" to eq(3), "cars:" to eq(2), "perfumes:" to eq(1)
    )
    findSue(lines, toFind, 1)
    toFind["cats:"] = gt(7); toFind["trees:"] = gt(3); toFind["pomeranians:"] = lt(3); toFind["goldfish:"] = lt(5)
    findSue(lines, toFind, 2)
}

fun findSue(lines: List<String>, toFind: Map<String, (Int) -> Boolean>, part: Int) {
    lines.forEachIndexed { index, line ->
        val split = split(line)
        val func = { n: Int, v: Int -> compareProp(toFind, split, n, v) }
        if (func(2, 3) && func(4, 5) && func(6, 7)) println("part" + part + "=" + (index + 1))
    }
}

fun eq(b: Int) = { a: Int -> a == b }
fun gt(b: Int) = { a: Int -> a > b }
fun lt(b: Int) = { a: Int -> a < b }

fun compareProp(toFind: Map<String, (Int) -> Boolean>, split: List<String>, name: Int, value: Int) =
    toFind[split[name]]?.let { it(split[value].let { matches(it, "[0-9]+").first().toInt() }) } ?: false
