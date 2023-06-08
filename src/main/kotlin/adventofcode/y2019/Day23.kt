package adventofcode.y2019 // ktlint-disable filename

import adventofcode.matchNumbersToBigInt
import adventofcode.readFile
import java.math.BigInteger
import kotlin.concurrent.thread
import kotlin.system.exitProcess

fun main() {
    val line = matchNumbersToBigInt(readFile("src/main/resources/y2019/day23.txt")[0])
    val nics = 0..49
    val buffers = nics.associateWith { mutableListOf(it.toBigInteger()) } + mapOf(255 to mutableListOf())

    thread {
        var part1Complete = false
        val deliveredYs = mutableListOf<BigInteger>()
        while (true) {
            Thread.sleep(100)
            val buffer = buffers.getValue(255)
            if (buffer.isNotEmpty() && !part1Complete) {
                part1Complete = true
                println("part1=" + buffer[1])
            }
            val allBuffersEmpty = buffers.filter { it.key in nics }.all { it.value.isEmpty() }
            if (allBuffersEmpty && buffer.isNotEmpty()) {
                if (deliveredYs.isNotEmpty() && deliveredYs.last() == buffer.last()) {
                    println("part2=" + buffer.last())
                    exitProcess(0)
                }
                deliveredYs.add(buffer.last())
                buffers.getValue(0).addAll(buffer.takeLast(2))
            }
        }
    }

    nics.map { nicId ->
        thread {
            var programContext = ProgramContext()
            val program = line.toMutableList()
            while (true) {
                programContext = runIntCodeProgramMutable(program, buffers.getValue(nicId), programContext)
                val receiver = programContext.output?.toInt()
                programContext = runIntCodeProgramMutable(program, buffers.getValue(nicId), programContext)
                val x = programContext.output
                programContext = runIntCodeProgramMutable(program, buffers.getValue(nicId), programContext)
                val y = programContext.output
                if (receiver == null || x == null || y == null) throw IllegalArgumentException("Something is null receiver=$receiver x=$x y=$y")
                buffers.getValue(receiver).addAll(listOf(x, y))
            }
        }
    }
}
