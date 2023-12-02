package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split
import java.lang.IllegalArgumentException

fun main() {
    val lines = readFile("src/main/resources/y2016/day12.txt")

    val insts = lines.map { line ->
        val split = split(line)
        when (split[0]) {
            "inc" -> Inc(split[1])
            "dec" -> Dec(split[1])
            "cpy" -> {
                val nums = matchNumbers(split[1])
                if (nums.isEmpty()) CpyRegister(split[1], split[2])
                else CpyValue(nums.first(), split[2])
            }
            "jnz" -> {
                val nums = matchNumbers(split[1])
                if (nums.isEmpty()) JnzRegisterValue(split[1], matchNumbers(split[2]).first())
                else JnzValueValue(nums.first(), matchNumbers(split[2]).first())
            }
            else -> throw IllegalArgumentException("Unknown $line")
        }
    }
    println("part1=" + iterate(insts, mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)))
    println("part2=" + iterate(insts, mutableMapOf("a" to 0, "b" to 0, "c" to 1, "d" to 0)))
}

private fun iterate(lines: List<Instruction>, map: MutableMap<String, Int>): Int {
    var pos = 0
    while (pos < lines.size) {
        when (val inst = lines[pos]) {
            is Inc -> map[inst.register] = map.getValue(inst.register) + 1
            is Dec -> map[inst.register] = map.getValue(inst.register) - 1
            is CpyValue -> map[inst.to] = inst.value
            is CpyRegister -> map[inst.to] = map.getValue(inst.from)
            is JnzValueValue -> pos += if (inst.value == 0) 0 else inst.jump - 1
            is JnzRegisterValue -> pos += if (map.getValue(inst.from) == 0) 0 else inst.jump - 1
            else -> throw UnsupportedOperationException("Can't handle $inst")
        }
        pos += 1
    }
    return map.getValue("a")
}
