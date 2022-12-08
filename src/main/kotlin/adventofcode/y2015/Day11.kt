package adventofcode.y2015 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val line = readFile("src/main/resources/y2015/day11.txt")[0]
    var latest = inc(line)
    while (!isValid(latest)) latest = inc(latest)
    println("part1=$latest")

    latest = inc(latest)
    while (!isValid(latest)) latest = inc(latest)
    println("part2=$latest")
}

fun inc(pass: String): String {
    var inc = true
    var result = ""
    pass.reversed().forEach {
        when {
            !inc -> result = it + result
            it == 'z' -> result = "a$result"
            else -> {
                inc = false
                result = (it.code + 1).toChar() + result
            }
        }
    }
    return result
}

fun isValid(pass: String) = when {
    listOf('i', 'o', 'l').any { pass.contains(it) } -> false
    !consecutive(pass) -> false
    else -> pairs(pass)
}

fun consecutive(pass: String) = pass.windowed(3, 1) { it[0].code + 1 == it[1].code && it[1].code + 1 == it[2].code }.any { it }
fun pairs(pass: String) = pass.windowed(2, 1).filter { it.first() == it.last() }.toSet().size >= 2
