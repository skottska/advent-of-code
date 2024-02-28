package adventofcode.y2023 // ktlint-disable filename

import adventofcode.Coord3D
import adventofcode.anyRange
import adventofcode.matchNumbers
import adventofcode.readFile
import kotlin.math.max
import kotlin.math.min

fun main() {
    val bricks = readFile("src/main/resources/y2023/day22.txt").map { line ->
        val nums = matchNumbers(line)
        toBrick(Coord3D(nums[0], nums[1], nums[2]), Coord3D(nums[3], nums[4], nums[5]))
    }

    val settled = settle(bricks)
    val unsettled = settled.map { numUnsettled(settled - it) }
    println("part1=" + unsettled.count { it == 0 })
    println("part2=" + unsettled.sum())
}

private fun settle(bricks: List<Brick>): List<Brick> {
    val settled = mutableListOf<Brick>()
    bricks.sortedBy { it.z.first }.forEach { b ->
        settled += b.down(b.gap(settled))
    }
    return settled
}

private fun numUnsettled(bricks: List<Brick>): Int {
    var unsettled = 0
    val settled = mutableListOf<Brick>()
    bricks.sortedBy { it.z.first }.forEach { b ->
        if (b.gap(settled) != 0) unsettled++
        else settled += b
    }
    return unsettled
}

private fun toBrick(start: Coord3D, end: Coord3D): Brick = Brick(
    anyRange(start.x, end.x),
    anyRange(start.y, end.y),
    anyRange(start.z, end.z),
)

private data class Brick(val x: IntRange, val y: IntRange, val z: IntRange) {
    fun down(i: Int = 1) = copy(z = (z.first - i)..(z.last - i))

    fun gap(l: List<Brick>): Int {
        return if (z.first == 1) 0
        else l.mapNotNull { internalGap(it) }.minOrNull() ?: (z.first - 1)
    }

    private fun internalGap(b: Brick): Int? {
        return if (x.overlaps(b.x) && y.overlaps(b.y)) z.first - b.z.last - 1
        else null
    }
}

private fun IntRange.overlaps(b: IntRange) = max(first, b.first) <= min(last, b.last)
