package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.parser
import common.digit
import common.grid
import java.lang.Integer.max

typealias Forest = Array<Array<Int>>

fun Forest.countVisible(): Int {
    var visCount = 0
    for (y in this.indices) {
        for (x in this[y].indices) {
            if (this.isTreeVisible(x, y)) {
                visCount++
            }
        }
    }

    return visCount
}

fun Forest.isTreeVisible(x: Int, y: Int): Boolean =
    x == 0
            || y == 0
            || x == this[y].size - 1
            || y == this.size - 1
            || (0 until y).all { this[it][x] < this[y][x] }
            || (y + 1 until this.size).all { this[it][x] < this[y][x] }
            || (0 until x).all { this[y][it] < this[y][x] }
            || (x + 1 until this[y].size).all { this[y][it] < this[y][x] }

fun Forest.highestScenicScore(): Int {
    var maxScore = 0

    for (y in this.indices) {
        for (x in this[y].indices) {
            maxScore = max(maxScore, this.scenicScore(x, y))
        }
    }

    return maxScore
}

fun Forest.scenicScore(x: Int, y: Int): Int {
    var down = (1 until this.size - y).takeWhile { this[y + it][x] < this[y][x] }.size
    if (down < this.size - y - 1) down++

    var up = (1..y).takeWhile { this[y - it][x] < this[y][x] }.size
    if (up < y - 1) up++

    var right = (1 until this[0].size - x).takeWhile { this[y][x + it] < this[y][x] }.size
    if (right < this[0].size - x - 1) right++

    var left = (1..x).takeWhile { this[y][x - it] < this[y][x] }.size
    if (left < x - 1) left++

    return up * left * down * right
}

class Day2208 : Day<Forest>() {
    override fun inputParser(): Parser<Forest> = grid(parser { digit() })

    override fun solve1(input: Forest): Int = input.countVisible()


    override fun solve2(input: Forest): Int = input.highestScenicScore()
}