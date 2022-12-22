package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.char
import cc.ekblad.konbini.integer
import cc.ekblad.konbini.parser
import common.Point3
import common.lines

class Day2218 : Day<List<Point3>>() {
    override fun inputParser(): Parser<List<Point3>> = lines(
        parser {
            val x = integer().toInt()
            char(',')
            val y = integer().toInt()
            char(',')
            val z = integer().toInt()
            Point3(x, y, z)
        }
    )

    override fun solve1(input: List<Point3>): Int = TODO()

    override fun solve2(input: List<Point3>): Int = TODO()

//    data class Side()
}