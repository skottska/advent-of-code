package adventofcode.y2023 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split

fun main() {
    val hands = readFile("src/main/resources/y2023/day07.txt").map { line -> split(line).let { it.first() to matchNumbers(it.last()).first() } }

    println("part1=" + totalWinnings(hands, false))
    println("part2=" + totalWinnings(hands, true))
}

private fun totalWinnings(hands: List<Pair<String, Int>>, wildcard: Boolean) = hands.sortedWith { a, b ->
    val handValueA = handValue(a.first, wildcard)
    val handValueB = handValue(b.first, wildcard)
    when (handValueA) {
        handValueB -> equalHandCompareTo(a.first, b.first, wildcard)
        else -> handValueA.compareTo(handValueB)
    }
}.mapIndexed { index, hand -> (index + 1) * hand.second }.sum()

private fun equalHandCompareTo(a: String, b: String, wildcard: Boolean): Int {
    a.indices.forEach {
        val cardValueA = cardValue(a[it], wildcard)
        val cardValueB = cardValue(b[it], wildcard)
        if (cardValueA != cardValueB) return cardValueA.compareTo(cardValueB)
    }
    return 0
}

private fun cardValue(c: Char, wildcard: Boolean): Int = when {
    c.isDigit() -> c.digitToInt()
    c == 'T' -> 10
    c == 'J' -> if (wildcard) 1 else 11
    c == 'Q' -> 12
    c == 'K' -> 13
    else -> 14
}

private fun handValue(s: String, wildcard: Boolean): Int {
    var group = s.groupBy { it }.map { it.key to it.value.size }.toMap()
    if (wildcard) {
        val jokerSize = group.getOrDefault('J', 0)
        if (jokerSize == 5) return 7
        val maxNonJokers = group.filter { it.key != 'J' }.maxBy { it.value }.key
        group = group.filter { it.key != 'J' }.map { if (it.key == maxNonJokers) it.key to it.value + jokerSize else it.key to it.value }.toMap()
    }
    return when {
        group.size == 1 -> 7
        group.containsValue(4) -> 6
        group.size == 2 -> 5
        group.containsValue(3) -> 4
        group.values.count { it == 2 } == 2 -> 3
        group.containsValue(2) -> 2
        else -> 1
    }
}
