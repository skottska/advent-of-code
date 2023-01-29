package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matches
import adventofcode.readFile
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lines = readFile("src/main/resources/y2015/day22.txt")
    val boss = lines.map { matches(it, "[0-9]+").first().toInt() }.let { Boss(it[0], it[1]) }
    val spells = listOf(
        TurnsSpell(229, 5, { fight: Fight -> fight.increaseMana(101) }),
        TurnsSpell(113, 6, { fight: Fight -> fight.adjustArmour(7) }, { fight: Fight -> fight.adjustArmour(0) }),
        DirectSpell(53) { fight: Fight -> fight.damageBoss(4)},
        DirectSpell(73) { fight: Fight -> fight.drain(2)},
        TurnsSpell(173, 6, { fight: Fight -> fight.damageBoss(3) }),
    )
    iterateFight(Fight(Wizard(50, 500), boss), spells, false)
    println("part1=$minMana")

    minMana = null
    iterateFight(Fight(Wizard(50, 500), boss), spells, true)
    println("part1=$minMana")
}
private var minMana: Int? = null

private fun iterateFight(fight: Fight, spells: List<Spell>, isPart2: Boolean) {
    if (minMana?.let { it <= fight.manaSpent } == true) return
    val started = fight.startTurn(isPlayer = isPart2)
    if (!started.ongoing()) {
        updateMana(started)
        return
    }
    spells.forEach { spell -> castSpell(started, spell, spells, isPart2) }
}

private fun castSpell(fight: Fight, spell: Spell, spells: List<Spell>, isPart2: Boolean) {
    if (fight.canCastSpell(spell)) {
        val wizardAttack = fight.applySpell(spell)
        if (!wizardAttack.ongoing()) {
            updateMana(wizardAttack)
            return
        }
        val middle = wizardAttack.startTurn()
        if (!middle.ongoing()) {
            updateMana(middle)
            return
        }
        val bossAttack = middle.damageWizard(fight.boss.damage)
        if (!bossAttack.ongoing()) {
            return // Lost
        }
        iterateFight(bossAttack, spells, isPart2)
    }
}

private fun updateMana(fight: Fight) { if (fight.wonFight()) {
    minMana = minMana?.let { min(it, fight.manaSpent) } ?: fight.manaSpent }
}

private sealed interface Spell {val mana: Int }
private data class DirectSpell(override val mana: Int, val castSpell: (Fight) -> Fight): Spell
private data class TurnsSpell(override val mana: Int, val turns: Int, val castSpell: (Fight) -> Fight, val end: (Fight) -> Fight = { fight: Fight -> fight}): Spell

private data class Fight(val wizard: Wizard, val boss: Boss, val activeSpells: List<TurnsSpell> = listOf(), val manaSpent: Int = 0) {
    fun damageBoss(damage: Int) = copy(boss = boss.copy(hp = boss.hp - damage))
    fun damageWizard(damage: Int) = copy(wizard = wizard.copy(hp = wizard.hp - (max(1, damage - wizard.armour))))
    fun drain(hp: Int) = copy(wizard = wizard.copy(hp = wizard.hp + hp), boss = boss.copy(hp = boss.hp - hp))
    fun increaseMana(mana: Int) = copy(wizard = wizard.copy(mana = wizard.mana + mana))
    fun adjustArmour(amount: Int) = copy(wizard = wizard.copy(armour = amount))
    fun applySpell(spell: Spell) = when (spell) {
        is DirectSpell -> spell.castSpell(this).let { it.copy(manaSpent = manaSpent + spell.mana, wizard = it.wizard.copy(mana = it.wizard.mana - spell.mana)) }
        is TurnsSpell -> copy(activeSpells = activeSpells + listOf(spell), wizard = wizard.copy(mana = wizard.mana - spell.mana), manaSpent = manaSpent + spell.mana)
    }

    fun startTurn(isPlayer: Boolean = false): Fight {
        val newActiveSpells = activeSpells.map { spell -> spell.copy(turns =  spell.turns - 1) }
        return newActiveSpells.fold(copy(activeSpells = newActiveSpells)) {
                result, spell -> if (spell.turns == 0) spell.castSpell(result).let { spell.end(it) } else spell.castSpell(result)
        }.let { it.copy(activeSpells = it.activeSpells.filter { a -> a.turns > 0 }) }
            .let { if(isPlayer) it.copy(wizard = it.wizard.copy(hp = it.wizard.hp - 1)) else it }
    }

    fun ongoing() = wizard.hp > 0 && boss.hp > 0
    fun wonFight() = boss.hp <= 0
    fun canCastSpell(spell: Spell): Boolean {
        val canAfford = wizard.mana >= spell.mana
        return when (spell) {
            is DirectSpell -> canAfford
            is TurnsSpell -> canAfford && activeSpells.none { it.mana == spell.mana }
        }
    }

}
private data class Wizard(val hp: Int, val mana: Int, val armour: Int = 0)
private data class Boss(val hp: Int, val damage: Int)

