package adventofcode.y2022 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import kotlin.math.min

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day19.txt")

    var totalPart1 = 0
    lines.forEachIndexed { index, line ->
        val m = matchNumbers(line)
        val robotBlueprints = listOf(OreRobot(m[1]), ClayRobot(m[2]), ObsidianRobot(m[3], m[4]), GeodeRobot(m[5], m[6]))
        iterate(robotBlueprints, listOf(robotBlueprints.first()), 24)
        totalPart1 += (index + 1) * maxGeodes
        maxGeodes = 0
        totalMineStates.clear()
    }
    println("part1=$totalPart1")

    var totalPart2 = 1
    (0..2).forEach {
        val m = matchNumbers(lines[it])
        val robotBlueprints = listOf(OreRobot(m[1]), ClayRobot(m[2]), ObsidianRobot(m[3], m[4]), GeodeRobot(m[5], m[6]))
        iterate(robotBlueprints, listOf(robotBlueprints.first()), 32)
        totalPart2 *= maxGeodes
        maxGeodes = 0
        totalMineStates.clear()
    }
    println("part2=$totalPart2")
}

private var maxGeodes = 0
private val totalMineStates = mutableSetOf<TotalMineState>()

private fun factorial(x: Int) = (2..x).fold(1) { total, i -> total * i }

private fun iterate(robotBlueprints: List<Robot>, robots: List<Robot>, iters: Int, mineState: MineState = MineState()) {
    val totalMineState = TotalMineState(mineState.prune(robotBlueprints), iters, robots)
    if (totalMineStates.contains(totalMineState)) return
    totalMineStates.add(totalMineState)

    if (iters == 0) { maxGeodes = maxOf(maxGeodes, mineState.geode); return }
    if (iters < 7 && mineState.geode + factorial(iters - 1 + robots.filterIsInstance<GeodeRobot>().size) < maxGeodes) return

    val producedMineState = robots.fold(mineState) { total, i -> i.produce(total) }

    val geodeRobot = robotBlueprints[3] as GeodeRobot
    val obsidianRobot = robotBlueprints[2] as ObsidianRobot
    val clayRobot = robotBlueprints[1] as ClayRobot
    val oreRobot = robotBlueprints[0] as OreRobot

    if (geodeRobot.canAfford(mineState)) {
        iterate(robotBlueprints, robots + listOf(geodeRobot), iters - 1, geodeRobot.buy(producedMineState))
    }

    val func = { r: Robot, cost: Int, amount: Int ->
        if (r.canAfford(mineState) && cost > robots.filter { it == r }.size && amount + robots.filter { it == r }.size * iters < cost * iters) {
            iterate(robotBlueprints, robots + listOf(r), iters - 1, r.buy(producedMineState))
        }
    }

    func(obsidianRobot, geodeRobot.obsidianCost, mineState.obsidian)
    func(clayRobot, obsidianRobot.clayCost, mineState.clay)
    func(oreRobot, robotBlueprints.maxOf { it.oreCost }, mineState.ore)

    iterate(robotBlueprints, robots, iters - 1, producedMineState)
}

private data class TotalMineState(val mineState: MineState, val iter: Int, val robots: List<Robot>)
private data class MineState(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0, val geode: Int = 0) {
    fun prune(robotBlueprints: List<Robot>) = MineState(
        ore = min(ore, robotBlueprints.maxOf { it.oreCost }),
        clay = min(clay, (robotBlueprints[2] as ObsidianRobot).clayCost),
        obsidian = min(obsidian, (robotBlueprints[3] as GeodeRobot).obsidianCost),
        geode = geode
    )
}
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
