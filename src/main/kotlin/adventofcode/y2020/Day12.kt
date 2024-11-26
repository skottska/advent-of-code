package adventofcode.y2020 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.Facing
import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles
import kotlin.math.abs

fun main() {
    val directions = readFile(MethodHandles.lookup()).map { it.first() to matchNumbers(it.drop(1)).first() }
    part1(directions)
    part2(directions)
}

private fun part2(directions: List<Pair<Char, Int>>) {
    var cur = Coord(0, 0)
    var waypoint = Coord(-1, 10)

    directions.forEach { dir ->
        when (dir.first) {
            'F' -> cur = Coord(cur.row + waypoint.row * dir.second, cur.col + waypoint.col * dir.second)
            'N' -> waypoint = Facing.UP.move(waypoint, dir.second)
            'S' -> waypoint = Facing.DOWN.move(waypoint, dir.second)
            'E' -> waypoint = Facing.RIGHT.move(waypoint, dir.second)
            'W' -> waypoint = Facing.LEFT.move(waypoint, dir.second)
            'L' -> waypoint = (1..(dir.second / 90)).fold(waypoint) { total, _ -> Coord(-total.col, total.row) }
            'R' -> waypoint = (1..(dir.second / 90)).fold(waypoint) { total, _ -> Coord(total.col, -total.row) }
        }
    }
    println("par2=" + (abs(cur.col) + abs(cur.row)))
}

private fun part1(directions: List<Pair<Char, Int>>) {
    var cur = Coord(0, 0)
    var facing = Facing.RIGHT

    directions.forEach { dir ->
        when (dir.first) {
            'F' -> cur = facing.move(cur, dir.second)
            'N' -> cur = Facing.UP.move(cur, dir.second)
            'S' -> cur = Facing.DOWN.move(cur, dir.second)
            'E' -> cur = Facing.RIGHT.move(cur, dir.second)
            'W' -> cur = Facing.LEFT.move(cur, dir.second)
            'L' -> facing = (1..(dir.second / 90)).fold(facing) { total, _ -> total.left() }
            'R' -> facing = (1..(dir.second / 90)).fold(facing) { total, _ -> total.right() }
        }
    }
    println("part1=" + (abs(cur.col) + abs(cur.row)))
}
