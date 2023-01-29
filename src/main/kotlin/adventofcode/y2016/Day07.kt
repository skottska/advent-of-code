package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

fun main() {
    val lines = readFile("src/main/resources/y2016/day07.txt")
    val insAndOutsFunc = { s: String -> matches(s, "\\[[a-z]+\\]") to (matches(s, "[a-z]+\\[") + matches(s, "\\][a-z]+")) }
    println(
        "part1=" + lines.count { line ->
            val insAndOuts = insAndOutsFunc(line)
            if (insAndOuts.first.any { supportsTls(it) }) false else { insAndOuts.second.any { supportsTls(it) } }
        }
    )
    println(
        "part2=" + lines.count { line ->
            val insAndOuts = insAndOutsFunc(line).toList().map { l -> l.flatMap { ssls(it) } }
            insAndOuts.first().any { i -> insAndOuts.last().any { o -> correspondingSSl(i, o) } }
        }
    )
}

private fun supportsTls(s: String) = s.windowed(size = 4, step = 1).any { it[0] == it[3] && it[1] == it[2] && it[0] != it[1] }
private fun ssls(s: String) = s.windowed(size = 3, step = 1).filter { it[0] == it[2] && it[0] != it[1] }
private fun correspondingSSl(a: String, b: String) = a.take(2) == b.takeLast(2)
