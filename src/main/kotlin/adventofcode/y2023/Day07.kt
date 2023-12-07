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
        val compareTo = cardValue(a[it], wildcard).compareTo(cardValue(b[it], wildcard))
        if (compareTo != 0) return compareTo
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
    val group = s.groupBy { it }.map { it.key to it.value.size }.toMap().toMutableMap()
    if (wildcard) {
        val jokerSize = group.remove('J') ?: 0
        group.maxByOrNull { it.value }?.let { group.replace(it.key, it.value + jokerSize) } ?: return 7
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
