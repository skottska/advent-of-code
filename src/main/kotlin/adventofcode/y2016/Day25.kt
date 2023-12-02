package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split
import java.lang.IllegalArgumentException

fun main() {
    val lines = readFile("src/main/resources/y2016/day25.txt")

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
            "out" -> Out(split[1])
            else -> throw IllegalArgumentException("Unknown $line")
        }
    }
    println("part1=" + (1..Int.MAX_VALUE).first { iterate(insts.toMutableList(), mutableMapOf("a" to it, "b" to 0, "c" to 0, "d" to 0)) })
}

private fun iterate(lines: MutableList<Instruction>, map: MutableMap<String, Int>): Boolean {
    var iter = 0
    var prev = 1
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
            is Out -> {
                val out = map.getValue(inst.from)
                when {
                    out !in listOf(0, 1) || out == prev -> return false
                    iter > 1000 -> return true
                    else -> prev = out
                }
                iter++
            }
            else -> throw UnsupportedOperationException("" + inst)
        }
        pos += 1
    }
    return true
}

data class Out(val from: String) : Instruction { override fun toggle(): Instruction { throw UnsupportedOperationException("Can't reverse Mult") } }
