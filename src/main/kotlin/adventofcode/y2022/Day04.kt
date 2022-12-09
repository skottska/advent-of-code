package adventofcode.y2022

import adventofcode.readFile

fun main(args: Array<String>) {
    val lines = readFile("src/main/resources/y2022/day04.txt")
    println("part1=" + lines.filter { overlaps(it) }.size)
    println("part2=" + lines.filter { overlapsAtAll(it) }.size)
}

fun overlaps(line: String) = splitLine(line).let { splitElf(it[0]).overlaps(splitElf(it[1])) }
fun overlapsAtAll(line: String) = splitLine(line).let { splitElf(it[0]).overlapsAtAll(splitElf(it[1])) }

fun splitLine(line: String) = line.split(",+".toRegex())
fun splitElf(elf: String) = elf.split("-+".toRegex()).let { ElfDay4(it[0].toInt(), it[1].toInt()) }

data class ElfDay4(val start: Int, val end: Int) {
    fun overlaps(other: ElfDay4) = when {
        start >= other.start && end <= other.end -> true
        other.start >= start && other.end <= end -> true
        else -> false
    }

    fun overlapsAtAll(other: ElfDay4) = when {
        other.start in start..end -> true
        other.end in start..end -> true
        start in other.start..other.end -> true
        end in other.start..other.end -> true
        else -> false
    }
}
