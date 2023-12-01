package adventofcode.y2016 // ktlint-disable filename

import adventofcode.asString
import adventofcode.matchNumbers
import adventofcode.readFile
import adventofcode.split

fun main() {
    val lines = readFile("src/main/resources/y2016/day21.txt")
    println("part1=" + iterateCommands(lines, "abcdefgh"))
    val reverseLines = lines.reversed().map {
        it.replace("rotate right", "rotate LEFT")
            .replace("rotate left", "rotate RIGHT")
            .replace("move position", "move reversed")
            .replace("rotate based", "rotate unbased")
            .lowercase()
    }
    println("part2=" + iterateCommands(reverseLines, "fbgdceah"))
}

private fun iterateCommands(lines: List<String>, start: String) = lines.fold(start) { total, line ->
    val nums = matchNumbers(line)
    when {
        line.startsWith("rotate right") -> total.takeLast(nums[0]) + total.dropLast(nums[0])
        line.startsWith("rotate left") -> total.drop(nums[0]) + total.take(nums[0])
        line.startsWith("swap position") -> {
            val list = total.toList().toMutableList()
            list[nums[0]] = total[nums[1]]
            list[nums[1]] = total[nums[0]]
            list.asString()
        }
        line.startsWith("rotate based") -> rotateBased(total, line)
        line.startsWith("rotate unbased") -> {
            total.indices.map { total.takeLast(it) + total.dropLast(it) }.first {
                rotateBased(it, line) == total
            }
        }
        line.startsWith("reverse positions") -> {
            total.substring(0, nums[0]) + total.substring(nums[0], nums[1] + 1)
                .reversed() + total.substring(nums[1] + 1)
        }
        line.startsWith("swap letter") -> {
            val split = split(line)
            total.replace(split[2], "_").replace(split[5], split[2]).replace("_", split[5])
        }
        line.startsWith("move position") -> {
            total.removeRange(nums[0]..nums[0]).let { it.substring(0, nums[1]) + total[nums[0]] + it.substring(nums[1]) }
        }
        line.startsWith("move reversed") -> {
            total.removeRange(nums[1]..nums[1]).let { it.substring(0, nums[0]) + total[nums[1]] + it.substring(nums[0]) }
        }
        else -> throw IllegalArgumentException("Unknown command =>$line")
    }
}

private fun rotateBased(s: String, line: String): String {
    val index = s.indexOf(split(line).last().last())
    val rotation = when {
        index >= 4 -> index + 2
        else -> index + 1
    } % s.length
    return s.takeLast(rotation) + s.dropLast(rotation)
}
