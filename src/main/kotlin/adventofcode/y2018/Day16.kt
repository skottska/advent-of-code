package adventofcode.y2018 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2018/day16.txt")
    val samples = lines.windowed(size = 4, step = 4).mapNotNull { window ->
        if (window.first().startsWith("Before")) Sample(matchNumbers(window[0]), matchNumbers(window[1]), matchNumbers(window[2]))
        else null
    }
    val funcs = listOf(::addr, ::addi, ::muli, ::mulr, ::banr, ::bani, ::borr, ::bori, ::setr, ::seti, ::gtir, ::gtri, ::gtrr, ::eqir, ::eqri, ::eqrr)
    println("part1=" + samples.count { s -> funcs.count { s.after == it(s) } >= 3 })
}

private data class Sample(val before: List<Int>, val opcode: List<Int>, val after: List<Int>)

private fun addr(s: Sample) = operation(s, Int::plus, true)
private fun addi(s: Sample) = operation(s, Int::plus, false)
private fun mulr(s: Sample) = operation(s, Int::times, true)
private fun muli(s: Sample) = operation(s, Int::times, false)
private fun banr(s: Sample) = operation(s, Int::and, true)
private fun bani(s: Sample) = operation(s, Int::and, false)
private fun borr(s: Sample) = operation(s, Int::or, true)
private fun bori(s: Sample) = operation(s, Int::or, false)
private fun setr(s: Sample) = operation(s, { a: Int, _: Int -> a }, true)
private fun seti(s: Sample) = operation(s, { a: Int, _: Int -> a }, false)

private fun greaterThan(a: Int, b: Int) = if (a > b) 1 else 0
private fun gtir(s: Sample) = operationImRe(s, ::greaterThan)
private fun gtri(s: Sample) = operation(s, ::greaterThan, false)
private fun gtrr(s: Sample) = operation(s, ::greaterThan, true)

private fun equalTo(a: Int, b: Int) = if (a == b) 1 else 0
private fun eqir(s: Sample) = operationImRe(s, ::equalTo)
private fun eqri(s: Sample) = operation(s, ::equalTo, false)
private fun eqrr(s: Sample) = operation(s, ::equalTo, true)

private fun operation(s: Sample, func: (Int, Int) -> Int, register: Boolean): List<Int> = s.before.mapIndexed { index, i ->
    when {
        index != s.opcode.last() -> i
        register -> func(s.before[s.opcode[1]], s.before[s.opcode[2]])
        else -> func(s.before[s.opcode[1]], s.opcode[2])
    }
}
private fun operationImRe(s: Sample, func: (Int, Int) -> Int): List<Int> = s.before.mapIndexed { index, i ->
    when {
        index != s.opcode.last() -> i
        else -> func(s.opcode[1], s.before[s.opcode[2]])
    }
}
