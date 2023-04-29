package adventofcode

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun readFile(fileName: String): List<String> = File(fileName).useLines { it.toList() }

fun split(line: String) = line.trim().split("\\s+".toRegex())

fun matches(line: String, regex: String) = Regex(regex)
    .findAll(line)
    .map { it.groupValues[0] }
    .toList()

private const val NUMBER_REGEX = "-?[0-9]+"

fun matchNumbers(line: String) = matches(line, NUMBER_REGEX).map { it.toInt() }
fun matchNumbersToBigInt(line: String) = matches(line, NUMBER_REGEX).map { it.toBigInteger() }
fun matchPositiveNumbers(line: String) = matches(line, "[0-9]+").map { it.toInt() }
fun matchNumbersLong(line: String) = matches(line, NUMBER_REGEX).map { it.toLong() }

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
data class Coord(val row: Int, val col: Int) {
    fun distance(b: Coord) = abs(row - b.row) + abs(col - b.col)
}
fun printCoords(cs: Collection<Coord>, printFunc: (c: Coord) -> String) {
    val rows = cs.map { it.row }
    val cols = cs.map { it.col }
    for (row in rows.min()..rows.max()) {
        for (col in cols.min()..cols.max()) {
            print(printFunc(Coord(row, col)))
        }
        println()
    }
}

fun <T> transpose(l: List<List<T>>) = (0 until l.first().size).map { colIndex -> l.map { it[colIndex] } }

inline fun <T> Iterable<T>.firstIndexed(predicate: (index: Int, T) -> Boolean): T {
    forEachIndexed { index, element ->
        if (predicate(index, element)) return element
    }
    throw NoSuchElementException("Collection contains no element matching the predicate.")
}

fun List<Char>.asString() = fold("") { total, i -> total + i }

class LinkedNode<T>(val value: T) {
    var prev: LinkedNode<T> = this
    var next: LinkedNode<T> = this

    fun forward(places: Int) = (1..places).fold(this) { total, _ -> total.next }
    fun back(places: Int) = (1..places).fold(this) { total, _ -> total.prev }
    fun addAfter(toAdd: LinkedNode<T>) {
        next.prev = toAdd
        toAdd.next = next
        toAdd.prev = this
        next = toAdd
    }
    fun remove() {
        prev.next = next
        next.prev = prev
    }
}

data class Coord3D(val x: Int, val y: Int, val z: Int) {
    fun isAdjacent(c: Coord3D) =
        when {
            listOf(x == c.x, y == c.y, z == c.z).filter { it }.size != 2 -> false
            else -> abs((x + y + z) - (c.x + c.y + c.z)) == 1
        }

    fun absSumOfCoords() = abs(x) + abs(y) + abs(z)
}
