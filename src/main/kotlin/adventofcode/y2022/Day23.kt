package adventofcode.y2022 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day23.txt")
    val initState = lines.map { line -> line.map { if (it == '.') State.EMPTY else State.ELF } }
    val field = (0..9).fold(initState) { init, i ->
        val field = listOf(List(init.first().size) { State.EMPTY }).let { it + init + it }.map { row ->
            listOf(State.EMPTY) + row + listOf(State.EMPTY)
        }

        val moves = generateMoves(field, i)
        val uniqueMoves = moves.filter { entry -> moves.values.count { it == entry.value } == 1 }
        field.mapIndexed { x, row ->
            row.mapIndexed { y, state ->
                when {
                    uniqueMoves.keys.contains(x to y) -> State.EMPTY
                    uniqueMoves.values.contains(x to y) -> State.ELF
                    else -> state
                }
            }
        }
    }

    val xSize = field.indexOfLast { it.contains(State.ELF) } - field.indexOfFirst { it.contains(State.ELF) } + 1
    val ySize = field.filter { it.contains(State.ELF) }.let { it.maxOf { row -> row.lastIndexOf(State.ELF) } - it.minOf { row -> row.indexOf(State.ELF) } + 1 }
    println("part1=" + (xSize * ySize - field.sumOf { it.count { s -> s == State.ELF } }))
    part2(initState)
}

private val funcs = listOf(::isElfNorth, ::isElfSouth, ::isElfWest, ::isElfEast)
private val moveFuncs = listOf(::moveNorth, ::moveSouth, ::moveWest, ::moveEast)

private fun generateMoves(field: List<List<State>>, i: Int) =
    field.mapIndexed { x, row ->
        row.mapIndexedNotNull { y, state ->
            when {
                state == State.EMPTY -> null
                !isElfNear(field, x to y) -> null
                !funcs[i % funcs.size](field, x to y) -> (x to y) to moveFuncs[i % moveFuncs.size](x to y)
                !funcs[(i + 1) % funcs.size](field, x to y) -> (x to y) to moveFuncs[(i + 1) % moveFuncs.size](x to y)
                !funcs[(i + 2) % funcs.size](field, x to y) -> (x to y) to moveFuncs[(i + 2) % moveFuncs.size](x to y)
                !funcs[(i + 3) % funcs.size](field, x to y) -> (x to y) to moveFuncs[(i + 3) % moveFuncs.size](x to y)
                else -> null
            }
        }
    }.flatten().toMap()

private fun needsPadding(field: List<List<State>>) =
    when {
        field.first().contains(State.ELF) -> true
        field.last().contains(State.ELF) -> true
        field.map { it.first() }.contains(State.ELF) -> true
        field.map { it.last() }.contains(State.ELF) -> true
        else -> false
    }

private fun part2(initState: List<List<State>>) {
    var i = 0
    var field = initState
    while (true) {
        if (needsPadding(field)) {
            field = listOf(List(field.first().size) { State.EMPTY }).let { it + field + it }.map { row ->
                listOf(State.EMPTY) + row + listOf(State.EMPTY)
            }
        }

        val moves = generateMoves(field, i++)
        val uniqueMoves = moves.filter { entry -> moves.values.count { it == entry.value } == 1 }
        if (uniqueMoves.isEmpty()) {
            println("part2=$i")
            return
        }
        field = field.mapIndexed { x, row ->
            row.mapIndexed { y, state ->
                when {
                    uniqueMoves.keys.contains(x to y) -> State.EMPTY
                    uniqueMoves.values.contains(x to y) -> State.ELF
                    else -> state
                }
            }
        }
    }
}

private fun moveNorth(pos: Pair<Int, Int>) = pos.copy(first = pos.first - 1)
private fun moveSouth(pos: Pair<Int, Int>) = pos.copy(first = pos.first + 1)
private fun moveEast(pos: Pair<Int, Int>) = pos.copy(second = pos.second + 1)
private fun moveWest(pos: Pair<Int, Int>) = pos.copy(second = pos.second - 1)
private fun isElfNear(field: List<List<State>>, pos: Pair<Int, Int>) =
    isElfNorth(field, pos) || isElfSouth(field, pos) || isElfEast(field, pos) || isElfWest(field, pos)

private fun isElfNorth(field: List<List<State>>, pos: Pair<Int, Int>) = listOfNotNull(
    field.getOrNull(pos.first - 1)?.getOrNull(pos.second - 1),
    field.getOrNull(pos.first - 1)?.getOrNull(pos.second),
    field.getOrNull(pos.first - 1)?.getOrNull(pos.second + 1)
).any { it == State.ELF }

private fun isElfSouth(field: List<List<State>>, pos: Pair<Int, Int>) = listOfNotNull(
    field.getOrNull(pos.first + 1)?.getOrNull(pos.second - 1),
    field.getOrNull(pos.first + 1)?.getOrNull(pos.second),
    field.getOrNull(pos.first + 1)?.getOrNull(pos.second + 1)
).any { it == State.ELF }

private fun isElfEast(field: List<List<State>>, pos: Pair<Int, Int>) = listOfNotNull(
    field.getOrNull(pos.first + 1)?.getOrNull(pos.second + 1),
    field.getOrNull(pos.first)?.getOrNull(pos.second + 1),
    field.getOrNull(pos.first - 1)?.getOrNull(pos.second + 1)
).any { it == State.ELF }

private fun isElfWest(field: List<List<State>>, pos: Pair<Int, Int>) = listOfNotNull(
    field.getOrNull(pos.first + 1)?.getOrNull(pos.second - 1),
    field.getOrNull(pos.first)?.getOrNull(pos.second - 1),
    field.getOrNull(pos.first - 1)?.getOrNull(pos.second - 1)
).any { it == State.ELF }

private enum class State { ELF, EMPTY }
