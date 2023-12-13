package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.readFile
import adventofcode.split
import java.lang.IllegalArgumentException
import java.util.*

fun main() {
    val lines = readFile("src/main/resources/y2017/day18.txt")
    part1(lines)

    val queue0 = LinkedList<Long>()
    val queue1 = LinkedList<Long>()
    val program0 = Program(myQueue = queue0, otherQueue = queue1, lines = lines)
    val program1 = Program(myQueue = queue1, otherQueue = queue0, lines = lines, registers = mutableMapOf('p' to 1))
    while (!program0.isStuck() || !program1.isStuck()) {
        program0.command(); program1.command()
    }
    println("part2=" + program1.sent)
}

private class Program(val registers: MutableMap<Char, Long> = mutableMapOf(), val myQueue: Queue<Long>, val otherQueue: Queue<Long>, val lines: List<String>) {
    var pos = 0; var sent = 0

    fun isStuck() = lines[pos].startsWith("rcv") && myQueue.isEmpty()
    fun command() {
        val split = split(lines[pos])
        pos += commonCommands(registers, split) ?: when (split[0]) {
            "snd" -> { otherQueue.add(getVal(registers, split[1])); sent++; 1 }
            "rcv" -> if (myQueue.isEmpty()) 0 else { registers[split[1].first()] = myQueue.remove(); 1 }
            else -> throw IllegalArgumentException("Cannot handle " + lines[pos])
        }
    }
}

private fun commonCommands(registers: MutableMap<Char, Long>, split: List<String>): Int? {
    val valFunc = { i: Int -> getVal(registers, split[i]) }
    val to = split[1].first()
    return when (split[0]) {
        "set" -> { registers[to] = valFunc(2); 1 }
        "add" -> { registers[to] = valFunc(1) + valFunc(2); 1 }
        "mul" -> { registers[to] = valFunc(1) * valFunc(2); 1 }
        "mod" -> { registers[to] = valFunc(1) % valFunc(2); 1 }
        "jgz" -> if (getVal(registers, split[1]) > 0) getVal(registers, split[2]).toInt() else 1
        else -> null
    }
}

private fun part1(lines: List<String>) {
    val registers = mutableMapOf<Char, Long>()
    var pos = 0
    var lastSound: Long? = null
    while (pos < lines.size) {
        val split = split(lines[pos])
        pos += commonCommands(registers, split) ?: when (split[0]) {
            "snd" -> { lastSound = getVal(registers, split[1]); 1 }
            "rcv" -> if (registers[split[1].first()] != 0L) break else 1
            else -> throw IllegalArgumentException("Cannot handle " + lines[pos])
        }
    }
    println("part1=$lastSound")
}

private fun getVal(registers: Map<Char, Long>, s: String): Long = when (s.first().isLetter()) {
    true -> registers.getOrDefault(s.first(), 0)
    false -> matchNumbersLong(s).first()
}
