package adventofcode.y2022 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day19.txt")
    val bestStates: MutableList<Int> = mutableListOf()
    var total = 0

    (0..2).forEach {
        val line = lines[it]
        val m = matchNumbers(line)
        val robotBlueprints = listOf(OreRobot(m[1]), ClayRobot(m[2]), ObsidianRobot(m[3], m[4]), GeodeRobot(m[5], m[6]))
        val robots: List<Robot> = listOf(robotBlueprints.first())
        iterate(robotBlueprints, robots, 32)
        println("i=" + it + " geodes=" + maxGeodes)
        maxGeodes = 0
        totalMineStates.clear()
    }
    println("part2=$total")

    lines.forEachIndexed { index, line ->
        val m = matchNumbers(line)
        val robotBlueprints = listOf(OreRobot(m[1]), ClayRobot(m[2]), ObsidianRobot(m[3], m[4]), GeodeRobot(m[5], m[6]))
        val robots: List<Robot> = listOf(robotBlueprints.first())
        iterate(robotBlueprints, robots, 24)
        bestStates.add(maxGeodes)
        println("i=" + (index + 1) + " " + maxGeodes)
        total += (index + 1) * maxGeodes
        maxGeodes = 0
        totalMineStates.clear()
    }
    println("part1=$total")
}

private var maxGeodes = 0
private val totalMineStates = mutableSetOf<TotalMineState>()

private fun factorial(x: Int) = (2..x).fold(1) { total, i -> total * i }

private fun iterate2(robotBlueprints: List<Robot>, robots: List<Robot>, iters: Int, mineState: MineState = MineState()) {
    val totalMineState = TotalMineState(mineState, iters, robots)
    if (totalMineStates.any { isBetter(it, totalMineState) }) {
        return
    }
    totalMineStates.add(totalMineState)

    if (iters < 4 && mineState.geode + factorial(iters + robots.filterIsInstance<GeodeRobot>().size) < maxGeodes) return
    if (iters == 0) {
        val temp = maxGeodes
        maxGeodes = maxOf(maxGeodes, mineState.geode)
        if (temp != maxGeodes) println("newmax=$maxGeodes mine=$mineState robots=" + robots.groupBy { it.javaClass.simpleName }.map { Pair(it.key, it.value.size) })
        return
    }
    val producedMineState = robots.fold(mineState) { total, i -> i.produce(total) }

    val affordable = robotBlueprints.filter { it.canAfford(mineState) }

    if (robotBlueprints[0].oreCost >= robotBlueprints[1].oreCost && robots.filterIsInstance<OreRobot>().size == 1) {
        return if (robotBlueprints[0].canAfford(mineState)) iterate2(robotBlueprints, robots + listOf(robotBlueprints[0]), iters - 1, robotBlueprints[0].buy(producedMineState))
        else iterate2(robotBlueprints, robots, iters - 1, producedMineState)
    }

    return if (affordable.isEmpty()) iterate2(robotBlueprints, robots, iters - 1, producedMineState)
    else iterate2(robotBlueprints, robots + listOf(affordable.last()), iters - 1, affordable.last().buy(producedMineState))
}

private fun iterate(robotBlueprints: List<Robot>, robots: List<Robot>, iters: Int, mineState: MineState = MineState()) {
    val totalMineState = TotalMineState(mineState, iters, robots)
    if (totalMineStates.any { isBetter(it, totalMineState) }) {
        return
    }
    totalMineStates.add(totalMineState)

    val index = 24 - iters + 1
    if (iters < 4 && mineState.geode + factorial(iters + robots.filterIsInstance<GeodeRobot>().size) < maxGeodes) return
    if (iters == 0) {
        val temp = maxGeodes
        maxGeodes = maxOf(maxGeodes, mineState.geode)
        if (temp != maxGeodes) println("newmax=$maxGeodes mine=$mineState robots=" + robots.groupBy { it.javaClass.simpleName }.map { Pair(it.key, it.value.size) })
        return
    }
    val producedMineState = robots.fold(mineState) { total, i -> i.produce(total) }
    val geode = robotBlueprints.last()
    if (geode.canAfford(mineState)) iterate(robotBlueprints, robots + listOf(geode), iters - 1, geode.buy(producedMineState))
    else {
        if (robotBlueprints[2].canAfford(mineState) && mineState.obsidian < (robotBlueprints[3] as GeodeRobot).obsidianCost * 2) {
            iterate(robotBlueprints, robots + listOf(robotBlueprints[2]), iters - 1, robotBlueprints[2].buy(producedMineState))
        }

        if (mineState.clay < (robotBlueprints[2] as ObsidianRobot).clayCost * 2 && robotBlueprints[1].canAfford(mineState)) {
            iterate(robotBlueprints, robots + listOf(robotBlueprints[1]), iters - 1, robotBlueprints[1].buy(producedMineState))
        }

        val minOre = robotBlueprints.maxOf { it.oreCost } + robotBlueprints[0].oreCost + 1

        if (mineState.ore < minOre * 2 && robotBlueprints[0].canAfford(mineState)) {
            iterate(robotBlueprints, robots + listOf(robotBlueprints[0]), iters - 1, robotBlueprints[0].buy(producedMineState))
        }

        if (!geode.canAfford(mineState) && mineState.ore < minOre) iterate(robotBlueprints, robots, iters - 1, producedMineState)
    }
}

private fun isBetter(a: TotalMineState, b: TotalMineState) = when {
    a.iter < b.iter -> false
    a.mineState.ore < b.mineState.ore -> false
    a.mineState.clay < b.mineState.clay -> false
    a.mineState.obsidian < b.mineState.obsidian -> false
    a.mineState.geode < b.mineState.geode -> false
    a.robots.filterIsInstance<OreRobot>().size < b.robots.filterIsInstance<OreRobot>().size -> false
    a.robots.filterIsInstance<ClayRobot>().size < b.robots.filterIsInstance<ClayRobot>().size -> false
    a.robots.filterIsInstance<ObsidianRobot>().size < b.robots.filterIsInstance<ObsidianRobot>().size -> false
    a.robots.filterIsInstance<GeodeRobot>().size < b.robots.filterIsInstance<GeodeRobot>().size -> false
    else -> true
}
private data class TotalMineState(val mineState: MineState, val iter: Int, val robots: List<Robot>)
private data class MineState(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0, val geode: Int = 0)
private sealed interface Robot {
    val oreCost: Int
    fun canAfford(mineState: MineState): Boolean
    fun buy(mineState: MineState): MineState
    fun produce(mineState: MineState): MineState
}
private data class OreRobot(override val oreCost: Int) : Robot {
    override fun canAfford(mineState: MineState) = mineState.ore >= oreCost
    override fun buy(mineState: MineState) = mineState.copy(ore = mineState.ore - oreCost)
    override fun produce(mineState: MineState) = mineState.copy(ore = mineState.ore + 1)
}
private data class ClayRobot(override val oreCost: Int) : Robot {
    override fun canAfford(mineState: MineState) = mineState.ore >= oreCost
    override fun buy(mineState: MineState) = mineState.copy(ore = mineState.ore - oreCost)
    override fun produce(mineState: MineState) = mineState.copy(clay = mineState.clay + 1)
}
private data class ObsidianRobot(override val oreCost: Int, val clayCost: Int) : Robot {
    override fun canAfford(mineState: MineState) = mineState.ore >= oreCost && mineState.clay >= clayCost
    override fun buy(mineState: MineState) = mineState.copy(ore = mineState.ore - oreCost, clay = mineState.clay - clayCost)
    override fun produce(mineState: MineState) = mineState.copy(obsidian = mineState.obsidian + 1)
}
private data class GeodeRobot(override val oreCost: Int, val obsidianCost: Int) : Robot {
    override fun canAfford(mineState: MineState) = mineState.ore >= oreCost && mineState.obsidian >= obsidianCost
    override fun buy(mineState: MineState) = mineState.copy(ore = mineState.ore - oreCost, obsidian = mineState.obsidian - obsidianCost)
    override fun produce(mineState: MineState) = mineState.copy(geode = mineState.geode + 1)
}
