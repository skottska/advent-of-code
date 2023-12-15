package adventofcode.y2018 // ktlint-disable filename

import adventofcode.Coord
import adventofcode.DirectedCoord
import adventofcode.Facing
import adventofcode.mapCoord
import adventofcode.readFile
import java.lang.IllegalArgumentException

fun main() {
    val initGrid = readFile("src/main/resources/y2018/day13.txt").mapCoord()
    val carts = initGrid.mapNotNull { c ->
        when (c.value) {
            '^' -> Facing.UP
            'v' -> Facing.DOWN
            '>' -> Facing.RIGHT
            '<' -> Facing.LEFT
            else -> null
        }?.let { Cart(DirectedCoord(it, c.key)) }
    }
    val grid = initGrid.map {
        it.key to when (it.value) {
            in listOf('^', 'v') -> '|'
            in listOf('<', '>') -> '-'
            else -> it.value
        }
    }.toMap()

    val crash = iterateUntilCrash(grid, carts)
    println("part1=" + crash.col + "," + crash.row)
    val sole = iterateUntilSoleSurvivor(grid, carts)
    println("part2=" + sole.col + "," + sole.row)
}

private fun doNothingForward(d: DirectedCoord) = d

private fun List<Cart>.sortCarts() = sortedWith(compareBy({ it.pos.coord.row }, { it.pos.coord.col })).toMutableList()
private data class Cart(val pos: DirectedCoord, val intersection: (DirectedCoord) -> DirectedCoord = DirectedCoord::left) {
    fun move(grid: Map<Coord, Char>) = when (val cur = grid.getValue(pos.coord)) {
        in listOf('|', '-') -> copy(pos = pos.forward())
        '+' -> moveOnIntersection()
        else -> moveOnCorner(cur)
    }

    fun moveOnIntersection() = Cart(
        pos = intersection(pos).forward(),
        intersection = when (intersection) {
            DirectedCoord::left -> ::doNothingForward
            ::doNothingForward -> DirectedCoord::right
            else -> DirectedCoord::left
        }
    )

    fun moveOnCorner(corner: Char): Cart {
        val newFacing = when {
            corner == '/' && pos.facing in listOf(Facing.UP, Facing.DOWN) -> pos.facing.right()
            corner == '/' && pos.facing in listOf(Facing.LEFT, Facing.RIGHT) -> pos.facing.left()
            corner == '\\' && pos.facing in listOf(Facing.UP, Facing.DOWN) -> pos.facing.left()
            corner == '\\' && pos.facing in listOf(Facing.LEFT, Facing.RIGHT) -> pos.facing.right()
            else -> throw IllegalArgumentException("Don't know what to do for char=$corner")
        }
        return Cart(pos.copy(facing = newFacing).forward(), intersection)
    }
}

private fun iterateUntilCrash(grid: Map<Coord, Char>, inCarts: List<Cart>): Coord {
    var carts = inCarts.sortCarts()
    while (true) {
        val newCarts = mutableListOf<Cart>()
        while (carts.isNotEmpty()) {
            val newCart = carts.removeFirst().move(grid)
            if (newCart.pos.coord in (carts + newCarts).map { it.pos.coord }) {
                return newCart.pos.coord
            }
            newCarts += newCart
        }
        carts = newCarts.sortCarts()
    }
}

private fun iterateUntilSoleSurvivor(grid: Map<Coord, Char>, inCarts: List<Cart>): Coord {
    var carts = inCarts.sortCarts()
    while (carts.size > 1) {
        val newCarts = mutableListOf<Cart>()
        while (carts.isNotEmpty()) {
            val newCart = carts.removeFirst().move(grid)
            if (newCart.pos.coord in (carts + newCarts).map { it.pos.coord }) {
                carts.removeIf { it.pos.coord == newCart.pos.coord }
                newCarts.removeIf { it.pos.coord == newCart.pos.coord }
            } else {
                newCarts += newCart
            }
        }
        carts = newCarts.sortCarts()
    }
    return carts.first().pos.coord
}
