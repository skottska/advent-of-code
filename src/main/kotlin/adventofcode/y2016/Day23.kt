package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split
import java.lang.IllegalArgumentException

/**
 * Note: I had to change the text file to add new commands to summarise what was happening in exponentially fewer operations
 */
fun main() {
    val lines = readFile("src/main/resources/y2016/day23.txt")

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
                val nums1 = matchNumbers(split[1])
                val nums2 = matchNumbers(split[2])
                when {
                    nums1.isNotEmpty() && nums2.isNotEmpty() -> JnzValueValue(nums1.first(), nums2.first())
                    nums1.isNotEmpty() -> JnzValueRegister(nums1.first(), split[2])
                    else -> JnzRegisterValue(split[1], nums2.first())
                }
            }
            "tgl" -> Tgl(split[1])
            "nad" -> Nada
            "mov" -> Move(split[1], split[2])
            "mul" -> Mult(split[1], split[2], split[3])
            else -> throw IllegalArgumentException("Unknown $line")
        }
    }
    println("part1=" + iterate(insts.toMutableList(), mutableMapOf("a" to 7, "b" to 0, "c" to 0, "d" to 0)))
    println("part2=" + iterate(insts.toMutableList(), mutableMapOf("a" to 12, "b" to 0, "c" to 0, "d" to 0)))
}

private fun iterate(lines: MutableList<Instruction>, map: MutableMap<String, Int>): Int {
    var pos = 0
    while (pos < lines.size) {
        when (val inst = lines[pos]) {
            is Inc -> map[inst.register] = map.getValue(inst.register) + 1
            is Dec -> map[inst.register] = map.getValue(inst.register) - 1
            is CpyValue -> map[inst.to] = inst.value
            is CpyRegister -> map[inst.to] = map.getValue(inst.from)
            is JnzValueValue -> pos += if (inst.value == 0) 0 else inst.jump - 1
            is JnzRegisterValue -> pos += if (map.getValue(inst.from) == 0) 0 else inst.jump - 1
            is JnzValueRegister -> pos += if (inst.value == 0) 0 else map.getValue(inst.to) - 1
            is Tgl -> {
                val toChange = pos + map.getValue(inst.from)
                if (lines.indices.contains(toChange)) {
                    lines[toChange] = lines[toChange].toggle()
                }
            }
            is Nada -> {}
            is Move -> {
                map[inst.to] = map.getValue(inst.from) + map.getValue(inst.to)
                map[inst.from] = 0
            }
            is Mult -> {
                map[inst.to] = (map.getValue(inst.from) * map.getValue(inst.by)) + map.getValue(inst.to)
                map[inst.from] = 0
            }
            else -> throw UnsupportedOperationException("" + inst)
        }
        pos += 1
    }
    return map.getValue("a")
}

sealed interface Instruction { fun toggle(): Instruction }
data class Inc(val register: String) : Instruction { override fun toggle() = Dec(register) }
data class Dec(val register: String) : Instruction { override fun toggle() = Inc(register) }
data class CpyValue(val value: Int, val to: String) : Instruction { override fun toggle() = JnzValueRegister(value, to) }
data class CpyRegister(val from: String, val to: String) : Instruction { override fun toggle() = JnzRegisterRegister(from, to) }
data class CpyValueValue(val value: Int, val to: Int) : Instruction { override fun toggle() = JnzValueValue(value, to) }
data class CpyRegisterValue(val from: String, val to: Int) : Instruction { override fun toggle() = JnzRegisterValue(from, to) }
data class JnzValueValue(val value: Int, val jump: Int) : Instruction { override fun toggle() = CpyValueValue(value, jump) }
data class JnzRegisterValue(val from: String, val jump: Int) : Instruction { override fun toggle() = CpyRegisterValue(from, jump) }
data class JnzValueRegister(val value: Int, val to: String) : Instruction { override fun toggle() = CpyValue(value, to) }
data class JnzRegisterRegister(val from: String, val to: String) : Instruction { override fun toggle() = CpyRegister(from, to) }
data class Tgl(val from: String) : Instruction { override fun toggle() = Inc(from) }
object Nada : Instruction { override fun toggle(): Instruction { throw UnsupportedOperationException("Can't reverse Nada") } }
data class Move(val from: String, val to: String) : Instruction { override fun toggle(): Instruction { throw UnsupportedOperationException("Can't reverse Move") } }
data class Mult(val from: String, val by: String, val to: String) : Instruction { override fun toggle(): Instruction { throw UnsupportedOperationException("Can't reverse Mult") } }
