package adventofcode.y2016 // ktlint-disable filename

import adventofcode.lcm
import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val discs = readFile("src/main/resources/y2016/day15.txt").map { line -> matchNumbers(line).let { Disc(it[1], it[3]) } }
        .mapIndexed { index, disc -> disc.move(index + 1) }
    println("part1=" + iterate(discs))
    println("part2=" + iterate(discs + Disc(11, 0).move(discs.size + 1)))
}

private fun iterate(discs: List<Disc>): Int {
    val inPos = discs.filter { it.cur == 0 }.map { it.positions }
    if (inPos.size == discs.size) return 0
    val lcm = lcm(inPos)
    return lcm + iterate(discs.map { it.move(lcm) })
}

private data class Disc(val positions: Int, val cur: Int) {
    fun move(move: Int) = Disc(positions, (cur + move) % positions)
}
