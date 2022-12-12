package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val line = readFile("src/main/resources/y2015/day20.txt")[0].toLong()
    deliverPresents(line, 1)
    deliverPresents(line, 2)
}

private fun deliverPresents(lowestHouseNumber: Long, part: Int) {
    var doors: Long = 2
    while (true) {
        val divs = findDivisors(doors).let { d -> if (part == 1) d.sumOf { it * 10 } else d.filter { it * 50 > doors }.sumOf { it * 11 } }
        if (divs >= lowestHouseNumber) {
            println("part$part=$doors"); return
        }
        doors += 1
    }
}

val knownDivisors = mutableMapOf(2L to setOf(2L))
val primes = mutableSetOf(2L)

private fun findDivisors(x: Long) = (findDivisorsInner(x) + 1L + x).also { knownDivisors[x] = it }
private fun findDivisorsInner(x: Long): Set<Long> {
    knownDivisors[x]?.let { return it }
    if (primes.contains(x)) return setOf(x)
    primes.forEach { prime ->
        if (x % prime == 0L) return findDivisorsInner(x / prime).let { res -> res + res.map { it * prime } } + prime
    }
    return emptySet<Long>().also { primes.add(x) }
}
