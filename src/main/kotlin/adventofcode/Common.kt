package adventofcode

import java.io.File

fun readFile(fileName: String): List<String> = File(fileName).useLines { it.toList() }

fun split(line: String) = line.split("\\s+".toRegex())

fun matches(line: String, regex: String) = Regex(regex)
    .findAll(line)
    .map { it.groupValues[0] }
    .toList()
