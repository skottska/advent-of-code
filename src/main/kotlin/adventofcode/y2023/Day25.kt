package adventofcode.y2023 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.split

fun main() {
    val connections = readFile("src/main/resources/y2023/day25.txt").flatMap { line ->
        val split = split(line)
        split.drop(1).map { split[0].dropLast(1) to it }
    }

    val connections2 = readFile("src/main/resources/y2023/day25.txt").flatMap { line ->
        val split = split(line)
        split.drop(1).map { split[0].dropLast(1) to it }.flatMap { listOf(it, it.second to it.first) }
    }.groupBy { it.first }.map { it.key to it.value.map { a -> a.second } }.toMap()

    val numNodes = (connections.map { it.first } + connections.map { it.second }).toSet().size

    // val impossibles = connections.windowed(size = 5) { window -> if (isAllConnected(connections - window.toSet(), numNodes)) window else null }.filterNotNull()
    val impossibles = (4..10).flatMap { size ->
        connections.windowed(size = size) { window ->
            val newMap = connections2.toMutableMap()
            window.forEach {
                newMap[it.first] = newMap.getOrDefault(it.first, emptyList()) - it.second
                newMap[it.second] = newMap.getOrDefault(it.second, emptyList()) - it.first
            }
            if (isAllConnected(newMap, numNodes)) window.map { it } else null
        }.filterNotNull()
    }

    println("impossible=" + impossibles.size)

    // connections.map { c -> c to impossibles.count { c in it } }.sortedBy { it.second }.forEach { println(it) }

    println("" + connections.size)
    var skips = 0
    var isAlls = 0
    connections.indices.forEach { one ->
        println(one)
        ((one + 1) until connections.size).forEach { two ->
            println("skips=$skips isAlls=$isAlls")
            ((two + 1) until connections.size).forEach { three ->
                val list = setOf(connections[one], connections[two], connections[three])
                if (impossibles.any { it.containsAll(list) }) {
                    skips++
                } else {
                    isAlls++
                    val newMap = connections2.toMutableMap()
                    list.forEach {
                        newMap[it.first] = newMap.getOrDefault(it.first, emptyList()) - it.second
                        newMap[it.second] = newMap.getOrDefault(it.second, emptyList()) - it.first
                    }
                    if (!isAllConnected(newMap, numNodes)) {
                        println("" + connections[one] + " " + connections[two] + " " + connections[three])
                        return
                    }
                    /*if (!isAllConnected(connections - list, numNodes)) {
                        println("" + connections[one] + " " + connections[two] + " " + connections[three])
                        return
                    }*/
                }
            }
        }
    }
}

private fun isAllConnected(connections: List<Pair<String, String>>, numNodes: Int): Boolean {
    var cur = listOf(connections.first().first)
    val seen = cur.toSet().toMutableSet()
    var next = (connections.filter { it.first in cur }.map { it.second } + connections.filter { it.second in cur }.map { it.first }).toSet().filter { it !in seen }

    while (next.isNotEmpty()) {
        cur = next
        seen += cur
        next = (connections.filter { it.first in cur }.map { it.second } + connections.filter { it.second in cur }.map { it.first }).toSet().filter { it !in seen }
    }
    return seen.size == numNodes
}

private fun isAllConnected(connections: Map<String, List<String>>, numNodes: Int): Boolean {
    var cur = listOf(connections.keys.first())
    val seen = cur.toSet().toMutableSet()
    var next = cur.flatMap { connections.getValue(it) }.filter { it !in seen }

    while (next.isNotEmpty()) {
        cur = next
        seen += cur
        next = cur.flatMap { connections.getValue(it) }.filter { it !in seen }
    }
    return seen.size == numNodes
}
