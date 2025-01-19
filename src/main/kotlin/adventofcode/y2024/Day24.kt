package adventofcode.y2024 // ktlint-disable filename

import adventofcode.binaryToInt
import adventofcode.concat
import adventofcode.intToBinary
import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val gap = lines.indexOfFirst { it.isEmpty() }
    val wireVals = lines.subList(0, gap).associate { it.substring(0, 3) to matchNumbers(it).last() }
    val gates = lines.drop(gap + 1).map { line -> split(line).let { Gate(it[0], it[2], it[4], it[1]) } }
    val zs = gates.map { it.out }.filter { it.startsWith("z") }.sorted().reversed()
    val part1WireVals = wireVals.toMutableMap()
    while (zs.any { it !in part1WireVals }) {
        gates.forEach { g -> g.output(part1WireVals)?.let { part1WireVals[it.first] = it.second } }
    }
    println("part1=" + zs.map { "" + part1WireVals.getValue(it) }.concat().binaryToInt())

    println(wireToLong(part1WireVals, "x"))
    println(wireToLong(part1WireVals, "y"))
    val shouldBe = wireToLong(part1WireVals, "x") + wireToLong(part1WireVals, "y")
    println("$shouldBe SHOULD BE")
    println(zs.map { "" + part1WireVals.getValue(it) }.concat().binaryToInt()+" BUT IS")
    println(shouldBe.toString().intToBinary()+" SHOULD BE")
    println(zs.map { "" + part1WireVals.getValue(it) }.concat()+" BUT IS")
}

private fun wireToLong(wireVals: Map<String, Int>, prefix: String): Long {
    val wires = wireVals.keys.filter { it.startsWith(prefix) }.sorted().reversed()
    return wires.map { "" + wireVals.getValue(it) }.concat().binaryToInt().toLong()
}

private data class Gate(val a: String, val b: String, val out: String, val type: String) {
    fun output(wireVals: Map<String, Int>): Pair<String, Int>? {
        val aVal = wireVals[a] ?: return null
        val bVal = wireVals[b] ?: return null
        return out to when (type) {
            "AND" -> if (aVal == 1 && bVal == 1) 1 else 0
            "OR" -> if (aVal + bVal > 0) 1 else 0
            else -> if (aVal + bVal == 1) 1 else 0
        }
    }
}
