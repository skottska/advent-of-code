package adventofcode.y2018 // ktlint-disable filename

import adventofcode.matchNumber
import adventofcode.readFile
import java.util.*

fun main() {
    val line = readFile("src/main/resources/y2018/day14.txt")[0]

    val numRecipes = matchNumber(line)
    val recipes = StringBuffer("37")
    var elf1 = 0
    var elf2 = 1
    repeat(numRecipes + 10) {
        val elf1Val = recipes[elf1].digitToInt()
        val elf2Val = recipes[elf2].digitToInt()
        recipes.append((elf1Val + elf2Val).toString())
        elf1 = (elf1 + 1 + elf1Val) % recipes.length
        elf2 = (elf2 + 1 + elf2Val) % recipes.length
    }
    println("part1=" + recipes.substring(numRecipes, numRecipes + 10))

    while (!recipes.contains(line)) {
        repeat(2_000_000) {
            val elf1Val = recipes[elf1].digitToInt()
            val elf2Val = recipes[elf2].digitToInt()
            recipes.append((elf1Val + elf2Val).toString())
            elf1 = (elf1 + 1 + elf1Val) % recipes.length
            elf2 = (elf2 + 1 + elf2Val) % recipes.length
        }
    }
    println("part2=" + recipes.indexOf(line))
}
