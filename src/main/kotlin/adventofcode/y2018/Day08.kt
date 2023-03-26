package adventofcode.y2018 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val line = matchNumbers(readFile("src/main/resources/y2018/day08.txt")[0])
    println("part1=" + parseNodes(line).first.metadataSize())
    println("part2=" + parseNodes(line).first.nodeValue())
}

private fun parseNodes(line: List<Int>, index: Int = 0): Pair<Node, Int> {
    val numChildren = line[index]; val numMetadata = line[index + 1]
    val children = (1..numChildren).fold(emptyList<Pair<Node, Int>>()) { total, _ ->
        total + parseNodes(line, if (total.isEmpty()) index + 2 else total.last().second)
    }
    val newIndex = if (children.isEmpty()) index + 2 else children.last().second
    return Node(children.map { it.first }, line.subList(newIndex, newIndex + numMetadata)) to newIndex + numMetadata
}
private data class Node(val children: List<Node>, val metadata: List<Int>) {
    fun metadataSize(): Int = metadata.sum() + children.sumOf { it.metadataSize() }
    fun nodeValue(): Int = if (children.isEmpty()) metadata.sum() else metadata.sumOf { children.getOrNull(it - 1)?.nodeValue() ?: 0 }
}
