package adventofcode.y2020 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2020/day18.txt")
    println("part1=" + lines.sumOf { iteratePart1(it).first })
    println("part2=" + lines.sumOf { solve(iteratePart2(it).first.nodes).num })
}

private interface MathsNode
private class PlusNode : MathsNode
private class MultNode : MathsNode
private class BracketNode(val nodes: List<MathsNode>) : MathsNode
private class NumberNode(val num: Long) : MathsNode

private fun solve(nodes: List<MathsNode>): NumberNode {
    val simplify = nodes.map { if (it is BracketNode) solve(it.nodes) else it }
    return if (simplify.none { it is PlusNode }) simplify.filterIsInstance<NumberNode>().fold(1L) { total, i -> total * i.num }.let { NumberNode(it) }
    else {
        val firstPlus = simplify.indexOfFirst { it is PlusNode }
        val before = simplify[firstPlus - 1] as NumberNode
        val after = simplify[firstPlus + 1] as NumberNode
        val node = NumberNode(before.num + after.num)
        solve(simplify.take(firstPlus - 1) + node + simplify.drop(firstPlus + 2))
    }
}

private fun iteratePart2(line: String, inPos: Int = 0): Pair<BracketNode, Int> {
    val nodes = mutableListOf<MathsNode>()
    var pos = inPos
    while (pos < line.length) {
        when (line[pos]) {
            ' ' -> Unit
            ')' -> return BracketNode(nodes) to (pos + 1)
            '(' -> {
                val result = iteratePart2(line, pos + 1)
                nodes += result.first
                pos = result.second - 1
            }
            '+' -> nodes += PlusNode()
            '*' -> nodes += MultNode()
            else -> nodes += NumberNode(line[pos].digitToInt().toLong())
        }
        pos++
    }
    return BracketNode(nodes) to pos
}

private fun iteratePart1(line: String, inPos: Int = 0): Pair<Long, Int> {
    var total = 0L
    var pos = inPos
    var isPlus = true
    while (pos < line.length) {
        when (line[pos]) {
            ' ' -> Unit
            ')' -> return total to (pos + 1)
            '(' -> {
                val result = iteratePart1(line, pos + 1)
                if (isPlus) total += result.first else total *= result.first
                pos = result.second - 1
            }
            '+' -> isPlus = true
            '*' -> isPlus = false
            else -> {
                val result = line[pos].digitToInt()
                if (isPlus) total += result else total *= result
            }
        }
        pos++
    }
    return total to pos
}
