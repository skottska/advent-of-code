package adventofcode.y2025 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.mapCoord
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val coords = readFile(MethodHandles.lookup()).mapCoord()
    println("part1=" + (ats(coords) - ats(removeWallpaper(coords))))
    println("part2=" + (ats(coords) - ats(iterate(coords))))
}

private fun ats(coords: Map<Coord, Char>) = coords.count { it.value == '@' }

private tailrec fun iterate(coords: Map<Coord, Char>): Map<Coord, Char> {
    val next = removeWallpaper(coords)
    return if (next == coords) coords else iterate(next)
}

private fun removeWallpaper(coords: Map<Coord, Char>): Map<Coord, Char> = coords.mapValues {
    when {
        it.value == '@' && it.key.aroundDiag().count { c -> coords.getOrDefault(c, '.') == '@' } < 4 -> '.'
        else -> it.value
    }
}
