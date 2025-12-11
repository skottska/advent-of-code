package adventofcode.y2025 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val map = readFile(MethodHandles.lookup()).associate { line -> matches(line, "[a-z]+").let { it.first() to it.drop(1) } }
    println("part1=" + iterate1(map, listOf("you")))
    println("part2=" + iterate2(map, "svr").ways0)
}

private tailrec fun iterate1(map: Map<String, List<String>>, cur: List<String>, count: Int = 0): Int {
    if (cur.isEmpty()) return count
    return iterate1(map, cur.filter { it != "out" }.flatMap { map.getValue(it) }, count + cur.count { it == "out" })
}

private data class Result(val ways0: Long, val ways1: Long, val ways2: Long) {
    fun merge(r: Result) = Result(ways0 + r.ways0, ways1 + r.ways1, ways2 + r.ways2)
    fun elevate() = Result(ways0 + ways1, ways2, 0)
}
private val cache: MutableMap<String, Result> = mutableMapOf()
private fun iterate2(map: Map<String, List<String>>, cur: String): Result = cache.getOrPut(cur) {
    if (cur == "out") Result(ways0 = 0, ways1 = 0, ways2 = 1)
    else {
        val result = map.getValue(cur).map { iterate2(map, it) }.reduce { a, b -> a.merge(b) }
        if (cur in listOf("dac", "fft")) result.elevate() else result
    }
}
