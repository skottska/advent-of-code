package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2023/day15.txt")[0].split(",")
    println("part1=" + lines.sumOf { hashify(it) })

    val boxes = MutableList<LinkedHashMap<String, Int>>(256) { LinkedHashMap() }
    lines.forEach { line ->
        val label = line.take(line.indexOfFirst { !it.isLetter() })
        val boxNum = hashify(label)
        when (val after = line.drop(label.length)) {
            "-" -> boxes[boxNum].remove(label)
            else -> boxes[boxNum][label] = matchNumbers(after).first()
        }
    }
    val part2 = boxes.flatMapIndexed { boxIndex, box ->
        box.map { it.value }.mapIndexed { slotIndex, focal ->
            (boxIndex + 1) * (slotIndex + 1) * focal
        }
    }.sum()
    println("part2=$part2")
}

private fun hashify(s: String) = s.fold(0) { total, i -> ((total + i.code) * 17) % 256 }
