package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchNumbersToBigInt
import adventofcode.readFile
import java.math.BigInteger

fun main() {
    val lines = readFile("src/main/resources/y2019/day22.txt")
    println("part1=" + rememberCardPosition(convertShuffle(lines), startPos = 2019L.toBigInteger(), totalCards = 10007L.toBigInteger()))

    val totalCards = 119315717514047L.toBigInteger()
    val iterations = totalCards - 101741582076661L.toBigInteger() - BigInteger.ONE
    val generatedShuffles = generateShuffles(convertShuffle(lines), iterations, totalCards)
    println("part2=" + rememberCardPosition(generatedShuffles, startPos = 2020L.toBigInteger(), totalCards = totalCards))
}

private fun compressShuffles(inShuffles: List<Shuffle>, totalCards: BigInteger): List<Shuffle> {
    var shuffles = inShuffles
    var newShuffles = reorderShuffles(shuffles, totalCards)
    while (shuffles != newShuffles) {
        shuffles = newShuffles
        newShuffles = reorderShuffles(shuffles, totalCards)
    }
    return shuffles
}

private fun generateShuffles(baseShuffles: List<Shuffle>, iterations: BigInteger, totalCards: BigInteger): List<Shuffle> {
    var num = BigInteger.ONE
    var curShuffles = baseShuffles
    val map = mutableMapOf(num to baseShuffles)
    while (num < iterations) {
        num *= BigInteger.TWO
        curShuffles = compressShuffles(curShuffles + curShuffles, totalCards)
        map[num] = curShuffles
    }

    val keys = map.keys.sorted()
    var iterationsLeft = iterations
    var result = emptyList<Shuffle>()
    while (iterationsLeft > BigInteger.ZERO) {
        val highestUnder = keys.last { it <= iterationsLeft }
        result = compressShuffles(result + map.getValue(highestUnder), totalCards)
        iterationsLeft -= highestUnder
    }
    return result
}

private fun rememberCardPosition(shuffles: List<Shuffle>, startPos: BigInteger, totalCards: BigInteger): BigInteger =
    shuffles.fold(startPos) { pos, shuffle ->
        when (shuffle) {
            is DealNewStack -> totalCards - pos - BigInteger.ONE
            is Cut -> (pos - shuffle.cut) % totalCards
            is DealIncrement -> (pos * shuffle.increment) % totalCards
        }
    }

private fun convertShuffle(lines: List<String>) = lines.map { line ->
    when {
        line == "deal into new stack" -> DealNewStack
        line.startsWith("cut") -> Cut(matchNumbersToBigInt(line).first())
        else -> DealIncrement(matchNumbersToBigInt(line).first())
    }
}

private fun reorderShuffles(lines: List<Shuffle>, totalCards: BigInteger): List<Shuffle> {
    val firstPass = reorderShufflesInternal(lines, totalCards)
    return listOf(firstPass.first()) + reorderShufflesInternal(firstPass.drop(1), totalCards)
}

private fun reorderShufflesInternal(lines: List<Shuffle>, totalCards: BigInteger) = lines.windowed(size = 2, step = 2, partialWindows = true).flatMap { window ->
    if (window.size == 1) {
        window
    } else {
        val first = window.first()
        val last = window.last()
        when {
            first is DealNewStack && last is DealNewStack -> emptyList()
            first is Cut && last is Cut -> listOf(Cut((first.cut + last.cut) % totalCards))
            first is DealIncrement && last is DealIncrement -> listOf(DealIncrement((first.increment * last.increment) % totalCards))
            first is DealNewStack && last is Cut -> listOf(Cut((totalCards - last.cut) % totalCards), first)
            first is Cut && last is DealIncrement -> listOf(last, Cut((first.cut * last.increment) % totalCards))
            first is DealNewStack && last is DealIncrement -> listOf(last, Cut((-last.increment + BigInteger.ONE) % totalCards), first)
            else -> window
        }
    }
}

private sealed interface Shuffle
private object DealNewStack : Shuffle
private data class DealIncrement(val increment: BigInteger) : Shuffle
private data class Cut(val cut: BigInteger) : Shuffle
