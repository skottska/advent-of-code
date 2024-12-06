package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.DirectedCoord
import adventofcode.Facing
import adventofcode.mapCoord
import adventofcode.readFile
import java.lang.invoke.MethodHandles
import kotlin.time.measureTime

fun main() {
    val grid = readFile(MethodHandles.lookup()).mapCoord()
    val part1Path = navigateGrid(grid).first
    println("part1="+ (part1Path.size - 1))

    val part2 = grid.filter { it.value == '.' && it.key in part1Path }.map { it.key }.count { navigateGrid(grid + (it to '#')).second }
    println("part2=$part2")
}

private fun navigateGrid(grid: Map<Coord, Char>): Pair<List<Coord>, Boolean> {
    val start = grid.filter { it.value == '^' }.toList().first()
    val obstacles = grid.filter { it.value == '#' }.map { it.key }.toList()
    var curCoord =  DirectedCoord(Facing.UP, start.first)

    val visited = mutableSetOf<DirectedCoord>()
    while (curCoord.coord in grid.keys && !visited.contains(curCoord)) {
        visited += curCoord
        val next = curCoord.forward()
        curCoord = if (next.coord !in obstacles) next else curCoord.right()
    }
    return visited.map { it.coord } to (curCoord.coord in grid.keys)
}
