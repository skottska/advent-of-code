package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.DirectedCoord
import adventofcode.Facing
import adventofcode.mapCoord
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val grid = readFile(MethodHandles.lookup()).mapCoord()

    val start = DirectedCoord(Facing.RIGHT, grid.filter { it.value == 'S' }.map { it.key }.first())
    val end = grid.filter { it.value == 'E' }.map { it.key }.first()

    iterateForBest(start, 0, grid, end)
    println("part1=$best")

    iterateForPath(start, 0, grid, end, listOf(start))
    println("part2="+bestPaths.flatten().toSet().size)
}

private var best: Int? = null
private val bestPaths = mutableListOf<Set<Coord>>()
private val been: MutableMap<DirectedCoord, Int> = mutableMapOf()
private val beenCoord: MutableMap<Coord, Int> = mutableMapOf()

private fun iterateForBest(cur: DirectedCoord, moves: Int, grid: Map<Coord, Char>, end: Coord) {
    best?.let { if (it <= moves) return }
    if (been.getOrDefault(cur, Int.MAX_VALUE) <= moves) return
    if (beenCoord.getOrDefault(cur.coord, Int.MAX_VALUE) <= moves - 2000) return
    been[cur] = moves
    beenCoord[cur.coord] = moves
    if (cur.coord == end) {
        best = moves
        return
    }
    val next = grid.getOrDefault(cur.forward().coord, '#')
    if (next != '#') iterateForBest(cur.forward(), moves + 1, grid, end)
    listOf(cur.left(), cur.right()).map { iterateForBest(it, moves + 1000, grid, end) }
}

private fun iterateForPath(cur: DirectedCoord, moves: Int, grid: Map<Coord, Char>, end: Coord, path: List<DirectedCoord>) {
    best?.let { if (it < moves) return }
    if (been.getOrDefault(cur, Int.MAX_VALUE) < moves) return
    if (cur.coord == end) {
        bestPaths.add(path.map { it.coord }.toSet())
        return
    }
    val next = grid.getOrDefault(cur.forward().coord, '#')
    if (next != '#') iterateForPath(cur.forward(), moves + 1, grid, end, path + cur.forward())
    listOf(cur.left(), cur.right()).forEach { iterateForPath(it, moves + 1000, grid, end, path + it) }
}