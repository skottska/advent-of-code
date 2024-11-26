package adventofcode.y2020 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).fold(listOf("")) { total, i ->
        if (i.isEmpty()) total + listOf("") else total.last().let { total.dropLast(1) + listOf("$it $i") }
    }
    val validFields = mapOf<String, (String) -> Boolean>(
        "byr" to { s -> s.toInt() in 1920..2002 },
        "iyr" to { s -> s.toInt() in 2010..2020 },
        "eyr" to { s -> s.toInt() in 2020..2030 },
        "hgt" to { s -> matchNumbers(s).first().let { if (s.contains("cm")) it in 150..193 else it in 59..76 } },
        "hcl" to { s -> s.matches(Regex("#[0-9a-f]{6}")) },
        "ecl" to { s -> s in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") },
        "pid" to { s -> s.matches(Regex("[0-9]{9}")) },
        "cid" to { true }
    )
    val passports = lines.map { line -> line.split(" ").associate { field -> field.split(":").let { it.first() to it.last() } } }
        .filter { it.keys.containsAll(validFields.keys - "cid") }
    println("part1=" + passports.count())
    println("part2=" + passports.count { p -> p.all { field -> validFields[field.key]?.let { it(field.value) } ?: true } })
}
