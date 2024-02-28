package adventofcode.y2023 // ktlint-disable filename

import adventofcode.Coord3DLong
import adventofcode.anyRange
import adventofcode.applyToOthers
import adventofcode.matchNumbersLong
import adventofcode.readFile
import kotlin.math.abs

fun main() {
    val hailstones = readFile("src/main/resources/y2023/day24.txt").map { line ->
        matchNumbersLong(line).let { Hailstone(Coord3DLong(it[0], it[1], it[2]), Coord3DLong(it[3], it[4], it[5])) }
    }
    val equations = hailstones.map { it to it.xyEquation() }

    val min = 200000000000000
    val max = 400000000000000

    val meets = equations.applyToOthers { e1, e2 ->
        val meet = e1.second.meet(e2.second)
        when {
            meet == null -> null
            meet.first < min || meet.first > max -> null
            meet.second < min || meet.second > max -> null
            !e1.first.isFuture(meet) -> null
            !e2.first.isFuture(meet) -> null
            else -> meet
        }
    }.filterNotNull()

    println("part1=" + meets.size)

    val lots = 1000L

    /*(1..lots).forEach { seconds ->
        val pos = hailstones.first().move(seconds.toInt())
        if (seconds % 10 == 0L) println("seconds=$seconds")
        (-lots..lots).forEach { x ->
            (-lots..lots).forEach { y ->
                (-lots..lots).forEach { z ->
                    //val h = Hailstone(pos, Coord3DLong(x, y, z))
                    val inc = Coord3DLong(x, y, z)
                    val h = Hailstone(Hailstone(pos, inc).move(-seconds.toInt()), inc)
                    if (hailstones.all { it.intersects(h) }) {
                        println("RES=$h")
                        return
                    }
                }
            }
        }
    }*/

    val others = hailstones.drop(1)
    val pos = hailstones.first().move(5)

    // val x = others.fold(true) { total, i -> total && i.validSeconds(pos, 5).isNotEmpty() }

    /*val second = (0..lots).firstOrNull { i ->
        val pos = hailstones.first().move(i)
        others.fold(hailstones[1].validSeconds(pos)) { total, i -> if (total.isEmpty()) total else total.intersect(i.validSeconds(pos)) }.isNotEmpty()
    }*/
    /*println("part2=" + x)

    println("[-2] => " + axisMeet(18L, -1L, 9L))
    println("[1, 3, 4] => " + axisMeet(1L, 5L, 5L))
    println(axisMeet(1L, 3L, 5L))
    println("[2, 3, 5] => " + axisMeet(5L, 1L, 1L))
    println("[3, 4, 6] => " + axisMeet(5L, 2L, 1L))

    hailstones.forEach {
        println(""+it+"=>"+it.intersects(Hailstone(Coord3DLong(24, 13, 10), Coord3DLong(-3, 1, 2))))
    }*/

    (1..1_000_000_000).forEach { seconds ->
        if (seconds % 100_000 == 0) println("seconds=$seconds")
        val increments = others.last().calculateIncrements(pos, seconds)
        increments.forEach {
            val move = Hailstone(pos, it).move(-seconds)
            val all = hailstones.all { h ->
                h.intersects(Hailstone(move, it))
            }
            if (all) {
                println("part2="+Hailstone(move, it))
                return
            }
        }
    }
}

private data class Equation(val x: Double, val c: Double) {
    fun meet(e: Equation): Pair<Double, Double>? = when {
        x != e.x -> ((e.c - c) / (e.x - x)).let { -it to -it * x + c }
        else -> null
    }
}

private fun meets(e1: Pair<Hailstone, Equation>, e2: Pair<Hailstone, Equation>): Pair<Double, Double>? {
    val meet = e1.second.meet(e2.second)
    return when {
        meet == null -> null
        // !e1.first.isFuture(meet) -> null
        // !e2.first.isFuture(meet) -> null
        else -> meet
    } // .also { println("meets="+it) }
}

private data class Hailstone(val start: Coord3DLong, val inc: Coord3DLong) {
    fun isFuture(point: Pair<Double, Double>) = point.first > start.x == inc.x > 0 && point.second > start.y == inc.y > 0
    fun move(i: Int) = Coord3DLong(start.x + i * inc.x, start.y + i * inc.y, start.z + i * inc.z)
    fun validSeconds(other: Coord3DLong, move: Int): Set<Long> {
        val new = move(move)
        println("x=>" + axisMeet(new.x, inc.x, other.x))
        println("y=>" + axisMeet(new.y, inc.y, other.y))
        println("z=>" + axisMeet(new.z, inc.z, other.z))
        return axisMeet(new.x, inc.x, other.x).intersect(axisMeet(new.y, inc.y, other.y)).intersect(axisMeet(new.z, inc.z, other.z)).also { println("res=>" + it) }
    }

    fun intersects(other: Hailstone): Boolean {
        val meet = meets(this to xyEquation(), other to other.xyEquation()) ?: return false
        if (meet.first.isNaN() || meet.second.isNaN()) return false

        // This rounds to int first, might be an issue!
        val seconds = ((Math.round(meet.first) - start.x) / inc.x).toInt()
        val compare1 = move(seconds)
        val compare2 = other.move(seconds)
        // println("compare="+compare1+" to=>"+compare2+" == "+(compare1==compare2))
        return compare1 == compare2
    }

    fun xyEquation(): Equation {
        val gradient = inc.y.toDouble() / inc.x.toDouble()
        return Equation(gradient, gradient * -start.x + start.y)
    }

    fun calculateIncrements(other: Coord3DLong, move: Int): List<Coord3DLong> {
        val new = move(move)
        /*println("x=>"+axisMeet2(new.x, inc.x, other.x))
        println("y=>"+axisMeet2(new.y, inc.y, other.y))
        println("z=>"+axisMeet2(new.z, inc.z, other.z))*/

        val xs = axisMeet2(new.x, inc.x, other.x)
        val ys = axisMeet2(new.y, inc.y, other.y)
        val zs = axisMeet2(new.z, inc.z, other.z)

        val ySeconds = ys.map { it.second }
        val zSeconds = zs.map { it.second }

        return xs.filter { it.second in ySeconds && it.second in zSeconds }.map { xOffset ->
            Coord3DLong(xOffset.first, ys.first { it.second == xOffset.second }.first, zs.first { it.second == xOffset.second }.first)
        }
    }
}

// private val allInts = (Int.MIN_VALUE..Int.MAX_VALUE).toList()

private fun axisMeetOld(axis1: Long, axis1Inc: Long, axis2: Long): Set<Long> = axisMeetBeingCaught(axis1, axis1Inc, axis2) + axisMeetCatching(axis1, axis1Inc, axis2)
private fun axisMeetBeingCaught(axis1: Long, axis1Inc: Long, axis2: Long): Set<Long> {
    if (axis1 == axis2) return setOf(axis1Inc)
    val diff = axis1 - axis2
    return ((axis1Inc + 1)..(diff + axis1Inc)).mapNotNull {
        val incDiff = it - axis1Inc
        if (diff % incDiff == 0L) it else null
    }.toSet()
}

private fun axisMeetCatching(axis1: Long, axis1Inc: Long, axis2: Long): Set<Long> {
    if (axis1 == axis2) return setOf(axis1Inc)
    val diff = axis1 - axis2
    return (diff until axis1Inc).mapNotNull {
        val incDiff = it - axis1Inc
        if (diff % incDiff == 0L) it else null
    }.toSet()
}

private fun axisMeet(axis1: Long, axis1Inc: Long, axis2: Long): Set<Long> {
    if (axis1 == axis2) return setOf(axis1Inc)
    val diff = axis1 - axis2
    val max = abs(diff) + abs(axis1Inc)
    return anyRange(max, -max).mapNotNull {
        val incDiff = it - axis1Inc
        if (incDiff != 0L && diff % incDiff == 0L) it else null
    }.toSet()
}

private fun axisMeet2(axis1: Long, axis1Inc: Long, axis2: Long): Set<Pair<Long, Long>> {
    //if (axis1 == axis2) throw IllegalArgumentException("Can't handle equal positions yet")
    if (axis1 == axis2) return emptySet()
    val diff = axis1 - axis2
    // val max = abs(diff) + abs(axis1Inc)
    val max = 2_000L
    return anyRange(max, -max).mapNotNull {
        val incDiff = it - axis1Inc
        if (incDiff != 0L && diff % incDiff == 0L) it to diff / incDiff else null
    }.toSet()
}
