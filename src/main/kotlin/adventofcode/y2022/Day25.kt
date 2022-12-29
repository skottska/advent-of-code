package adventofcode.y2022 // ktlint-disable filename

import adventofcode.readFile
import java.math.BigInteger
import java.math.BigInteger.*

fun main(args: Array<String>) {
    val total = readFile("src/main/resources/y2022/day25.txt").flatMap { line ->
        line.reversed().mapIndexed { index, c ->
            5.toBigInteger().pow(index) * fromSnafu(c).toBigInteger()
        }
    }.sumOf { it }
    print("part1="+toSnafu(total, maxPower(total)))
}

private fun maxPower(i: BigInteger): BigInteger {
    var cur = ONE
    var total = ZERO
    while (total < i) {
        cur *= 5.toBigInteger()
        total += TWO * cur
    }
    return cur
}


private fun maxSnafu(power: BigInteger): BigInteger = when {
    power < ONE -> ZERO
    power == ONE -> TWO
    else -> TWO * power + maxSnafu(power / 5.toBigInteger())
}

private fun toSnafu(i: BigInteger, power: BigInteger): String {
    if (power < ONE) return ""
    val maxi = maxSnafu(power / 5.toBigInteger())
    val toSnafuFunc = { x: BigInteger -> toSnafu(x, power / 5.toBigInteger()) }
    return when {
        i > ZERO && i > power + maxi -> '2' + toSnafuFunc(i - TWO * power)
        i > ZERO && i > maxi -> '1' + toSnafuFunc(i - power)
        -i > power + maxi -> '=' + toSnafuFunc(i + TWO * power)
        -i > maxi -> '-' + toSnafuFunc(i + power)
        else -> '0' + toSnafuFunc(i)
    }
}

private fun fromSnafu(c: Char) = when (c) {
    '2' -> 2
    '1' -> 1
    '0' -> 0
    '-' -> -1
    else -> -2
}