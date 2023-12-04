package adventofcode.y2017

import kotlin.math.min

class Year2017 {
    companion object {
        fun knotHash(input: String, numRounds: Int = 64, appendSuffix: Boolean = true): String {
            val toBeHashed = input.map { it.code } + if (appendSuffix) listOf(17, 31, 73, 47, 23) else emptyList()
            var index = 0
            var skip = 0
            val list = (0..255).toMutableList()
            (1..numRounds).forEach { _ ->
                toBeHashed.forEach { i ->
                    val preSub = list.subList(index, min(list.size, index + i))
                    val postSub = if (index + i < list.size) emptyList() else list.subList(0, (index + i) % list.size)
                    (preSub + postSub).reversed().forEachIndexed { subIndex, subI -> list[(index + subIndex) % list.size] = subI }
                    index = (index + i + skip++) % list.size
                }
            }
            return list.windowed(size = 16, step = 16) { it.reduce { acc, i -> acc xor i } }
                .map { it.toString(16) }
                .map {
                    when (it.length) {
                        0 -> "00"
                        1 -> "0$it"
                        else -> it
                    }
                }
                .reduce { acc, i -> acc + i }
        }
    }
}
