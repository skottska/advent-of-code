package adventofcode.y2022 // ktlint-disable filename

import adventofcode.readFile

fun main(args: Array<String>) {
    val jets = Jets(readFile("src/main/resources/y2022/day17.txt")[0].map { it })
    val cave = Cave()
    val blocks = Blocks()
    var time = System.currentTimeMillis()
    val caveInstances = mutableSetOf<CaveInstance>()
    (1L..2022L).forEach { i ->
        val block = moveBlock(jets, blocks.getBlock().start(cave.highestBlock()), cave)
        //println(block)
        if (i % 10000000L == 0L) System.currentTimeMillis().let { println("perc="+(1000000000000L/i)+" time="+(System.currentTimeMillis() - time)+" cave="+cave.space.size); time = it }
        cave.merge(block)

        val maxColumnHeight = cave.space.groupBy { it.first }.map { col -> col.value.maxOf { it.second } }
        val columnDiffs = maxColumnHeight.map { it - maxColumnHeight.min()}
        val caveInstance = CaveInstance(jets.jetIndex, blocks.blockIndex, columnDiffs)
        if (caveInstances.contains(caveInstance)) {
            println(caveInstance)
        }
        caveInstances.add(caveInstance)
    }
    println(cave.highestBlock())
}

fun part1() {
    val jets = Jets(readFile("src/main/resources/y2022/day17.txt")[0].map { it })
    val cave = Cave()
    val blocks = Blocks()
    var time = System.currentTimeMillis()
    val caveInstances = mutableSetOf<CaveInstance>()
    (1L..2022L).forEach { i ->
        val block = moveBlock(jets, blocks.getBlock().start(cave.highestBlock()), cave)
        //println(block)
        if (i % 10000000L == 0L) System.currentTimeMillis().let { println("perc="+(1000000000000L/i)+" time="+(System.currentTimeMillis() - time)+" cave="+cave.space.size); time = it }
        cave.merge(block)

        val maxColumnHeight = cave.space.groupBy { it.first }.map { col -> col.value.maxOf { it.second } }
        val columnDiffs = maxColumnHeight.map { it - maxColumnHeight.min()}
        val caveInstance = CaveInstance(jets.jetIndex, blocks.blockIndex, columnDiffs)
        if (caveInstances.contains(caveInstance)) {
            println(caveInstance)
        }
        caveInstances.add(caveInstance)
    }
    println(cave.highestBlock())
}

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
    fun move(xMove: Int, yMove:Int) = Block(rocks.map { it.first + xMove to it.second + yMove })
}

private data class Cave(val space: MutableList<Pair<Long, Long>> = mutableListOf()) {
    fun highestBlock() = space.maxOfOrNull { it.second } ?: 0L
    fun clashes(block: Block) = when {
        block.rocks.any { it.first <= 0 || it.first > 7 || it.second <= 0} -> true
        else -> block.rocks.any { space.contains(it) }
    }
    fun merge(block: Block) {
        space.addAll(block.rocks)
        val maxColumnHeight = space.groupBy { it.first }.map { col -> col.value.maxOf { it.second } }
        if (maxColumnHeight.size == 7) {
            space.removeIf { it.second < maxColumnHeight.min() - 1 }
        }
    }
}

private data class CaveInstance(val jetIndex: Int, val blockIndex: Int, val columnDiffs: List<Long>)
