package adventofcode

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun readFile(fileName: String): List<String> = File(fileName).useLines { it.toList() }

fun split(line: String) = line.split("\\s+".toRegex())

fun matches(line: String, regex: String) = Regex(regex)
    .findAll(line)
    .map { it.groupValues[0] }
    .toList()

fun md5(input: String) = BigInteger(1, MessageDigest.getInstance("MD5").digest(input.toByteArray()))
    .toString(16)
    .padStart(32, '0')
