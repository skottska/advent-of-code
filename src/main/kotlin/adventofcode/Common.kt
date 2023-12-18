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

fun matchNumber(line: String) = matches(line, NUMBER_REGEX).map { it.toInt() }.first()
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

fun mergeRanges(ranges: List<LongRange>) = ranges.sortedBy { it.first }.fold(listOf<LongRange>()) { total, it ->
    when {
        total.isEmpty() -> listOf(it)
        else -> {
            val last = total.last()
            if (last.last >= it.first - 1) total.dropLast(1) + listOf(LongRange(last.first, max(it.last, last.last)))
            else total + listOf(it)
        }
    }
}

fun anyRange(a: Int, b: Int) = min(a, b)..(max(a, b))
fun anyRange(a: Long, b: Long) = min(a, b)..(max(a, b))
fun anyRange(a: List<Int>) = a.min()..a.max()
data class Coord(val row: Int, val col: Int) {
    fun distance(b: Coord) = abs(row - b.row) + abs(col - b.col)
    fun right() = copy(col = col + 1)
    fun left() = copy(col = col - 1)
    fun down() = copy(row = row + 1)
    fun up() = copy(row = row - 1)
    fun upRight() = up().right()
    fun downRight() = down().right()
    fun upLeft() = up().left()
    fun downLeft() = down().left()
    fun around() = listOf(left(), right(), up(), down())
    fun aroundDiag() = around() + listOf(upRight(), upLeft(), downRight(), downLeft())
}
data class DirectedCoord(val facing: Facing, val coord: Coord) {
    fun left() = copy(facing = facing.left())
    fun right() = copy(facing = facing.right())
    fun forward() = copy(coord = facing.move(coord))
}

enum class Facing(val move: Coord) {
    UP(Coord(-1, 0)), RIGHT(Coord(0, 1)), DOWN(Coord(1, 0)), LEFT(Coord(0, -1));

    fun isLeftTurn(f2: Facing): Boolean = when {
        isHoriz() != f2.isHoriz() -> left() == f2
        else -> throw IllegalArgumentException("Cannot check for left turn on this=$this f2=$f2")
    }

    fun left() = when (this) {
        UP -> LEFT
        RIGHT -> UP
        DOWN -> RIGHT
        LEFT -> DOWN
    }

    fun right() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }

    fun move(c: Coord) = Coord(c.row + move.row, c.col + move.col)
    fun move(c: Coord, numSteps: Int) = Coord(c.row + move.row * numSteps, c.col + move.col * numSteps)

    fun isHoriz() = this in listOf(LEFT, RIGHT)
    fun isVert() = this in listOf(UP, DOWN)
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
fun List<String>.mapCoord() = mapIndexed { row, line -> line.mapIndexed { col, c -> Coord(row, col) to c } }.flatten().toMap()

fun <T> transpose(l: List<List<T>>) = (0 until l.first().size).map { colIndex -> l.map { it[colIndex] } }
fun transposeStrings(l: List<String>) = (0 until l.first().length).map { colIndex -> l.map { it[colIndex] } }.map { it.asString() }
fun <T> rotate(l: List<List<T>>) = transpose(l).map { it.reversed() }

inline fun <T> Iterable<T>.firstIndexed(predicate: (index: Int, T) -> Boolean): T {
    forEachIndexed { index, element ->
        if (predicate(index, element)) return element
    }
    throw NoSuchElementException("Collection contains no element matching the predicate.")
}

fun List<Char>.asString() = fold("") { total, i -> total + i }
fun List<String>.concat() = fold("") { total, i -> total + i }
fun String.sort() = String(toCharArray().apply { sort() })

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

private val primeSet = mutableSetOf(2, 3, 5, 7, 11, 13, 17, 19)
fun primes(max: Int): Set<Int> {
    ((primeSet.max() + 1)..max).forEach { i ->
        if (primeSet.none { i % it == 0 }) primeSet.add(i)
    }
    return primeSet.filter { it <= max }.toSet()
}

fun divisors(i: Int): List<Int> {
    if (i == 1) return emptyList()
    val first = primes(i).first { i % it == 0 }
    return listOf(first) + divisors(i / first)
}

private fun gcd(x: Int, y: Int): Int = if (y == 0) x else gcd(y, x % y)
private fun gcd(x: Long, y: Long): Long = if (y == 0L) x else gcd(y, x % y)

fun gcd(numbers: List<Long>) = numbers.fold(0L) { x, y -> gcd(x, y) }
fun gcd(numbers: List<Int>) = numbers.fold(0) { x, y -> gcd(x, y) }

fun lcm(numbers: List<Long>) = numbers.fold(1L) { x, y -> x * (y / gcd(x, y)) }
fun lcm(numbers: List<Int>) = numbers.fold(1) { x, y -> x * (y / gcd(x, y)) }

/**
 * Implementation of the Chinese Remainder Theorem. Pair(a, b) of form: a is loop, b is the remainder
 */
fun crt(list: List<Pair<Long, Long>>): Long {
    val m = list.fold(1L) { total, i -> total * i.first }
    return list.sumOf { i ->
        val m1 = m / i.first
        val mInverse = modInverse(m1, i.first)
        m1 * mInverse * i.second
    } % m
}

fun modInverse(number: Long, base: Long): Long {
    val a = number % base
    for (x in 1L until base) {
        if ((a * x) % base == 1L) {
            return x
        }
    }
    throw IllegalArgumentException("No inverse of number=$number base=$base")
}

/**
 * Will run the same function n times.
 *
 * If it finds a loop before it reaches n, then it will find the modulus of n by the loop and the offset to the loop's start
 * Then it will run the appropriate remaining number of times to simulate having run n times.
 */
fun <T> runNTimes(func: (T) -> T, init: T, n: Long): T {
    val seen = mutableListOf<T>()
    var cur = init
    var times = 0L
    while (cur !in seen) {
        seen += cur
        cur = func(cur)
        if (++times == n) return cur
    }
    val offset = seen.indexOf(cur)
    val loop = seen.size - offset
    val afterLoop = (n - offset) % loop
    return (1..afterLoop).fold(cur) { total, _ -> func(total) }
}

fun <T> findOffsetAndLoop(func: (T) -> T, init: T): Pair<Int, Int> {
    val seen = mutableListOf<T>()
    var cur = init
    var times = 0L
    while (cur !in seen) {
        seen += cur
        cur = func(cur)
        times++
    }
    val offset = seen.indexOf(cur)
    val loop = seen.size - offset
    return offset to loop
}

/**
 * Finds the numberof coords  within the given coords corners. The coords must be in order that they are connected to one another.
 * Cannot be disjointed (i.e. no holes inside)
 */
fun lagoonSize(corners: List<Coord>): Long {
    val area = abs(
        corners.foldIndexed(0L) { i, total, c ->
            val next = corners[(i + 1) % corners.size]
            total + c.col.toLong() * next.row - c.row.toLong() * next.col
        } / 2
    )
    val perimeter = corners.foldIndexed(0L) { i, total, c ->
        val next = corners[(i + 1) % corners.size]
        total + abs(c.row - next.row) + abs(c.col - next.col)
    }
    return area + 1 + perimeter / 2
}
