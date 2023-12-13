package adventofcode.y2017 // ktlint-disable filename

import adventofcode.matchNumbersLong
import adventofcode.primes
import adventofcode.readFile
import adventofcode.split

fun main() {
    part1(readFile("src/main/resources/y2017/day23a.txt"))

    /**
     * Added new commands to avoid the inner loops. The crux is that f is only set to 1 when
     * b is divisable by any number. So first we calculate the number of loops taken and then
     * remove any iteration where b was prime
     */
    val primes = (0..1000).map { 109300 + 17 * it }.intersect(primes(109300 + 17 * 1000)).size
    val loops = part2(readFile("src/main/resources/y2017/day23b.txt"))
    println("part2=" + (loops - primes))
}

private fun part1(lines: List<String>) {
    val registers = mutableMapOf<Char, Long>()
    var pos = 0
    var muls = 0
    while (pos < lines.size) {
        val split = split(lines[pos])
        val valFunc = { i: Int -> getVal(registers, split[i]) }
        val to = split[1].first()
        pos += when (split[0]) {
            "mul" -> { registers[to] = valFunc(1) * valFunc(2); muls++; 1 }
            "set" -> { registers[to] = valFunc(2); 1 }
            "sub" -> { registers[to] = valFunc(1) - valFunc(2); 1 }
            "jnz" -> if (valFunc(1) != 0L) valFunc(2).toInt() else 1
            else -> throw IllegalArgumentException("Cannot handle " + lines[pos])
        }
    }
    println("part1=$muls")
}

private fun part2(lines: List<String>): Long {
    val registers = mutableMapOf('a' to 1L)
    var pos = 0
    while (pos < lines.size) {
        val split = split(lines[pos])
        val valFunc = { i: Int -> getVal(registers, split[i]) }
        val to = split[1].first()
        pos += when (split[0]) {
            "mul" -> { registers[to] = valFunc(1) * valFunc(2); 1 }
            "set" -> { registers[to] = valFunc(2); 1 }
            "sub" -> { registers[to] = valFunc(1) - valFunc(2); 1 }
            "jnz" -> if (valFunc(1) != 0L) valFunc(2).toInt() else 1
            "jgz" -> if (valFunc(1) > 0L) valFunc(2).toInt() else 1
            "mod" -> { registers[to] = valFunc(2) % valFunc(3); 1 }
            "div" -> { registers[to] = valFunc(2) / valFunc(3); 1 }
            "nad" -> 1
            else -> throw IllegalArgumentException("Cannot handle " + lines[pos])
        }
    }
    return registers.getValue('h')
}

private fun getVal(registers: Map<Char, Long>, s: String): Long = when (s.first().isLetter()) {
    true -> registers.getOrDefault(s.first(), 0)
    false -> matchNumbersLong(s).first()
}
