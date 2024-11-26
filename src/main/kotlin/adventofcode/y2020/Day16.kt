package adventofcode.y2020 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.matches
import adventofcode.readFile
import adventofcode.split
import adventofcode.transpose
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    val first = lines.indexOfFirst { it.isEmpty() }
    val last = lines.indexOfLast { it.isEmpty() }

    val ticketFields = lines.subList(0, first).map { line ->
        TicketField(
            name = line.substring(0, line.indexOf(':')),
            ranges = split(line).flatMap { matches(it, "[0-9]+-[0-9]+") }.map { range ->
                range.substring(0, range.indexOf('-')).toInt()..range.substring(range.indexOf('-') + 1).toInt()
            }
        )
    }

    val myTicket = matchNumbers(lines[first + 2])
    val nearbyTickets = lines.subList(last + 2, lines.size).map { matchNumbers(it) }

    val errorRate = nearbyTickets.flatten().filter { ticketFields.flatMap { it.ranges }.none { range -> it in range } }
    println("part1=${errorRate.sum()}")

    val validNearbyTickets = nearbyTickets.filter { ticket -> ticket.none { it in errorRate } }

    val transposedTickets = transpose(validNearbyTickets)
    val possibilities = restrictPossibilities(ticketFields.associate { tf ->
        tf.name to myTicket.indices.filter { index ->
            transposedTickets[index].all { tf.contains(it) }
        }
    })

    val departures = possibilities.filter { it.key.startsWith("departure") }.map { myTicket[it.value].toLong() }
    println("part2="+departures.fold(1L) { total, i -> total * i })
}

private fun restrictPossibilities(possibilities: Map<String, List<Int>>): Map<String, Int> {
    if (possibilities.values.all { it.size == 1 }) return possibilities.map { it.key to it.value.first() }.toMap()
    val found = possibilities.filter { it.value.size == 1 }.map { it.value.first() }
    return restrictPossibilities(possibilities.map {
        if (it.value.size == 1) it.key to it.value
        else it.key to it.value.filter { v -> v !in found }
    }.toMap())
}

private data class TicketField(val name: String, val ranges: List<IntRange>) {
    fun contains(i: Int) = ranges.any { i in it }
}
