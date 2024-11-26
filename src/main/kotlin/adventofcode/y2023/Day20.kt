package adventofcode.y2023 // ktlint-disable filename

import adventofcode.readFile
import java.lang.invoke.MethodHandles

fun main() {
    val lines = readFile(MethodHandles.lookup()).map { line ->
        line.substring(0, line.indexOf(' ')) to line.substring(line.indexOf('>') + 2).split(",").map { it.trim() }
    }
    val modules = lines.associate { line ->
        when (line.first.first()) {
            'b' -> "broadcaster" to Broadcaster(line.second)
            '%' -> line.first.drop(1).let { it to FlipFlop(it, line.second) }
            else -> line.first.drop(1).let { name ->
                name to Conjunction(
                    name = name,
                    tos = line.second,
                    fromMap = lines.filter { it.second.contains(name) }.map { it.first.drop(1) }.associateWith { false }.toMutableMap()
                )
            }
        }
    }

    println("part1=" + sendPulse(modules))
    println("part2=" + sendPulsePart2(modules))
}

private fun sendPulse(modulesIn: Map<String, Module>): Long {
    val modules = modulesIn.toMutableMap()
    var low = 0L
    var high = 0L
    repeat(1000) {
        var pulses = listOf(PulseModule("button", "broadcaster", false))
        while (pulses.isNotEmpty()) {
            low += pulses.count { !it.pulse }
            high += pulses.count { it.pulse }
            pulses = pulses.flatMap { pulseModule ->
                modules[pulseModule.to]?.let {
                    val result = it.pulse(pulseModule.from, pulseModule.pulse)
                    modules[pulseModule.to] = result.first
                    result.second
                } ?: emptyList()
            }
        }
    }
    return low * high
}

private fun sendPulsePart2(modulesIn: Map<String, Module>): Long {
    val modules = modulesIn.toMutableMap()
    val outputters = listOf("sr", "sn", "rf", "vq")
    val outputtersMap = mutableMapOf<String, Long>()
    var presses = 0L
    while (outputtersMap.size < outputters.size) {
        presses++
        var pulses = listOf(PulseModule("button", "broadcaster", false))
        while (pulses.isNotEmpty()) {
            pulses = pulses.flatMap { pulseModule ->
                if (pulseModule.from in outputters && pulseModule.from !in outputtersMap.keys && pulseModule.pulse) outputtersMap[pulseModule.from] = presses
                modules[pulseModule.to]?.let {
                    val result = it.pulse(pulseModule.from, pulseModule.pulse)
                    modules[pulseModule.to] = result.first
                    result.second
                } ?: emptyList()
            }
        }
    }
    return outputtersMap.values.fold(1L) { total, i -> total * i }
}

private data class PulseModule(val from: String, val to: String, val pulse: Boolean)

private interface Module {
    fun pulse(from: String, isHigh: Boolean): Pair<Module, List<PulseModule>>
}
private data class Broadcaster(val tos: List<String>) : Module {
    override fun pulse(from: String, isHigh: Boolean) = this to tos.map { PulseModule("broadcaster", it, isHigh) }
}

private data class FlipFlop(val name: String, val tos: List<String>, val status: Boolean = false) : Module {
    override fun pulse(from: String, isHigh: Boolean) = when {
        isHigh -> this to emptyList()
        status -> FlipFlop(name, tos, !status) to tos.map { PulseModule(name, it, false) }
        else -> FlipFlop(name, tos, !status) to tos.map { PulseModule(name, it, true) }
    }
}

private data class Conjunction(val name: String, val tos: List<String>, val fromMap: Map<String, Boolean>) : Module {
    override fun pulse(from: String, isHigh: Boolean): Pair<Module, List<PulseModule>> {
        val newMap = fromMap.toMutableMap()
        newMap[from] = isHigh
        val pulse = !newMap.values.all { it }
        return Conjunction(name, tos, newMap) to tos.map { PulseModule(name, it, pulse) }
    }
}
