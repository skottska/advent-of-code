package adventofcode.y2016 // ktlint-disable filename

import adventofcode.md5
import adventofcode.readFile

fun main() {
    val line = readFile("src/main/resources/y2016/day05.txt")[0]
    var i = 0
    var part1 = ""
    val part2 = mutableMapOf<Int, Char>()
    while (part2.size != 8) {
        val md5 = md5(line + i++)
        if (md5.take(5) == "00000") {
            part1 += md5[5]
            when {
                md5[5].code < '0'.code || md5[5].code > '7'.code -> Unit
                part2[md5[5].digitToInt()] != null -> Unit
                else -> part2[md5[5].digitToInt()] = md5[6]
            }
        }
    }
    println("part1=" + part1.take(8))
    println("part2=" + part2.keys.sorted().fold("") { total, c -> total + part2[c] })
}
