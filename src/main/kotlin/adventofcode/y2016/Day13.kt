package adventofcode.y2016 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val input = readFile("src/main/resources/y2016/day13.txt")[0].let { matchNumbers(it).first() }

    val target = Coord(39, 31)
    val start = Coord(1, 1)

    iteratePart1(start, target, input, 0)
    println("part1=$minSteps")

    cache.clear()
    iteratePart2(start, 50, input, 0)
    println("part2=" + cache.size)
}

private fun iteratePart1(cur: Coord, target: Coord, input: Int, steps: Int) {
    if (steps >= minSteps || steps >= cache.getOrDefault(cur, Int.MAX_VALUE)) return
    cache[cur] = steps
    if (cur == target) {
        minSteps = steps
        return
    }
    cur.around().filter { isOpenSpace(it, input) }.forEach {
        iteratePart1(it, target, input, steps + 1)
    }
}

private fun iteratePart2(cur: Coord, maxSteps: Int, input: Int, steps: Int) {
    if (steps > maxSteps || steps >= cache.getOrDefault(cur, Int.MAX_VALUE)) return
    cache[cur] = steps
    cur.around().filter { isOpenSpace(it, input) }.forEach {
        iteratePart2(it, maxSteps, input, steps + 1)
    }
}

private var minSteps = Int.MAX_VALUE
private val cache = mutableMapOf<Coord, Int>()

private fun isOpenSpace(coord: Coord, input: Int): Boolean {
    if (coord.row < 0 || coord.col < 0) return false
    val num = coord.col * coord.col + 3 * coord.col + 2 * coord.col * coord.row + coord.row + coord.row * coord.row + input
    return Integer.toBinaryString(num).count { it == '1' } % 2 == 0
}
