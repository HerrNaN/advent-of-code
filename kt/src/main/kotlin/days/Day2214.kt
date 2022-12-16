package days

import Day
import cc.ekblad.konbini.*
import common.Dir2
import common.Point2
import common.newline
import days.Day2214.Material.Rock
import days.Day2214.Material.Sand

internal typealias Slice = MutableMap<Point2, Day2214.Material>

class Day2214 : Day<Slice>() {
    override fun inputParser(): Parser<Slice> = parser {
        val slice = mutableMapOf<Point2, Material>()
        val formations = chain(formation, newline).terms
        formations.forEach { f ->
            for (i in (0 until f.size - 1)) {
                val diff = f[i + 1] - f[i]
                val line = when {
                    diff.y > 0 ->
                        (0..diff.y).map { f[i] + Point2(0, it) }

                    diff.y < 0 ->
                        (0 downTo diff.y).map { f[i] + Point2(0, it) }

                    diff.x > 0 ->
                        (0..diff.x).map { f[i] + Point2(it, 0) }

                    diff.x < 0 ->
                        (0 downTo diff.x).map { f[i] + Point2(it, 0) }

                    else -> emptyList()
                }
                line.forEach { slice[it] = Rock }
            }
        }

        slice
    }

    override fun solve1(input: Slice): Int {
        val floorLevel = input.keys.maxOf { it.y }
        var counter = 0
        while (true) {
            if (input.dropSand(floorLevel)) {
                return counter
            }
            counter++
        }
    }

    override fun solve2(input: Slice): Int {
        val floorLevel = input.keys.maxOf { it.y } + 2

        var counter = 0
        while (true) {
            counter++
            input.dropSand(floorLevel)
            if (input.containsKey(source)) {
                return counter
            }
        }
    }

    private val point = parser {
        val x = integer()
        char(',')
        val y = integer()
        Point2(x.toInt(), y.toInt())
    }
    private val formation = parser { chain(point, parser { string(" -> ") }).terms }

    enum class Material {
        Rock,
        Sand
    }

    private val source = Point2(500, 0)

    // Can the source be clogged? Yes in part 2
    private fun Slice.dropSand(floorLevel: Int): Boolean {
        var grain = source

        while (true) {
            val next = this.next(grain)
            if (next == grain) {
                this[grain] = Sand
                return false
            }

            if (next.y >= floorLevel) {
                this[grain] = Sand
                return true
            }

            grain = next
        }
    }

    fun Slice.next(p: Point2): Point2 = when {
        !this.containsKey(p + Dir2.Up.p) ->
            p + Dir2.Up.p

        !this.containsKey(p + Dir2.UpLeft.p) ->
            p + Dir2.UpLeft.p

        !this.containsKey(p + Dir2.UpRight.p) ->
            p + Dir2.UpRight.p

        else -> p
    }

}