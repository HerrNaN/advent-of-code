package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.integer
import cc.ekblad.konbini.parser
import common.*

typealias Motions = List<Motion>

data class Motion(val n: Int, val dir: Dir2)

data class Rope(val knots: List<Point2>) {
    fun moveHead(dir: Dir2): Rope {
        val newKnots = mutableListOf<Point2>()

        newKnots += knots[0].move(dir)

        for (i in 1 until knots.size) {
            newKnots += knots[i].moveTo(newKnots[i - 1])
        }

        return Rope(newKnots)
    }

    private fun Point2.moveTo(other: Point2): Point2 {
        val diff = other - this
        if (this.distTo(other) <= 1) {
            return this
        }

        if (this.distTo(other) > 2) {
            throw Error("big diff: [$this:$other]")
        }

        return when {
            diff.x > 0 && diff.y > 0 -> this.move(Dir2.UpRight)
            diff.x > 0 && diff.y < 0 -> this.move(Dir2.DownRight)
            diff.x < 0 && diff.y > 0 -> this.move(Dir2.UpLeft)
            diff.x < 0 && diff.y < 0 -> this.move(Dir2.DownLeft)
            diff.x == 0 && diff.y > 0 -> this.move(Dir2.Up)
            diff.x == 0 && diff.y < 0 -> this.move(Dir2.Down)
            diff.x > 0 -> this.move(Dir2.Right)
            diff.x < 0 -> this.move(Dir2.Left)
            else -> throw Error("weird diff: [$this:$other]")
        }
    }
}

fun Rope.tailVisitedCount(motions: Motions): Int {
    val visited = mutableSetOf(Point2())
    var rope = this

    for (d in motions.flatMap { (1..it.n).map { _ -> it.dir } }) {
        rope = rope.moveHead(d)
        visited += rope.knots.last()
    }

    return visited.size
}

class Day2209 : Day<Motions>() {
    override fun inputParser(): Parser<Motions> = lines(
        parser {
            val d = char()
            space()
            val n = integer().toInt()
            when (d) {
                'D' -> Motion(n, Dir2.Down)
                'U' -> Motion(n, Dir2.Up)
                'R' -> Motion(n, Dir2.Right)
                'L' -> Motion(n, Dir2.Left)
                else -> this.fail("unknown direction")
            }
        }
    )

    override fun solve1(input: Motions): Int =
        Rope(List(2) { Point2() }).tailVisitedCount(input)

    override fun solve2(input: Motions): Int =
        Rope(List(10) { Point2() }).tailVisitedCount(input)
}