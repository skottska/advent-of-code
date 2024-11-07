package adventofcode.y2021 // ktlint-disable filename

import adventofcode.binaryToInt
import adventofcode.concat
import adventofcode.hexToBinary
import adventofcode.readFile
import java.lang.IllegalArgumentException

fun main() {
    val binaryReader = readFile("src/main/resources/y2021/day16.txt").first().map {
        padWithZeros(it.toString().hexToBinary())
    }.concat().let { BinaryReader(it) }
    val packet = parsePacket(binaryReader)
    println("part1=" + packet.sumVersions())
    println("part2=" + packet.value())
}

private fun padWithZeros(s: String) = (s.length until 4).fold(s) { total, _ -> "0$total" }

private fun parsePacket(binaryReader: BinaryReader): Packet {
    val version = binaryReader.readInt(3)
    return when (val type = binaryReader.readInt(3)) {
        4 -> parseLiteralPacket(binaryReader, version, type)
        else -> parseOperatorPacket(binaryReader, version, type)
    }
}

private fun parseOperatorPacket(binaryReader: BinaryReader, version: Int, type: Int): OperatorPacket {
    val lengthType = binaryReader.readInt(1)
    val packets = mutableListOf<Packet>()
    if (lengthType == 0) {
        val length = binaryReader.readInt(15)
        val endOfPacket = binaryReader.pos + length
        while (binaryReader.pos < endOfPacket) { packets += parsePacket(binaryReader) }
    } else {
        val numSubPackets = binaryReader.readInt(11)
        packets.addAll((1..numSubPackets).map { parsePacket(binaryReader) })
    }
    return OperatorPacket(version, type, packets)
}

private fun parseLiteralPacket(binaryReader: BinaryReader, version: Int, type: Int): LiteralPacket {
    var binaryNumber = ""
    var endBit = false
    while (!endBit) {
        endBit = binaryReader.readInt(1) == 0
        binaryNumber += binaryReader.read(4)
    }
    return LiteralPacket(version, type, binaryNumber.binaryToInt().toLong())
}
private data class BinaryReader(val binary: String) {
    var pos = 0
    fun read(num: Int) = binary.substring(pos, pos + num).also { pos += num }
    fun readInt(num: Int) = read(num).binaryToInt().toInt()
}
private interface Packet {
    val version: Int; val type: Int
    fun sumVersions(): Int
    fun value(): Long
}
private data class LiteralPacket(override val version: Int, override val type: Int, val literal: Long) : Packet {
    override fun sumVersions(): Int = version
    override fun value(): Long = literal
}
private data class OperatorPacket(override val version: Int, override val type: Int, val packets: List<Packet>) : Packet {
    override fun sumVersions(): Int = version + packets.sumOf { it.sumVersions() }
    override fun value(): Long {
        val values = packets.map { it.value() }
        return when (type) {
            0 -> values.sum()
            1 -> values.fold(1L) { total, i -> total * i }
            2 -> values.min()
            3 -> values.max()
            5 -> if (values.first() > values.last()) 1L else 0L
            6 -> if (values.first() < values.last()) 1L else 0L
            7 -> if (values.first() == values.last()) 1L else 0L
            else -> throw IllegalArgumentException("Unknown type $type")
        }
    }
}
