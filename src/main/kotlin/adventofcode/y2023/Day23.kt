package adventofcode.y2023

import adventofcode.Coord
import adventofcode.Node
import adventofcode.mapCoord
import adventofcode.readFile

fun main() {
    println("started")
    // val file = readFile("aoc23.txt")
    val file = readFile("src/main/resources/y2023/day23.txt")
    val grid = file.mapCoord()

    val start = Coord(0, file[0].indexOf('.'))
    val end = Coord(file.size - 1, file.last().indexOf('.'))

    println("part1=" + iterate(start, end, grid, false))
    cache.clear()

    val nodes = grid.filter { it.value != '#' && it.key.around().count { a -> grid.getOrDefault(a, '#') != '#' } > 2 }.map { it.key } + start + end

    val edges = nodes.flatMap { edges(it, grid, listOf(start, end)) }

    // adventofcode.y2023.edges.forEach { println(it)}
    println(edges.filter { it.node2 == start })
    // println(adventofcode.y2023.edges.map { it.distance})

    val part2 = findShortestPath(edges, start, Coord(5, 3))

    println("part2=" + part2.shortestDistance())

    // println("part2="+adventofcode.y2023.iterate(start, end, grid, true))
}

fun edges(c: Coord, grid: Map<Coord, Char>, ends: List<Coord>): List<Edge> {
    val around = safeAround(c, grid)
    return around.mapNotNull { edge(it, c, grid, ends) }
}

fun edge(first: Coord, start: Coord, grid: Map<Coord, Char>, ends: List<Coord>): Edge? {
    val seen = mutableListOf(start, first)
    var around = safeAround(first, grid).filter { it !in seen }
    while (around.size < 2 && around[0] !in ends) {
        if (around.isEmpty()) return null
        val next = around[0]
        seen += next
        around = safeAround(next, grid).filter { it !in seen }
    }
    return Edge(start, around[0], seen.size - 1)
}

fun safeAround(c: Coord, grid: Map<Coord, Char>): List<Coord> = c.around().filter { grid.getOrDefault(it, '#') != '#' }

private val cache = mutableMapOf<Coord, Int>()
fun iterate(cur: Coord, end: Coord, grid: Map<Coord, Char>, part2: Boolean, seen: Set<Coord> = emptySet()): Int? {
    if (!part2) cache[cur]?.let { return it }
    if (cur == end) {
        // println("res="+seen.size)
        // seen.forEach { println(""+it+"=> "+grid.getValue(it))}
    /*printCoords(grid.keys) {
      if (it in seen) "*" else ""+grid.getValue(it)}
    println()*/
        return 0
    }
    val curC = grid.getValue(cur)
    val possible = when {
        curC == '.' || part2 -> cur.around()
        curC == '^' -> listOf(cur.up())
        curC == '>' -> listOf(cur.right())
        curC == '<' -> listOf(cur.left())
        curC == 'v' -> listOf(cur.down())
        else -> emptyList()
    }

    val next: List<Coord> = possible.filter { it !in seen && grid.getOrDefault(it, '#') != '#' }

    val newSeen = seen + cur

    val best = next.mapNotNull { iterate(it, end, grid, part2, newSeen) }.maxOrNull()

    // println(""+cur+" best="+best)
    best?.let {
        val mine = it + 1
        cache[cur] = mine
        return mine
    }
    return best
}

data class Edge(val node1: Node, val node2: Node, val distance: Int)

fun findShortestPath(edges: List<Edge>, source: Node, target: Node): ShortestPathResult {
    // Note: this implementation uses similar variable names as the algorithm given do.
    // We found it more important to align with the algorithm than to use possibly more sensible naming.

    val dist = mutableMapOf<Node, Int>()
    val prev = mutableMapOf<Node, Node?>()
    val q = findDistinctNodes(edges)

    q.forEach { v ->
        dist[v] = Integer.MAX_VALUE
        prev[v] = null
    }
    dist[source] = 0

    while (q.isNotEmpty()) {
        val u = q.minByOrNull { dist[it] ?: 0 }
        q.remove(u)

        if (u == target) {
            break // Found shortest path to target
        }
        edges
            .filter { it.node1 == u }
            .forEach { edge ->
                val v = edge.node2
                val alt = (dist[u] ?: 0) + edge.distance
                if (alt < (dist[v] ?: 0)) {
                    dist[v] = alt
                    prev[v] = u
                }
            }
    }

    return ShortestPathResult(prev, dist, source, target)
}

private fun findDistinctNodes(edges: List<Edge>): MutableSet<Node> {
    val nodes = mutableSetOf<Node>()
    edges.forEach {
        nodes.add(it.node1)
        nodes.add(it.node2)
    }
    return nodes
}

/**
 * Traverse result
 */
class ShortestPathResult(val prev: Map<Node, Node?>, val dist: Map<Node, Int>, val source: Node, val target: Node) {

    fun shortestPath(from: Node = source, to: Node = target, list: List<Node> = emptyList()): List<Node> {
        val last = prev[to] ?: return if (from == to) {
            list + to
        } else {
            emptyList()
        }
        return shortestPath(from, last, list) + to
    }

    fun shortestDistance(): Int? {
        val shortest = dist[target]
        if (shortest == Integer.MAX_VALUE) {
            return null
        }
        return shortest
    }
}
