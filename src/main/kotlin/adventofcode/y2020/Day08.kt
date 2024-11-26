package adventofcode.y2020 // ktlint-disable filename

import adventofcode.readFile
import adventofcode.split
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    println("part1=" + runProgram(lines).second)
    val results = lines.mapIndexedNotNull { index, i ->
        if (i.startsWith("acc")) null
        else {
            val command = (if (i.startsWith("jmp")) "nop" else "jmp") + i.substring(3)
            runProgram(lines.mapIndexed { index2, s -> if (index == index2) command else s })
        }
    }
    println("part2=" + results.first { it.first }.second)
}

private fun runProgram(commands: List<String>): Pair<Boolean, Int> {
    var acc = 0
    var curLine = 0
    val seenLines = mutableSetOf<Int>()
    while (!seenLines.contains(curLine) && curLine in 0 until commands.size) {
        seenLines.add(curLine)
        val line = split(commands[curLine])
        when {
            line.first() == "nop" -> curLine++
            line.first() == "jmp" -> curLine += line.last().toInt()
            else -> { acc += line.last().toInt(); curLine++ }
        }
    }
    return (curLine >= commands.size) to acc
}
