package adventofcode.y2018

fun sampleFuncs() = sampleFuncsNames().map { it.second }
fun sampleFuncsNames() = listOf(
    "addr" to ::addr,
    "addi" to ::addi,
    "muli" to ::muli,
    "mulr" to ::mulr,
    "banr" to ::banr,
    "bani" to ::bani,
    "borr" to ::borr,
    "bori" to ::bori,
    "setr" to ::setr,
    "seti" to ::seti,
    "gtir" to ::gtir,
    "gtri" to ::gtri,
    "gtrr" to ::gtrr,
    "eqir" to ::eqir,
    "eqri" to ::eqri,
    "eqrr" to ::eqrr
)

data class Sample(val before: List<Int>, val opcode: List<Int>, val after: List<Int>)

private fun addr(s: Sample) = operation(s, Int::plus, true)
private fun addi(s: Sample) = operation(s, Int::plus, false)
private fun mulr(s: Sample) = operation(s, Int::times, true)
private fun muli(s: Sample) = operation(s, Int::times, false)
private fun banr(s: Sample) = operation(s, Int::and, true)
private fun bani(s: Sample) = operation(s, Int::and, false)
private fun borr(s: Sample) = operation(s, Int::or, true)
private fun bori(s: Sample) = operation(s, Int::or, false)
private fun setr(s: Sample) = operationSingle(s, true)
private fun seti(s: Sample) = operationImReSingle(s)

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

private fun operationSingle(s: Sample, register: Boolean): List<Int> = s.before.mapIndexed { index, i ->
    when {
        index != s.opcode.last() -> i
        register -> s.before[s.opcode[1]]
        else -> s.before[s.opcode[1]]
    }
}

private fun operationImRe(s: Sample, func: (Int, Int) -> Int): List<Int> = s.before.mapIndexed { index, i ->
    when {
        index != s.opcode.last() -> i
        else -> func(s.opcode[1], s.before[s.opcode[2]])
    }
}

private fun operationImReSingle(s: Sample): List<Int> = s.before.mapIndexed { index, i ->
    when {
        index != s.opcode.last() -> i
        else -> s.opcode[1]
    }
}
