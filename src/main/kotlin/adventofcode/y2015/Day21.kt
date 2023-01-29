package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.maxOfNull
import adventofcode.minOfNull
import adventofcode.readFile
import kotlin.math.max

fun main() {
    val lines = readFile("src/main/resources/y2015/day21.txt")
    val boss = (0..2).map { matches(lines[it], "[0-9]+").first().toInt() }.let { Fighter(it[0], it[1], it[2]) }
    println("part1=" + iterateFight { you, cost -> if (winFight(you, boss)) minOfNull(cost, you.cost) else cost })
    println("part2=" + iterateFight { you, cost -> if (!winFight(you, boss)) maxOfNull(cost, you.cost) else cost })
}

private fun iterateFight(func: (Fighter, Int?) -> Int?): Int? {
    val emptyItem = Item(0, 0, 0)
    val weapons = listOf(8, 10, 25, 40, 74).mapIndexed { index, i -> Item(i, index + 4, 0) }
    val armour = listOf(13, 31, 53, 75, 102).mapIndexed { index, i -> Item(i, 0, index + 1) } + emptyItem
    val rings = listOf(25, 50, 100).mapIndexed { index, i -> Item(i, index + 1, 0) } +
        listOf(20, 40, 80).mapIndexed { index, i -> Item(i, 0, index + 1) } + emptyItem + emptyItem

    val you = Fighter(100, 0, 0)
    var coins: Int? = null
    weapons.forEach { weapon ->
        armour.forEach { armour ->
            rings.forEach { ring1 ->
                rings.forEach { ring2 ->
                    if (ring2 == emptyItem || ring2 != ring1) {
                        val newYou = you.addItem(weapon).addItem(armour).addItem(ring1).addItem(ring2)
                        coins = func(newYou, coins)
                    }
                }
            }
        }
    }
    return coins
}
private data class Item(val cost: Int, val damage: Int, val armour: Int)
private data class Fighter(val hp: Int, val damage: Int, val armour: Int, val cost: Int = 0) {
    fun addItem(item: Item) = copy(damage = damage + item.damage, armour = armour + item.armour, cost = cost + item.cost)
}
private fun winFight(you: Fighter, boss: Fighter): Boolean {
    val newBoss = dealDamage(you, boss).also { if (it.hp <= 0) return true }
    val newYou = dealDamage(boss, you).also { if (it.hp <= 0) return false }
    return winFight(newYou, newBoss)
}
private fun dealDamage(from: Fighter, to: Fighter) = to.copy(hp = to.hp - max(from.damage - to.armour, 1))
