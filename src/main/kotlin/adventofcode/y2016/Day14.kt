package adventofcode.y2016 // ktlint-disable filename

import adventofcode.md5
import adventofcode.readFile

fun main() {
    val salt = readFile("src/main/resources/y2016/day14.txt")[0]
    println("part1=" + iterate(salt, numMd5s = 1))
    println("part2=" + iterate(salt, numMd5s = 2017))
}

private fun iterate(salt: String, numMd5s: Int): Int {
    val triples = mutableMapOf<Int, Char>()
    val quints = mutableMapOf<Int, Char>()
    var pos = 0
    while (true) {
        val md5 = (1..numMd5s).fold(salt + pos) { total, _ -> md5(total) }
        mults(md5, 5)?.let {
            triples[pos] = it
            quints[pos] = it
            val res = triples.filter { t -> quints.any { q -> t.value == q.value && q.key > t.key && q.key <= t.key + 1000 } }.keys
            if (res.size >= 64) return res.toList()[63]
        } ?: mults(md5, 3)?.let {
            triples[pos] = it
        }
        pos++
    }
}

private fun mults(s: String, n: Int): Char? {
    s.windowed(size = n, step = 1).forEach {
        if (it.toSet().size == 1) return it.first()
    }
    return null
}
