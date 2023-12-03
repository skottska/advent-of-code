package adventofcode.y2023 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val coords = readFile("src/main/resources/y2023/day03.txt").mapIndexed { row, line ->
        line.mapIndexed { col, c ->
            Coord(row, col) to c
        }
    }.flatten().toMap()
    val nextToSymbol = coords.filter { c ->
        c.value.isDigit() && c.key.aroundDiag().any { a ->
            val char = coords.getOrDefault(a, '.')
            !char.isDigit() && char != '.'
        }
    }.map { it.key }

    val numbers = coords.map {
        val numPos = mutableListOf<Coord>()
        var digits = ""
        if (it.value.isDigit() && !coords.getOrDefault(it.key.left(), '.').isDigit()) {
            var cur = it.key
            while (coords.getOrDefault(cur, '.').isDigit()) {
                numPos += cur
                digits += coords.getValue(cur)
                cur = cur.right()
            }
        }
        numPos to matchNumbers(digits).first()
    }.filter { n -> n.first.any { it in nextToSymbol } }

    println("part1=" + numbers.sumOf { it.second })

    val part2 = coords.filter { it.value == '*' }.map { it.key.aroundDiag() }.sumOf { star ->
        val numbersNextTo = numbers.filter { n -> n.first.any { it in star } }.map { it.second }
        if (numbersNextTo.size == 2) numbersNextTo.first() * numbersNextTo.last()
        else 0
    }

    println("part2=$part2")
}
