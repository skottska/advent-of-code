package adventofcode.y2021 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.IllegalArgumentException

fun main() {
    val snailfishes = readFile("src/main/resources/y2021/day18.txt").map { parse(it, 0, 0).first }

    val part1 = snailfishes.reduce { a, b -> reduce(SFPair(a, b.bumpPos(a.maxPos() + 1))) }.magnitude()
    println("part1=$part1")

    val part2 = snailfishes.maxOf { sf ->
        snailfishes.filter { it != sf }.maxOf { reduce(SFPair(sf, it.bumpPos(sf.maxPos() + 1))).magnitude() }
    }
    println("part2=$part2")
}

private fun reduce(sf: Snailfish): Snailfish {
    var oldSf = sf
    var newSf = reduceInternal(oldSf)
    while (oldSf != newSf) {
        oldSf = newSf
        newSf = reduceInternal(oldSf)
    }
    return newSf
}

private fun reduceInternal(sf: Snailfish): Snailfish = when (val find = sf.findExplode(0)) {
    null -> sf.split(false).first
    else -> sf.explode(find)
}

private fun parse(s: String, index: Int, literalPos: Int): Triple<Snailfish, Int, Int> = when (s[index]) {
    '[' -> {
        val a = parse(s, index + 1, literalPos)
        val b = parse(s, a.second, a.third)
        Triple(SFPair(a.first, b.first), b.second + 1, b.third)
    }
    else -> matchNumbers(s.substring(index)).first().let {
        Triple(SFLiteral(it, literalPos), index + it.toString().length + 1, literalPos + 1)
    }
}

private interface Snailfish {
    fun explode(replace: Pair<SFLiteral, SFLiteral>): Snailfish
    fun split(alreadySplit: Boolean): Pair<Snailfish, Boolean>
    fun findExplode(level: Int): Pair<SFLiteral, SFLiteral>?
    fun maxPos(): Int
    fun bumpPos(by: Int): Snailfish
    fun magnitude(): Int
}
private data class SFLiteral(val value: Int, val pos: Int) : Snailfish {
    override fun explode(replace: Pair<SFLiteral, SFLiteral>): Snailfish = when {
        pos == replace.first.pos - 1 -> copy(value = value + replace.first.value)
        pos == replace.second.pos + 1 -> SFLiteral(value + replace.second.value, pos - 1)
        pos > replace.first.pos -> copy(pos = pos - 1)
        else -> this
    }

    override fun split(alreadySplit: Boolean): Pair<Snailfish, Boolean> = when {
        alreadySplit -> copy(pos = pos + 1) to true
        value >= 10 -> SFPair(SFLiteral(value / 2, pos), SFLiteral((value + 1) / 2, pos + 1)) to true
        else -> this to false
    }

    override fun findExplode(level: Int): Pair<SFLiteral, SFLiteral>? = null
    override fun maxPos(): Int = pos
    override fun bumpPos(by: Int): Snailfish = copy(pos = pos + by)
    override fun magnitude(): Int = value

    override fun toString() = "" + value
}

private data class SFPair(val a: Snailfish, val b: Snailfish) : Snailfish {
    override fun findExplode(level: Int): Pair<SFLiteral, SFLiteral>? = when {
        level in 0..3 -> listOf(a, b).filterIsInstance<SFPair>().firstNotNullOfOrNull { it.findExplode(level + 1) }
        a is SFLiteral && b is SFLiteral -> a to b
        else -> throw IllegalArgumentException("I am too high a level $this")
    }

    override fun maxPos(): Int = b.maxPos()
    override fun bumpPos(by: Int): Snailfish = SFPair(a.bumpPos(by), b.bumpPos(by))
    override fun magnitude(): Int = 3 * a.magnitude() + 2 * b.magnitude()

    override fun explode(replace: Pair<SFLiteral, SFLiteral>): Snailfish = when {
        a == replace.first && b == replace.second -> SFLiteral(0, replace.first.pos)
        else -> SFPair(a.explode(replace), b.explode(replace))
    }

    override fun split(alreadySplit: Boolean): Pair<Snailfish, Boolean> {
        val newA = a.split(alreadySplit)
        val newB = b.split(newA.second)
        return SFPair(newA.first, newB.first) to newB.second
    }

    override fun toString() = "[$a,$b]"
}
