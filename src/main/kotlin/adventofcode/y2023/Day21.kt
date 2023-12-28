import java.io.File
import kotlin.math.abs

fun main() {
  val grid = readFile("aoc21.txt").mapCoord()

  val start = grid.filter { it.value == 'S'}.keys.first()

  val odd = mutableSetOf<Coord>()
  val even = mutableSetOf<Coord>()

  (0..64).fold(setOf(start)) { cur, i -> 
    if (i % 2 == 0) even += cur
    else odd += cur
    cur.flatMap {it.around()}.filter { it !in even && it !in odd && grid.getOrDefault(it, '#') == '.'}.toSet()
  }
  println("part1="+even.size)
}

fun readFile(fileName: String): List<String> = File(fileName).useLines { it.toList() }

fun List<String>.mapCoord() = mapIndexed { row, line -> line.mapIndexed { col, c -> Coord(row, col) to c } }.flatten().toMap()

data class Coord(val row: Int, val col: Int) {
    fun distance(b: Coord) = abs(row - b.row) + abs(col - b.col)
    fun right() = copy(col = col + 1)
    fun left() = copy(col = col - 1)
    fun down() = copy(row = row + 1)
    fun up() = copy(row = row - 1)
    fun upRight() = up().right()
    fun downRight() = down().right()
    fun upLeft() = up().left()
    fun downLeft() = down().left()
    fun around() = listOf(left(), right(), up(), down())
    fun aroundDiag() = around() + listOf(upRight(), upLeft(), downRight(), downLeft())
}
