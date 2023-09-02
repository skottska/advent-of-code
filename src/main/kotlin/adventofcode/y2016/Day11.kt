package adventofcode.y2016 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile

fun main() {
    val floors = readFile("src/main/resources/y2016/day11.txt").map { line ->
        val microchips = matches(line, "[a-z]+\\-compatible microchip").map { Item(it.substring(0, it.indexOf('-')), ItemType.MICROCHIP) }
        val generators = matches(line, "[a-z]+ generator").map { Item(it.substring(0, it.indexOf(' ')), ItemType.GENERATOR) }
        (microchips + generators).toSet()
    }

    iterate(FloorPlan(0, floors), moves = 0)
    println("part1=$minResult")
    minResult = Int.MAX_VALUE; cache2.clear()
    val newItems = listOf("elerium", "dilithium").flatMap { listOf(Item(it, ItemType.MICROCHIP), Item(it, ItemType.GENERATOR)) }
    val newFloors = listOf(floors.first() + newItems) + floors.drop(1)
    iterate(FloorPlan(0, newFloors), moves = 0)
    println("part2=$minResult")
}

private fun iterate(floorPlan: FloorPlan, moves: Int) {
    val simpleFloorPlan = floorPlan.toSimple()
    if (moves >= minResult - 1 || moves >= cache2.getOrDefault(simpleFloorPlan, Int.MAX_VALUE)) {
        return
    }
    cache2[simpleFloorPlan] = moves
    val curFloor = floorPlan.floors[floorPlan.liftPos].toList()
    val validLiftMoves = listOf(floorPlan.liftPos + 1, floorPlan.liftPos - 1).filter { it >= 0 && it < floorPlan.floors.size }
    validLiftMoves.forEach { toFloor ->
        curFloor.forEachIndexed { index, item ->
            val singleMove = listOf(moveFloors(floorPlan, toFloor, listOf(item)))
            val multiMove = if (index + 1 < curFloor.size) {
                curFloor.subList(index + 1, curFloor.size).map {
                    moveFloors(floorPlan, toFloor, listOf(item, it))
                }
            } else emptyList()

            val allMoves = (singleMove + multiMove).filter { validFloors(it.floors) }
            if (allMoves.any { f -> f.floors.dropLast(1).all { it.isEmpty() } }) {
                minResult = moves + 1
                return
            }
            allMoves.forEach { iterate(it, moves + 1) }
        }
    }
}

private fun moveFloors(floorPlan: FloorPlan, toFloor: Int, toMove: List<Item>): FloorPlan {
    val floors = floorPlan.floors.mapIndexed { index, floor ->
        when (index) {
            toFloor -> floor + toMove
            else -> floor - toMove.toSet()
        }
    }
    return FloorPlan(toFloor, floors)
}

private fun validFloors(floors: List<Set<Item>>): Boolean {
    return floors.all { floor ->
        val chips = floor.filter { it.type == ItemType.MICROCHIP }
        chips.all { chip ->
            when {
                floor.contains(Item(chip.name, ItemType.GENERATOR)) -> true
                floor.any { it.type == ItemType.GENERATOR } -> false
                else -> true
            }
        }
    }
}

private val cache2 = mutableMapOf<SimpleFloorPlan, Int>()
private var minResult = Int.MAX_VALUE
private data class SimpleFloorPlan(val liftPos: Int, val relations: List<Pair<Int, Int>>)
private data class FloorPlan(val liftPos: Int, val floors: List<Set<Item>>) {
    fun toSimple(): SimpleFloorPlan {
        val chips = floors.mapIndexed { index, f -> f.filter { it.type == ItemType.MICROCHIP }.map { it.name to index } }.flatten()
        val generators = floors.mapIndexed { index, f -> f.filter { it.type == ItemType.GENERATOR }.map { it.name to index } }.flatten().toMap()
        val relations = chips.map { it.second to generators.getValue(it.first) }.sortedWith { a, b ->
            when (a.first) {
                b.first -> a.second.compareTo(b.second)
                else -> a.first.compareTo(b.first)
            }
        }
        return SimpleFloorPlan(liftPos, relations)
    }
}
private data class Item(val name: String, val type: ItemType)
private enum class ItemType { MICROCHIP, GENERATOR }
