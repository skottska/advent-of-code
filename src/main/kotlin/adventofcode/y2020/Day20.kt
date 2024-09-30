package adventofcode.y2020 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.asString
import adventofcode.concat
import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.transposeStrings
import kotlin.math.sqrt

fun main() {
    val tiles = readFile("src/main/resources/y2020/day20.txt").windowed(size = 12, step = 12, partialWindows = true).map {
        Tile(id = matchNumbers(it.first()).first(), lines = it.subList(1, 11))
    }

    val width = sqrt(tiles.size.toFloat()).toInt()
    iterate(width, tiles, emptyMap())?.let { result ->
        val part1 = listOf(Coord(0, 0), Coord(0, width - 1), Coord(width - 1, 0), Coord(width - 1, width - 1))
            .map { result.getValue(it).id }
            .fold(1L) { total, i -> total * i }
        println("part1=$part1")

        val hashIndexFunc = { s: String -> s.mapIndexedNotNull { index, c -> if (c == '#') index else null } }
        val monster: List<List<Int>> = listOf("                  # ", "#    ##    ##    ###", " #  #  #  #  #  #   ").map { hashIndexFunc(it) }

        val baseImage: List<String> = (0 until width).flatMap { row ->
            (0 until 8).map { internalRow ->
                (0 until width).map { col ->
                    result.getValue(Coord(row, col)).stripBorders()[internalRow]
                }.concat()
            }
        }

        val monstersFound = possibles(baseImage).sumOf { image ->
            (0 until image.size - 2).sumOf { row ->
                (0 until image.first().length - 20).map { col ->
                    when {
                        !hashIndexFunc(image[row].substring(col, col + 20)).containsAll(monster[0]) -> 0
                        !hashIndexFunc(image[row + 1].substring(col, col + 20)).containsAll(monster[1]) -> 0
                        !hashIndexFunc(image[row + 2].substring(col, col + 20)).containsAll(monster[2]) -> 0
                        else -> 1
                    }
                }.sum()
            }
        }
        val totalHashes = baseImage.sumOf { line -> line.count { it == '#' } }
        val monsterHashes = monster.sumOf { it.count() } * monstersFound
        println("part2=" + (totalHashes - monsterHashes))
    }
}
private fun iterate(width: Int, remainingTiles: List<Tile>, camera: Map<Coord, Tile>): Map<Coord, Tile>? {
    if (remainingTiles.isEmpty()) return camera
    val row = camera.size / width
    val col = camera.size % width
    val coord = Coord(row, col)
    return remainingTiles.flatMap {
        it.fit(
            up = camera[coord.up()]?.lines?.last(),
            left = camera[coord.left()]?.lines?.map { line -> line.last() }?.asString()
        )
    }.firstNotNullOfOrNull { next ->
        iterate(width, remainingTiles.filter { it.id != next.id }, camera + (coord to next))
    }
}

private fun possibles(lines: List<String>): List<List<String>> = listOf(lines, lines.reversed())
    .flatMap { listOf(it, it.map { x -> x.reversed() }) }
    .flatMap { listOf(it, transposeStrings(it)) }

private data class Tile(val id: Int, val lines: List<String>) {
    val possibles = possibles(lines)

    fun fit(up: String?, left: String?): List<Tile> {
        return possibles.filter { rejiggedLines ->
            when {
                up == null && left != null -> left == rejiggedLines.map { it.first() }.asString()
                up != null && left == null -> up == rejiggedLines.first()
                up != null && left != null -> up == rejiggedLines.first() && left == rejiggedLines.map { it.first() }.asString()
                else -> true
            }
        }.map { Tile(id, it) }
    }

    fun stripBorders(): List<String> = lines.drop(1).dropLast(1).map { it.drop(1).dropLast(1) }
}
