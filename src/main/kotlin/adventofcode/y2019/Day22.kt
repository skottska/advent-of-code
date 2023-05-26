package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.matchNumbersLong
import adventofcode.readFile
import java.math.BigDecimal

fun main() {
    val lines = readFile("src/main/resources/y2019/day22.txt")
    println("part1=" + rememberCardPosition(lines, startPos = 2019L, totalCards = 10007L))
    println("part2=" + part2(lines))
    val x: (Long) -> Long
}

private fun part2(lines: List<String>): Long {
    //val seen = mutableSetOf<Long>()
    val remember = rememberCardPosition2(lines, totalCards = 119315717514047L)
    var pos = remember.fold(2020L) { total, func -> func(total) }
    var counter = 1

    while (pos != 2020L) {
        //seen.add(pos)
        pos = remember.fold(pos) { total, func -> func(total) }.also { println("pos="+pos+" become="+it+ " diff="+(pos-it)) }
        counter++
        //if (seen.size % 100_000 == 0) println("size=" + seen.size + " " + (100 * seen.size).toBigDecimal().div(BigDecimal(101741582076661L)))
        if (counter % 100_000 == 0) println("size=" + counter + " " + (100 * counter).toBigDecimal().div(BigDecimal(101741582076661L)))
    }
    println("Dupe at " + counter + " val=" + pos)
    return 0L
}

private fun rememberCardPosition(lines: List<String>, startPos: Long, totalCards: Long): Long =
    lines.fold(startPos) { pos, line ->
        when {
            line == "deal into new stack" -> totalCards - pos - 1
            line.startsWith("cut") -> {
                val cut = matchNumbersLong(line).first().let { if (it < 0) totalCards + it else it }
                if (cut > pos) {
                    totalCards - cut + pos
                } else {
                    pos - cut
                }
            }
            else -> {
                val increment = matchNumbers(line).first()
                pos * increment % totalCards
            }
        }
    }

private fun rememberCardPosition2(lines: List<String>, totalCards: Long): List<(Long) -> Long> =
    lines.fold(emptyList()) { total, line ->
        when {
            line == "deal into new stack" -> total + { pos: Long -> totalCards - pos - 1 }
            line.startsWith("cut") -> {
                val cut = matchNumbersLong(line).first().let { if (it < 0) totalCards + it else it }
                total + { pos: Long -> if (cut > pos) totalCards - cut + pos else pos - cut }
            }
            else -> total + { pos: Long -> pos * matchNumbersLong(line).first() % totalCards }
        }
    }
