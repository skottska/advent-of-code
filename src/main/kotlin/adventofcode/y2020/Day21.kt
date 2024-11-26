package adventofcode.y2020 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val foods = readFile(MethodHandles.lookup()).map { line ->
        val bracket = line.indexOf('(')
        val ingredients = line.substring(0 until bracket - 1).split(" ")
        val allergens = matches(line.substring(bracket), "[a-z]+").filter { it != "contains" }
        ingredients to allergens
    }
    val ingredientFrequency = foods.flatMap { it.first }.groupBy { it }.mapValues { it.value.size }
    val allergens = foods.flatMap { it.second }.toSet()

    var allergensToIngredients = allergens.associateWith { ingredientFrequency.keys }.toMutableMap()
    foods.forEach { food ->
        food.second.forEach { allergen ->
            allergensToIngredients[allergen] = allergensToIngredients.getValue(allergen).filter { it in food.first }.toSet()
        }
    }
    val possibleAllergicIngredients = allergensToIngredients.flatMap { it.value }.toSet()
    println("part1=" + ingredientFrequency.filter { it.key !in possibleAllergicIngredients }.map { it.value }.sum())

    while (allergensToIngredients.values.any { it.size > 1 }) {
        val known = allergensToIngredients.filter { it.value.size == 1 }.flatMap { it.value }
        allergensToIngredients = allergensToIngredients.map {
            it.key to if (it.value.size == 1) it.value else it.value.filter { i -> i !in known }.toSet()
        }.toMap().toMutableMap()
    }
    val part2 = allergensToIngredients.map { it.key to it.value.first() }.sortedBy { it.first }.map { it.second }
        .fold("") { total, i -> "$total,$i" }
        .substring(1)
    println("part2=$part2")
}
