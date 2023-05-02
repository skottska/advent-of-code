package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.matches
import adventofcode.readFile
import kotlin.math.max

fun main() {
    val lines = readFile("src/main/resources/y2019/day14.txt")
    val reactions = lines.map { line ->
        val reactionFunc = { s: String -> matches(s, "[A-Z]+").first() to matchNumbersLong(s).first() }
        Reaction(
            produces = reactionFunc(line.substring(line.indexOf('='))),
            ingredients = line.substring(0, line.indexOf('=')).split(',').associate { reactionFunc(it) }
        )
    }
    println("part1=" + howManyOresToProduce("FUEL" to 1L, reactions))
    println("part2=" + recurse(1L, 100_000_000L, reactions))
}

private data class Reaction(val produces: Pair<String, Long>, val ingredients: Map<String, Long>)

private fun recurse(min: Long, max: Long, reactions: List<Reaction>): Long {
    val find = 1000000000000L
    val mid = min + (max - min) / 2
    val res = howManyOresToProduce("FUEL" to mid, reactions)
    return when {
        min + 1L == max -> min
        res == find -> mid
        res > find -> recurse(min, mid, reactions)
        else -> recurse(mid, max, reactions)
    }
}

private fun howManyOresToProduce(chemical: Pair<String, Long>, reactions: List<Reaction>, remainder: MutableMap<String, Long> = mutableMapOf()): Long {
    if (chemical.first == "ORE") return chemical.second
    val inRemainder = remainder.getOrDefault(chemical.first, 0L)
    remainder[chemical.first] = max(0L, inRemainder - chemical.second)
    if (inRemainder >= chemical.second) return 0L
    val leftToFind = chemical.second - inRemainder

    val reaction = reactions.first { it.produces.first == chemical.first }
    val ingredientOresFunc = { produce: Long -> reaction.ingredients.map { howManyOresToProduce(it.key to produce * it.value, reactions, remainder) }.sum() }
    val numProduces = leftToFind / reaction.produces.second
    return when {
        leftToFind % reaction.produces.second == 0L -> ingredientOresFunc(numProduces)
        else -> {
            remainder[chemical.first] = (numProduces + 1) * reaction.produces.second - leftToFind
            ingredientOresFunc(numProduces + 1)
        }
    }
}
