package adventofcode.y2022 // ktlint-disable filename

import adventofcode.anyRange
import adventofcode.matchNumbersLong
import adventofcode.readFile
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lines = readFile("src/main/resources/y2022/day15.txt")
    val row = 2000000L
    val maxSearch = 4000000L
    val beacons = lines.map { line -> matchNumbersLong(line).let { Pair(it[2], it[3]) } }.toSet()
    val sensors = lines.map { line -> matchNumbersLong(line).let { Sensor(it[0], it[1], it[2], it[3]) } }
    val ranges = findNonBeacons(sensors, row)
    val beaconsInRange = beacons.filter { it.second == row }.filter { b -> ranges.any { r -> r.contains(b.first) } }.size
    val nonBeacons = ranges.fold(0L) { total, it -> total + (it.last - it.first + 1) } - beaconsInRange
    println("part1=$nonBeacons")
    findMissingBeacon(sensors, maxSearch)
}

private fun findMissingBeacon(sensors: List<Sensor>, maxSearch: Long) {
    (0..maxSearch).forEach { i ->
        val ranges = filterRanges(findNonBeacons(sensors, i), 0L..maxSearch)
        val left = ranges.fold(0L) { total, it -> total + (it.last - it.first + 1) }
        if (left == maxSearch) {
            println("part2=" + ((ranges.first().last + 1) * maxSearch + i))
            return
        }
    }
}

private data class Sensor(val x: Long, val y: Long, val beaconX: Long, val beaconY: Long)

private fun findNonBeacons(sensors: List<Sensor>, row: Long) = mergeRanges(sensors.mapNotNull { sensor -> onRow(row, sensor) })

private fun onRow(row: Long, sensor: Sensor): LongRange? {
    val beaconD = abs(sensor.x - sensor.beaconX) + abs(sensor.y - sensor.beaconY)
    val rowD = abs(sensor.y - row)
    return if (rowD > beaconD) null
    else anyRange(sensor.x + (beaconD - rowD), sensor.x - (beaconD - rowD))
}

private fun mergeRanges(ranges: List<LongRange>) = ranges.sortedBy { it.first }.fold(listOf<LongRange>()) { total, it ->
    when {
        total.isEmpty() -> listOf(it)
        else -> {
            val last = total.last()
            if (last.last >= it.first - 1) total.dropLast(1) + listOf(LongRange(last.first, max(it.last, last.last)))
            else total + listOf(it)
        }
    }
}

private fun filterRanges(ranges: List<LongRange>, filterRange: LongRange) = ranges.mapNotNull {
    if (it.first > filterRange.last || it.last < filterRange.first) null
    else LongRange(max(it.first, filterRange.first), min(it.last, filterRange.last))
}
