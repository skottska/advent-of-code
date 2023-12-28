import kotlin.math.abs
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
  val t1 = System.currentTimeMillis()
  println("started")
  val file = "aoc22.txt"
  //val file = "test.txt"
  val bricks = readFile(file).map{ line->
    val nums = matchNumbers(line)
    toBrick(Coord3D(nums[0], nums[1], nums[2]), Coord3D(nums[3], nums[4], nums[5]))
  }

  val settled = settle2(bricks)
  //settled.forEach { println(it)}
  println("Finished first settle " +(System.currentTimeMillis()-t1))

  val unsettled = settled.map{ b ->
    numUnsettled(settled - b)
  }
  println("part1="+unsettled.count { it == 0})
  println("part2="+unsettled.sum())
}

fun settle2(bricks: List<Brick>): List<Brick> {
  val settled = mutableListOf<Brick>()
  bricks.sortedBy { it.z.first }.forEach {b -> 
    settled += b.down(b.gap(settled))}
  
  return settled
}

fun numUnsettled(bricks: List<Brick>): Int {
  var unsettled = 0
  val settled = mutableListOf<Brick>()
  bricks.sortedBy { it.z.first }.forEach {b -> 
    if (b.gap(settled) != 0) unsettled++
    else settled += b
  }
  return unsettled
}

fun toBrick(start: Coord3D, end: Coord3D): Brick {
  return Brick(anyRange(start.x,end.x),
              anyRange(start.y,end.y),
              anyRange(start.z,end.z))
}

data class Brick(val x: IntRange, val y: IntRange, val z: IntRange) {
  fun directlyBelow(b: Brick): Boolean = x.overlaps(b.x) && y.overlaps(b.y) && z.overlaps(b.down().z)

  fun down(i: Int = 1) = copy(z = (z.first-i)..(z.last-i))

  fun gap(l: List<Brick>): Int {
    return if (z.first == 1) 0
    else l.mapNotNull { gap(it)}.minOrNull() ?: z.first - 1
  }

  fun gap(b: Brick): Int? {
    return if (x.overlaps(b.x) && y.overlaps(b.y)) z.first - b.z.last - 1
    else null
  }
}

fun IntRange.overlaps(b : IntRange) = 
  max(first, b.first) <= min(last, b.last)

data class Coord3D(val x: Int, val y: Int, val z: Int) {
    fun isAdjacent(c: Coord3D) =
        when {
            listOf(x == c.x, y == c.y, z == c.z).filter { it }.size != 2 -> false
            else -> abs((x + y + z) - (c.x + c.y + c.z)) == 1
        }

    fun absSumOfCoords() = abs(x) + abs(y) + abs(z)

    fun down() = copy(z = z - 1)
}

fun readFile(fileName: String): List<String> = File(fileName).useLines { it.toList() }

fun matches(line: String, regex: String) = Regex(regex)
    .findAll(line)
    .map { it.groupValues[0] }
    .toList()

private const val NUMBER_REGEX = "-?[0-9]+"

fun matchNumber(line: String) = matches(line, NUMBER_REGEX).map { it.toInt() }.first()
fun matchNumbers(line: String) = matches(line, NUMBER_REGEX).map { it.toInt() }

fun anyRange(a: Int, b: Int) = min(a, b)..(max(a, b))
