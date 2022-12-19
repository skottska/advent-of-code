package adventofcode.y2015 // ktlint-disable filename

import adventofcode.matchNumbers
import adventofcode.readFile
import java.math.BigInteger

fun main(args: Array<String>) {
    val line = readFile("src/main/resources/y2015/day25.txt")[0]
    val stopRow = matchNumbers(line)[0]; val stopColumn = matchNumbers(line)[1]
    var curValue = 20151125L
    val func = {i: Long -> (i * 252533L) % 33554393 }

    var curColumn = 1; var maxRow = 1; var curRow = 1
    while(curRow != stopRow || curColumn != stopColumn) {
        if (curRow == 1) { curRow = ++maxRow; curColumn = 1} else { curRow--; curColumn++ }
        curValue = func(curValue)
    }
    println("part1=$curValue")
}
