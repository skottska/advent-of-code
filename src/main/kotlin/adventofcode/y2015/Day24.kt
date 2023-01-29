package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile

fun main() {
    val packages = readFile("src/main/resources/y2015/day24.txt").map { matchNumbers(it) }.flatten()
    println(arrangePackagesTriple(packages))
    println(arrangePackagesQuad(packages))
}

private fun arrangePackagesQuad(packages: List<Int>): Long?{
    val target = packages.sum() / 4
    (1..packages.size / 4).forEach { size ->
        val arrangements = arrangeCompartment(target, size, packages.reversed())
        arrangements.sortedBy { it.fold(1L) { total, i -> total * i } }.forEach { a ->
            if (arrangePackagesTriple(packages - a) != null) return a.fold(1L) { total, i -> total * i }
        }
    }
    return null
}

private fun arrangePackagesTriple(packages: List<Int>): Long?{
    val target = packages.sum() / 3
    (1..packages.size / 3).forEach { size ->
        val arrangements = arrangeCompartment(target, size, packages.reversed())
        arrangements.sortedBy { it.fold(1L) { total, i -> total * i } }.forEach { a ->
            if (canArrangeInTwoComps(target, packages - a, size)) return a.fold(1L) { total, i -> total * i }
        }
    }
    return null
}

private fun canArrangeInTwoComps(target: Int, packages: List<Int>, minGroup: Int): Boolean {
    (minGroup..(packages.size / 2)).forEach { size ->
            if (arrangeBothCompartment(target, size, packages.reversed())) return true
    }
    return false
}

private fun arrangeBothCompartment(target: Int, size: Int, packages: List<Int>): Boolean {
    packages.forEach { p ->
        if (p == target && size == 1 ) return true
        if (p < target && size > 1)  if (arrangeBothCompartment(target - p, size - 1, packages.filter { it < p })) return true
    }
    return false
}

private fun arrangeCompartment(target: Int, size: Int, packages: List<Int>, current: Set<Int> = emptySet()): Set<Set<Int>> {
    val set: MutableSet<Set<Int>> = mutableSetOf()
    packages.forEach { p ->
        if (p == target && size == 1) {
            set.add(current + setOf(p))
        }
        if (p < target && size > 1) set += arrangeCompartment(target - p, size - 1, packages.filter { it < p }, current + setOf(p))
    }
    return set
}
