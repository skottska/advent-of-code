package adventofcode.y2022

import adventofcode.readFile
import adventofcode.split

fun main() {
    val lines = readFile("src/main/resources/y2022/day07.txt")

    var currentPos = JDirectory("/", null, mutableListOf())
    val topLevel = currentPos.copy()

    var index = 0
    while (index != lines.size) {
        val line = split(lines[index++])
        if (line[1] == "ls") {
            while (index != lines.size) {
                val lsline = split(lines[index++])
                if (lsline.isCommand()) {
                    index--
                    break
                }
                if (lsline[0] == "dir") currentPos.files.add(JDirectory(lsline[1], currentPos, mutableListOf()))
                else currentPos.files.add(JRawFile(lsline[1], currentPos, lsline[0].toInt()))
            }
        } else if (line[1] == "cd") {
            if (line[2] == "..") {
                currentPos = currentPos.parent ?: topLevel
            } else {
                currentPos = currentPos.files.filterIsInstance<JDirectory>().first { it.name == line[2] }
            }
        }
    }
    println("part1=" + topLevel.sumSmallDirs())

    val spaceToFind = 30_000_000 - (70_000_000 - topLevel.size())

    val map = mutableMapOf<String, Int>()
    topLevel.addSize(map)
    val result = map.filter { it.value >= spaceToFind }.minBy { it.value }
    println("part2=${result.value}")
}

fun List<String>.isCommand() = first() == "$"

interface JFile {
    val name: String
    val parent: JDirectory?

    fun absoluteName(): String = (parent?.absoluteName() ?: "") + name
    fun size(): Int
}

data class JRawFile(override val name: String, override val parent: JDirectory?, val size: Int) : JFile {
    override fun toString() = "RAW $name"
    override fun size() = size
}
data class JDirectory(
    override val name: String,
    override val parent: JDirectory?,
    val files: MutableList<JFile>
) : JFile {
    override fun toString() = "DIR $name files=$files"
    override fun size() = files.sumOf { it.size() }
    fun sumSmallDirs(): Int {
        val mySize = if (size() <= 100000) size() else 0
        return mySize + files.filterIsInstance<JDirectory>().sumOf { it.sumSmallDirs() }
    }

    fun addSize(map: MutableMap<String, Int>) {
        map[absoluteName()] = size()
        files.filterIsInstance<JDirectory>().forEach { it.addSize(map) }
    }
}
