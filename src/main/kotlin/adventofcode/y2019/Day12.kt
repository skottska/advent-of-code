package adventofcode.y2019 // ktlint-disable filename

import adventofcode.Coord3D
import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val moons = readFile("src/main/resources/y2019/day12.txt").map { line -> matchNumbers(line).let { Coord3D(it[0], it[1], it[2]) } to Coord3D(0, 0, 0) }

    val part1 = (1..1000).fold(moons) { total, _ -> moveMoons(total) }
    println("part1=" + part1.sumOf { it.first.absSumOfCoords() * it.second.absSumOfCoords() })

    var newMoons = moveMoons(moons)
    var count = 1L
    val minMap = mutableMapOf<Char, Long>()
    while (minMap.size != 3) {
        newMoons = moveMoons(newMoons); count++
        if (!minMap.contains('x') && newMoons.zip(moons).all { it.first.first.x == it.second.first.x } && newMoons.all { it.second.x == 0 }) minMap['x'] = count
        if (!minMap.contains('y') && newMoons.zip(moons).all { it.first.first.y == it.second.first.y } && newMoons.all { it.second.y == 0 }) minMap['y'] = count
        if (!minMap.contains('z') && newMoons.zip(moons).all { it.first.first.z == it.second.first.z } && newMoons.all { it.second.z == 0 }) minMap['z'] = count
    }
    println("part2=" + lcm(minMap.values.toList()).fold(1L) { total, i -> total * i })
}

private fun lcm(l: List<Long>, fromPrime: Long = 2L): List<Long> {
    if (l.all { it == 1L }) return emptyList()
    val primes = primes(l.max()).filter { it >= fromPrime }
    val lowest = primes.first { prime -> l.any { it % prime == 0L } }
    return listOf(lowest) + lcm(l.map { if (it % lowest == 0L) it / lowest else it }, lowest)
}

private val primes = mutableListOf(2L)
private fun primes(max: Long): List<Long> {
    var cur = primes.max() + 1L
    while (cur < max) {
        if (primes.none { cur % it == 0L }) primes.add(cur)
        cur++
    }
    return primes
}

private fun moveMoons(moons: List<Pair<Coord3D, Coord3D>>) = moons.map { moon ->
    val otherMoons = moons.filterNot { it == moon }
    val second = moon.second.copy(
        x = moon.second.x + otherMoons.count { it.first.x > moon.first.x } - otherMoons.count { it.first.x < moon.first.x },
        y = moon.second.y + otherMoons.count { it.first.y > moon.first.y } - otherMoons.count { it.first.y < moon.first.y },
        z = moon.second.z + otherMoons.count { it.first.z > moon.first.z } - otherMoons.count { it.first.z < moon.first.z }
    )
    moon.first.copy(
        x = moon.first.x + second.x,
        y = moon.first.y + second.y,
        z = moon.first.z + second.z
    ) to second
}
