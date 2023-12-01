package adventofcode.y2016 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split
import kotlin.math.abs

fun main() {
    val nodes = readFile("src/main/resources/y2016/day22.txt").drop(2).map { line ->
        val split = split(line)
        val coord = matchNumbers(split[0]).let { Coord(it.first(), it.last()) }
        Node(coord, matchNumbers(split[1]).first(), matchNumbers(split[2]).first(), matchNumbers(split[3]).first())
    }
    val viableNodes = nodes.sumOf { a: Node ->
        nodes.count { b: Node -> a.used != 0 && a != b && a.used <= b.avail }
    }
    println("part1=$viableNodes")

    val dataPos = nodes.map { it.coord }.filter { it.col == 0 }.maxBy { it.row }
    val zeroNode = nodes.first { it.used == 0 }
    println("part2=" + iterate(nodes, dataPos, 0, zeroNode))
}

private data class Node(val coord: Coord, val size: Int, val used: Int, val avail: Int)
private val seen: MutableMap<Pair<Coord, Coord>, Int> = mutableMapOf()
private var best = Int.MAX_VALUE

private fun iterate(nodes: List<Node>, dataPos: Coord, moves: Int = 0, zeroNode: Node): Int? {
    if (moves + dataPos.col + dataPos.row >= best) return null
    if (dataPos == Coord(0, 0)) {
        println("best=$moves")
        best = moves
        return moves
    }
    seen[dataPos to zeroNode.coord]?.let { if (it <= moves) return null }
    seen[dataPos to zeroNode.coord] = moves

    val around = zeroNode.coord.around()
    val viableMoves = nodes.filter { a: Node -> a.coord in around && a.used != 0 && a != zeroNode && a.used <= zeroNode.avail }
        .filter { it.coord != dataPos || (zeroNode.coord.col == 0 && zeroNode.coord.row < it.coord.row) }
        .map { it to zeroNode }
        .sortedBy { abs(dataPos.col - it.first.coord.col) + abs(dataPos.row - it.first.coord.row) }

    return viableMoves.mapNotNull { move ->
        val newNodes = nodes.map {
            when (it) {
                move.first -> it.copy(used = 0, avail = it.size)
                move.second -> it.copy(used = it.used + move.first.used, avail = it.avail - move.first.used)
                else -> it
            }
        }
        iterate(
            nodes = newNodes,
            dataPos = if (dataPos == move.first.coord) move.second.coord else dataPos,
            moves = moves + 1,
            zeroNode = newNodes.first { it.coord == move.first.coord },
        )
    }.minOrNull()
}
