package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile
import org.json.JSONArray
import org.json.JSONObject

fun main(args: Array<String>) {
    val line = readFile("src/main/resources/y2015/day12.txt")[0]
    println("part1=" + matches(line, "-?[0-9]+").sumOf { it.toInt() })
    println("part2=" + navigate(JSONObject("{ body: $line}")))
}

fun navigate(o: Any): Int = when (o) {
    is JSONArray -> o.sumOf { navigate(it) }
    is JSONObject -> if (hasRed(o)) 0 else o.keySet().sumOf { navigate(o.get(it)) }
    is Int -> o
    else -> 0
}

fun hasRed(o: JSONObject) = o.keySet().any {
    o.get(it).let { under -> under is String && under == "red" }
}
