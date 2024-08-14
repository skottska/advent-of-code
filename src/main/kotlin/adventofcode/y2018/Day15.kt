package adventofcode.y2018 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.mapCoord
import adventofcode.readFile
import java.util.UUID

fun main() {
    val map = readFile("src/main/resources/y2018/day15.txt").mapCoord()
    val grid = map.map {
        it.key to when {
            it.value in listOf('E', 'G') -> '.'
            else -> it.value
        }
    }.toMap()

    val part1 = rounds(grid, generateCombatUnits(map, 3))
    println("part1="+(part1.first-1) * part1.second.sumOf { it.hp })

    val numElves = map.values.count { it == 'E' }
    var firepower = 4
    var result = rounds(grid, generateCombatUnits(map, firepower++), endOnElfDeath = true)
    while (result.second.size != numElves) result = rounds(grid, generateCombatUnits(map, firepower++), endOnElfDeath = true)
    println("part2="+(result.first-1) * result.second.sumOf { it.hp })
}

private fun generateCombatUnits(map: Map<Coord, Char>, elfFirepower: Int): List<CombatUnit> = map.filter { it.value =='E' }.map { CombatUnit(200, it.key, true, elfFirepower) } +
    map.filter { it.value =='G' }.map { CombatUnit(200, it.key, false, 3) }


private fun rounds(grid: Map<Coord, Char>, combatUnitsIn: List<CombatUnit>, endOnElfDeath: Boolean = false): Pair<Int, Collection<CombatUnit>> {
    val combatUnits = combatUnitsIn.associateBy { it.id }.toMutableMap()
    val numElves = combatUnitsIn.count { it.isElf }
    var rounds = 1

    while(true) {
        val moved = mutableListOf<UUID>()
        var next = nextToMove(moved, combatUnits.values)
        while (next != null) {
            if (combatUnits.map { it.value.isElf }.toSet().size < 2) return rounds to combatUnits.values
            move(grid, next, combatUnits)
            attack(next, combatUnits)
            if (endOnElfDeath && combatUnits.count { it.value.isElf } != numElves) return 0 to emptyList()
            moved += next
            next = nextToMove(moved, combatUnits.values)
        }
        rounds++
    }
}

private fun move(grid: Map<Coord, Char>, next: UUID, combatUnits: MutableMap<UUID, CombatUnit>) {
    val me = combatUnits.getValue(next)
    val enemies = combatUnits.values.filter { it.isElf != me.isElf }
    if (me.c.around().any { it in enemies.map { cu -> cu.c }}) return // Already in range

    val enemyRangeCoords = enemies.flatMap { it.c.around() }.filter { moveable(it, grid, combatUnits) }.toSet()
    var moves = me.c.around().filter { moveable(it, grid, combatUnits) }.map { it to setOf(it) }
    while (moves.all { it.second.none { m -> m in enemyRangeCoords } }) {
        val newMoves = moves.map { move -> move.first to move.second.flatMap { m -> m.around().filter { moveable(it, grid, combatUnits) } + m }.toSet() }
        if (newMoves == moves) return
        moves = newMoves
    }
    val nearestEnemyRange = enemyRangeCoords.filter { moves.any { m -> it in m.second } }.sortedWith(compareBy<Coord> { it.row }.then(compareBy { it.col })).first()
    val shortest = moves.filter { nearestEnemyRange in it.second }.map { it.first }.sortedWith(compareBy<Coord> { it.row }.then(compareBy { it.col }))
    if (shortest.isNotEmpty()) combatUnits[me.id] = me.copy(c = shortest.first())
}

private fun moveable(to: Coord, grid: Map<Coord, Char>, combatUnits: Map<UUID, CombatUnit>) = to in grid.keys && grid[to] == '.' && to !in combatUnits.values.map { it.c }

private fun attack(next: UUID, combatUnits: MutableMap<UUID, CombatUnit>) {
    val me = combatUnits.getValue(next)
    val enemyCoords = combatUnits.values.filter { it.isElf != me.isElf }.map { it.c }
    val adjacentCUs = me.c.around().filter { it in enemyCoords }.map { combatUnits.values.first { cu -> cu.c == it } }
    if (adjacentCUs.isNotEmpty()) {
        val toAttack = adjacentCUs.sortedWith(compareBy<CombatUnit> { it.hp }.then(readingOrderComparator)).first()
        val result = toAttack.copy(hp = toAttack.hp - me.firepower)
        when {
            result.hp <= 0 -> combatUnits.remove(result.id)
            else -> combatUnits[result.id] = result
        }
    }
}

private val readingOrderComparator = compareBy<CombatUnit> { it.c.row }.then(compareBy { it.c.col })
private fun nextToMove(moved: List<UUID>, units: Collection<CombatUnit>): UUID? = units.filter { it.id !in moved }.sortedWith(readingOrderComparator).firstOrNull()?.id

private data class CombatUnit(val hp: Int, val c: Coord, val isElf: Boolean, val firepower: Int, val id: UUID = UUID.randomUUID())
