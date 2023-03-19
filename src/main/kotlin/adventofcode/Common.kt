package adventofcode

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.max
import kotlin.math.min

fun readFile(fileName: String): List<String> = File(fileName).useLines { it.toList() }

fun split(line: String) = line.trim().split("\\s+".toRegex())

fun matches(line: String, regex: String) = Regex(regex)
    .findAll(line)
    .map { it.groupValues[0] }
    .toList()

fun matchNumbers(line: String) = matches(line, "-?[0-9]+").map { it.toInt() }
fun matchPositiveNumbers(line: String) = matches(line, "[0-9]+").map { it.toInt() }
fun matchNumbersLong(line: String) = matches(line, "-?[0-9]+").map { it.toLong() }

fun md5(input: String) = BigInteger(1, MessageDigest.getInstance("MD5").digest(input.toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun maxOfNull(x: Int?, y: Int?) = when {
    x == null -> y
    y == null -> x
    else -> max(x, y)
}

fun minOfNull(x: Int?, y: Int?) = when {
    x == null -> y
    y == null -> x
    else -> min(x, y)
}

fun anyRange(a: Int, b: Int) = min(a, b)..(max(a, b))
fun anyRange(a: Long, b: Long) = min(a, b)..(max(a, b))
fun anyRange(a: List<Int>) = a.min()..a.max()
data class Coord(val row: Int, val col: Int)

fun <T> transpose(l: List<List<T>>) = (0 until l.first().size).map { colIndex -> l.map { it[colIndex] } }

inline fun <T> Iterable<T>.firstIndexed(predicate: (index: Int, T) -> Boolean): T {
    forEachIndexed { index, element ->
        if (predicate(index, element)) return element
    }
    throw NoSuchElementException("Collection contains no element matching the predicate.")
}

fun List<Char>.string() = fold("") { total, i -> total + i }