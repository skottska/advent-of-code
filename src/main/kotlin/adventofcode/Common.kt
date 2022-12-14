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
