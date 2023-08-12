package adventofcode.y2021 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbers
import adventofcode.printCoords
import adventofcode.readFile
import adventofcode.split

fun main() {
    val lines = readFile("src/main/resources/y2021/day13.txt")
    val coords = lines.filter { it.isNotBlank() && !it.startsWith("fold") }.map { matchNumbers(it) }.map { Coord(it.last(), it.first()) }.toSet()
    val folds = lines.filter { it.startsWith("fold") }.map { split(it)[2] }

    println("part1=" + fold(folds.first(), coords).size)
    val folded = folds.fold(coords) { total, fold -> fold(fold, total) }
    println("part2=")
    printCoords(folded) { c: Coord -> if (c in folded) "#" else "." }
}

private fun fold(fold: String, coords: Set<Coord>): Set<Coord> {
    val foldOn = matchNumbers(fold).first()
    return if (fold.first() == 'x') {
        coords.map { if (it.col <= foldOn) it else Coord(it.row, foldOn * 2 - it.col) }.toSet()
    } else {
        coords.map { if (it.row <= foldOn) it else Coord(foldOn * 2 - it.row, it.col) }.toSet()
    }
}
