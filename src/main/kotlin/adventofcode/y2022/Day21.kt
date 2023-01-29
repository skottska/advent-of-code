package adventofcode.y2022 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.split
import java.lang.IllegalArgumentException

fun main() {
    val lines = readFile("src/main/resources/y2022/day21.txt").map { split(it) }

    var mapValues = lines.filter { it.size == 2 }.associate { it[0].substring(0, 4) to it[1].toLong() }.toMutableMap()
    val mapFormula = lines.filter { it.size != 2 }.associate { it[0].substring(0, 4) to it.subList(1, 4) }.toMutableMap()
    println("part1=" + findRoot("root", mapValues, mapFormula))

    mapValues = lines.filter { it.size == 2 }.associate { it[0].substring(0, 4) to it[1].toLong() }.toMutableMap()
    mapFormula["root"] = mapFormula.getValue("root").let { listOf(it[0], "=", it[2]) }
    println("part2=" + reverseFind("humn", "root", 0, mapValues, mapFormula))

}

private fun containsName(toFind: String, from: String, mapFormula: Map<String, List<String>>): Boolean {
    if (from == toFind) return true
    if (mapFormula[from] == null) return false
    val formula = mapFormula.getValue(from)
    return formula[0] == toFind || formula[2] == toFind || containsName(toFind, formula[0], mapFormula) || containsName(toFind, formula[2], mapFormula)
}

private fun reverseFind(find: String, from: String, curValue: Long, mapValues: MutableMap<String, Long>, mapFormula: Map<String, List<String>>): Long {
    if (find == from) return curValue
    val formula = mapFormula.getValue(from)
    val root1 = formula[0]
    val root2 = formula[2]

    val unknownFirst = containsName(find, root1, mapFormula)
    val unknownName = if (unknownFirst) root1 else root2
    val knownValue = if (unknownFirst) findRoot(root2, mapValues, mapFormula) else findRoot(root1, mapValues, mapFormula)
    val func = {x : Long -> reverseFind(find, unknownName, x, mapValues, mapFormula) }
    return when {
        formula[1] == "-" && unknownFirst -> func(curValue + knownValue)
        formula[1] == "-" && !unknownFirst -> func((curValue - knownValue) * -1)
        formula[1] == "+" -> func(curValue - knownValue)
        formula[1] == "*" -> func(curValue / knownValue)
        formula[1] == "/" && unknownFirst -> func(curValue * knownValue)
        formula[1] == "/" && !unknownFirst -> func(knownValue / curValue)
        formula[1] == "=" -> func(knownValue)
        else -> throw IllegalArgumentException("what is $formula")
    }
}

private fun findRoot(find: String, mapValues: MutableMap<String, Long>, mapFormula: Map<String, List<String>>): Long {
    mapValues[find]?.let { return it }
    val formula = mapFormula.getValue(find)
    val x = findRoot(formula[0], mapValues, mapFormula)
    val y = findRoot(formula[2], mapValues, mapFormula)
    return when (formula[1]) {
        "-" -> x - y
        "+" -> x + y
        "*" -> x * y
        "/" -> x / y
        else -> throw IllegalArgumentException("what is "+formula[1])
    }.also { mapValues[find] = it }
}
