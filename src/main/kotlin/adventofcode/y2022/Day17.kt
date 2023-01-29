package adventofcode.y2022 // ktlint-disable filename

import adventofcode.readFile

fun main() {
    println("part1=" + iterateBlocks(2022))
    println("part2=" + iterateBlocks(1000000000000L))
}

private fun iterateBlocks(maxIters: Long): Long {
    val jets = Jets(readFile("src/main/resources/y2022/day17.txt")[0].map { it })
    val cave = Cave()
    val blocks = Blocks()
    val caveInstances = mutableMapOf<CaveInstance, IterationInstance>()
    var caveInstance: CaveInstance
    var i = 1L
    while (true) {
        cave.merge(moveBlock(jets, blocks.getBlock().start(cave.highestBlock()), cave))

        val maxColumnHeight = cave.maxColumnHeight()
        caveInstance = CaveInstance(jets.jetIndex, blocks.blockIndex, maxColumnHeight.map { it - maxColumnHeight.min() })
        if (caveInstances.contains(caveInstance)) {
            break
        }
        caveInstances[caveInstance] = IterationInstance(i++, maxColumnHeight)
    }
    val baseIter = caveInstances.getValue(caveInstance).iteration
    val loopIter = i - baseIter
    val leftOverIter = (maxIters - baseIter) % loopIter
    val maxColumn = getIterationInstance(caveInstances, baseIter + leftOverIter).columnHeights.let { it.indexOf(it.max()) }
    val columnHeight = { iter: Long -> getIterationInstance(caveInstances, iter).columnHeights[maxColumn] }

    return columnHeight(baseIter + leftOverIter) + (cave.maxColumnHeight()[maxColumn] - columnHeight(baseIter)) * ((maxIters - baseIter) / loopIter)
}

private fun getIterationInstance(caveInstances: Map<CaveInstance, IterationInstance>, iter: Long) = caveInstances.filter { it.value.iteration == iter }.values.first()

private fun moveBlock(jets: Jets, block: Block, cave: Cave): Block {
    val newBlock = when (jets.getJet()) {
        '>' -> block.move(1, 0).let { if (cave.clashes(it)) block else it }
        else -> block.move(-1, 0).let { if (cave.clashes(it)) block else it }
    }
    return newBlock.move(0, -1).let { if (cave.clashes(it)) newBlock else moveBlock(jets, it, cave) }
}

private data class Jets(val jets: List<Char>) {
    var jetIndex = 0
    fun getJet(): Char { if (jetIndex >= jets.size) jetIndex = 0; return jets[jetIndex++] }
}

private class Blocks {
    val blocks = listOf(
        Block(listOf(3L to 4L, 4L to 4L, 5L to 4L, 6L to 4L)),
        Block(listOf(4L to 6L, 3L to 5L, 4L to 5L, 5L to 5L, 4L to 4L)),
        Block(listOf(5L to 6L, 5L to 5L, 3L to 4L, 4L to 4L, 5L to 4L)),
        Block(listOf(3L to 7L, 3L to 6L, 3L to 5L, 3L to 4L)),
        Block(listOf(3L to 5L, 4L to 5L, 3L to 4L, 4L to 4L))
    )

    var blockIndex = 0

    fun getBlock(): Block { if (blockIndex >= blocks.size) blockIndex = 0; return blocks[blockIndex++] }
}
private data class Block(val rocks: List<Pair<Long, Long>>) {
    fun start(heightAbove: Long) = Block(rocks.map { it.first to it.second + heightAbove })
    fun move(xMove: Int, yMove: Int) = Block(rocks.map { it.first + xMove to it.second + yMove })
}

private data class Cave(val space: MutableList<Pair<Long, Long>> = mutableListOf()) {
    fun highestBlock() = space.maxOfOrNull { it.second } ?: 0L
    fun clashes(block: Block) = when {
        block.rocks.any { it.first <= 0 || it.first > 7 || it.second <= 0 } -> true
        else -> block.rocks.any { space.contains(it) }
    }
    fun merge(block: Block) {
        space.addAll(block.rocks)
        val maxColumnHeight = space.groupBy { it.first }.map { col -> col.value.maxOf { it.second } }
        if (maxColumnHeight.size == 7) {
            space.removeIf { it.second < maxColumnHeight.min() - 1 }
        }
    }
    fun maxColumnHeight() = space.groupBy { it.first }.map { col -> col.value.maxOf { it.second } }
}

private data class CaveInstance(val jetIndex: Int, val blockIndex: Int, val columnDiffs: List<Long>)
private data class IterationInstance(val iteration: Long, val columnHeights: List<Long>)
