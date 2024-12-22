package adventofcode.y2024 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.asString
import adventofcode.charToDirection
import adventofcode.concat
import adventofcode.readFile
import adventofcode.sort
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup())
    /*val part1 = lines.map { line ->
        val keypadKeys = "A$line".zipWithNext { a, b -> keypadDistance(a, b) }.concat()
        val controller1Keys = "A$keypadKeys".zipWithNext { a, b -> controllerDistance(a, b) }.concat()
        val controller2Keys = "A$controller1Keys".zipWithNext { a, b -> controllerDistance(a, b) }.concat()


        println(keypadKeys)
        println(keypadKeys.length)
        println(controller1Keys)
        println(controller1Keys.length)
        println(controller2Keys)
        println(controller2Keys.length)
        controller2Keys.length
    }.forEach {
        println("woo=$it")
    }


    print("<vAv<<Av<<AAAv<<AAAv<<AAv<<AA<vAAAv<<A<vAAv<<A<vAAv<<A<vAAA")
    print("<v<A>>^A<vA<A>>^AAvAA<^A>A<v<A>>^AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A")*/

    val part1b = lines.sumOf { line ->
        val keypadKeys = explode("A$line".zipWithNext { a, b -> keypadDistance2(a, b) })
        val controller1Keys = keypadKeys.flatMap { explode("A$it".zipWithNext { a, b -> controllerDistance2(a, b) }) }
        println("keypadKeys="+keypadKeys)
        val controller2Keys = controller1Keys.flatMap {
            val e = explode( "A$it".zipWithNext { a, b -> controllerDistance2(a, b) } )
            println("explode="+it+" min="+e.minOf { s -> s.length })
            e
        }


        /*println("keypadKeys="+keypadKeys)
        println(keypadKeys.map { it.length })
        println("controller1Keys="+controller1Keys)
        println(controller1Keys)
        println(controller1Keys.map { it.length })
        println("controller2Keys="+controller2Keys)
        println(controller2Keys)
        println(controller2Keys.map { it.length })*/

        controller2Keys.minOf { it.length } * line.dropLast(1).toLong()
    }
    println("part1="+ part1b)

    /*val part1c = lines.sumOf { line ->
        val keypadKeys = explode("A$line".zipWithNext { a, b -> keypadDistance2(a, b) })
        val controllerKeys = (1..2).fold(keypadKeys) { initTotal, i ->
            val total = initTotal.filter { !it.contains("<AAv<AA>>^") }
            println("iter="+i+" size="+total.size+" lengths="+total.groupBy { it.length }.map { it.key to it.value.size })
            total.flatMap { explode("A$it".zipWithNext { a, b -> controllerDistance2(a, b) }) }
        }
        println("iter="+3+" size="+controllerKeys.size+" lengths="+controllerKeys.groupBy { it.length }.map { it.key to it.value.size })
        controllerKeys.minOf { it.length } * line.dropLast(1).toLong()
    }
    println("part1="+ part1c)*/

    val ignore = "^^<<A"
    val part1d = lines.sumOf { line ->
        val keypadKeys = explode("A$line".zipWithNext { a, b -> keypadDistance2(a, b) }).first { !it.contains(ignore) }
        val controllerKeys = (1..25).fold(keypadKeys) { total, i ->
            println("iter="+i+"  lengths="+total.length)
            explode2("A$total".zipWithNext { a, b -> controllerDistance2(a, b) }).first { !it.contains(ignore) }
        }
        println("iter=end"+" size="+controllerKeys+" lengths="+controllerKeys.length)
        controllerKeys.length * line.dropLast(1).toLong()
    }
    println("part1d="+ part1d)


    //print("<vAv<<Av<<AAAv<<AAAv<<AAv<<AA<vAAAv<<A<vAAv<<A<vAAv<<A<vAAA")

    /*
    println("old="+("A"+lines.first()).zipWithNext { a, b -> keypadDistance(a, b) }.concat())
    println("new="+("A"+lines.first()).zipWithNext { a, b -> keypadDistance2(a, b) })
    explode(("A"+lines.first()).zipWithNext { a, b -> keypadDistance2(a, b) }, "").forEach { println("explode="+it) }*/
}

private fun explode(l : List<List<String>>, s: String = ""): List<String> {
    if (l.isEmpty()) return listOf(s)
    return l.first().flatMap { explode(l.drop(1), s + it ) }
}

private fun explode2(l : List<List<String>>, s: String = ""): List<String> {
    return listOf(l.map { it.first() }.concat())
}

private fun print(controller2: String) {
    println("controller2="+controller2)
    val controller1 = convertController(controller2)
    println("controller1="+controller1)
    val keypad = convertController(controller1)
    println("keypad="+keypad)
    val keys = convertKeypad(keypad)
    println("keys="+keys)
}

private fun p(s: String) = s.split('A').joinToString("") { it.sort() + 'A' }

private fun convertController(s: String): String {
    var cur = controllerGrid.getValue('A')
    return s.split('A').map { text ->
        cur = text.fold(cur) { total, next -> charToDirection(next).invoke(total) }
        controllerGrid.filter { it.value == cur }.map { it.key }.first()
    }.dropLast(1).asString()
}

private fun convertKeypad(s: String): String {
    var cur = keypadGrid.getValue('A')
    return s.split('A').map { text ->
        cur = text.fold(cur) { total, next -> charToDirection(next).invoke(total) }
        keypadGrid.filter { it.value == cur }.map { it.key }.first()
    }.dropLast(1).asString()
}

private val keypadGrid = transformGrid(listOf(listOf('7', '8', '9'), listOf('4', '5', '6'),
    listOf('1', '2', '3'), listOf(null, '0', 'A')))
private val controllerGrid = transformGrid(listOf(listOf(null, '^', 'A'), listOf('<', 'v', '>')))

/*private val controller1 = mapOf(
    Coord::left to 6, Coord::right to 2, Coord::up to 2, Coord::down to 4
)*/

private val controller1 = mapOf(
    '<' to 6, '>' to 2, '^' to 2, 'v' to 4, 'A' to 1
)

private fun transformGrid(l: List<List<Char?>>) = l.mapIndexed { row, line ->
    line.mapIndexedNotNull { col, c ->  c?.let { c to Coord(row, col) } }
}.flatten().toMap()

private fun keypadDistance(c1: Char, c2: Char): String {
    val coord1 = keypadGrid.getValue(c1)
    val coord2 = keypadGrid.getValue(c2)

    var result = ""
    val horizDist = coord1.col - coord2.col
    when {
        horizDist > 0 -> repeat(horizDist) { result += '<' }
        horizDist < 0 -> repeat(-horizDist) { result += '>' }
    }
    val vertDist = coord1.row - coord2.row
    when {
        vertDist > 0 -> repeat(vertDist) { result += '^'}
        vertDist < 0 -> repeat(-vertDist) { result += 'v' }
    }

    return result + 'A'
}

private fun keypadDistance2(c1: Char, c2: Char): List<String> {
    val coord1 = keypadGrid.getValue(c1)
    val coord2 = keypadGrid.getValue(c2)

    val horizDist = coord1.col - coord2.col
    val horiz = when {
        horizDist > 0 -> "<".repeat(horizDist)
        horizDist < 0 -> ">".repeat(-horizDist)
        else -> ""
    }
    val vertDist = coord1.row - coord2.row
    val vert = when {
        vertDist > 0 -> "^".repeat(vertDist)
        vertDist < 0 -> "v".repeat(-vertDist)
        else -> ""
    }

    return listOf(vert + horiz, horiz + vert).filter {
        it.fold(listOf(coord1)) { total, c -> total + charToDirection(c).invoke(total.last()) }.all { coord -> coord in keypadGrid.values }
    }.map { it+ "A" }.toSet().toList()
}

private fun controllerDistance(c1: Char, c2: Char): String {
    val coord1 = controllerGrid.getValue(c1)
    val coord2 = controllerGrid.getValue(c2)

    var result = ""
    val horizDist = coord1.col - coord2.col
    when {
        horizDist > 0 -> repeat(horizDist) { result += '<' }
        horizDist < 0 -> repeat(-horizDist) { result += '>' }
    }
    val vertDist = coord1.row - coord2.row
    when {
        vertDist > 0 -> repeat(vertDist) { result += '^'}
        vertDist < 0 -> repeat(-vertDist) { result += 'v' }
    }
    return result + 'A'
}

private val controllerCache = mutableMapOf<Pair<Char, Char>, List<String>>()
private fun controllerDistance2(c1: Char, c2: Char): List<String> = controllerCache.getOrPut(c1 to c2) {
    val coord1 = controllerGrid.getValue(c1)
    val coord2 = controllerGrid.getValue(c2)

    val horizDist = coord1.col - coord2.col
    val horiz = when {
        horizDist > 0 -> "<".repeat(horizDist)
        horizDist < 0 -> ">".repeat(-horizDist)
        else -> ""
    }
    val vertDist = coord1.row - coord2.row
    val vert = when {
        vertDist > 0 -> "^".repeat(vertDist)
        vertDist < 0 -> "v".repeat(-vertDist)
        else -> ""
    }

    return listOf(horiz + vert, vert + horiz).filter {
        it != "^^<<" &&
        it.fold(listOf(coord1)) { total, c -> total + charToDirection(c).invoke(total.last()) }.all { coord -> coord in controllerGrid.values }
    }.map { it+ "A" }.toSet().toList()
}
