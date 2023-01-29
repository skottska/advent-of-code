package adventofcode.y2022 // ktlint-disable filename

import adventofcode.readFile
import org.json.JSONArray
import org.json.JSONObject

fun main() {
    val lines = readFile("src/main/resources/y2022/day13.txt")
    val result1 = mutableListOf<Int>()
    val packets = mutableListOf<String>()
    lines.chunked(3).forEachIndexed { index, pair ->
        if (arePacketsInOrder(pair[0], pair[1])) result1.add(index + 1)
        packets.addAll(pair.take(2))
    }
    println("part1=" + result1.sum())
    val dividers = listOf("[[2]]", "[[6]]")
    packets.addAll(dividers)
    packets.sortWith { a, b -> arePacketsInOrder(a, b).let { if (it) -1 else 1 } }
    println("part2=" + dividers.map { packets.indexOf(it) + 1 }.let { it.first() * it.last() })
}

private fun arePacketsInOrder(packet1: String, packet2: String): Boolean {
    val json1 = JSONObject("{\"h\": $packet1}")
    val json2 = JSONObject("{\"h\": $packet2}")
    return arePacketsInOrderInner(json1.getJSONArray("h"), json2.getJSONArray("h")) ?: true
}

private fun arePacketsInOrderInner(packet1: JSONArray, packet2: JSONArray): Boolean? {
    (0 until packet1.length()).forEach { index ->
        val inner1 = packet1.get(index)
        val inner2 = if (index < packet2.length()) packet2.get(index) else return false
        val result = when {
            inner1 is Int && inner2 is Int -> if (inner1 == inner2) null else inner1 < inner2
            inner1 is Int -> arePacketsInOrderInner(JSONArray(listOf(inner1)), inner2 as JSONArray)
            inner2 is Int -> arePacketsInOrderInner(inner1 as JSONArray, JSONArray(listOf(inner2)))
            else -> arePacketsInOrderInner(inner1 as JSONArray, inner2 as JSONArray)
        }
        if (result != null) return result
    }
    return if (packet1.length() == packet2.length()) null else packet1.length() < packet2.length()
}
