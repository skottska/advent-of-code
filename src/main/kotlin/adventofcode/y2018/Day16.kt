package adventofcode.y2018 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2018/day16.txt")
    val samples = lines.windowed(size = 4, step = 4).mapNotNull { window ->
        if (window.first().startsWith("Before")) Sample(matchNumbers(window[0]), matchNumbers(window[1]), matchNumbers(window[2]))
        else null
    }
    val funcs = sampleFuncs()
    println("part1=" + samples.count { s -> funcs.count { s.after == it(s) } >= 3 })

    var unmatchedSamples = samples.groupBy { it.opcode.first() }
    val unmatchedFuncs = funcs.toMutableList()
    val opcodeFuncs = mutableMapOf<Int, (Sample) -> List<Int>>()
    while (unmatchedSamples.isNotEmpty()) {
        unmatchedSamples = unmatchedSamples.filter { s ->
            val fs = unmatchedFuncs.filter { f -> s.value.any { it.after == f(it) } }
            if (fs.size == 1) {
                opcodeFuncs[s.key] = fs.first()
                unmatchedFuncs.remove(fs.first())
                false
            } else true
        }
    }

    val instructions = lines.drop(lines.lastIndexOf("") + 1).map { matchNumbers(it) }
    val part2 = instructions.fold(listOf(0, 0, 0, 0)) { total, inst -> opcodeFuncs.getValue(inst.first())(Sample(total, inst, emptyList())) }
    println("part2=" + part2.first())
}

