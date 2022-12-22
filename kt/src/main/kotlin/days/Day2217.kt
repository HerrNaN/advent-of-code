package days

import Day
import cc.ekblad.konbini.*
import common.Dir2
import common.Dir2.*
import common.Point2
import days.Day2217.Rock.*

class Day2217 : Day<List<Dir2>>() {
    override fun inputParser(): Parser<List<Dir2>> =
        many1(
            parser {
                oneOf(
                    parser { char('<') }.map { Left },
                    parser { char('>') }.map { Right }
                )
            }
        )

    private val leftWall = 0
    private val rightWall = 8

    override fun solve1(input: List<Dir2>): Long = heightAfter(input, 2022)

    fun printState(chamber: Set<Point2>, cur: Rock) {
        val maxY = (chamber.maxOfOrNull { it.y } ?: 0) + 3 + 4
        for (y in (maxY downTo 1)) {
            var row = ""
            for (x in (0..8)) {
                row += when {
                    x == 0 || x == 8 -> '|'
                    chamber.contains(Point2(x, y)) -> '#'
                    cur.content.contains(Point2(x, y)) -> '@'
                    else -> '.'
                }
            }
            println(row)
        }
        println("---------")
    }

    override fun solve2(input: List<Dir2>): Long = heightAfter(input, 1000000000000)

    data class State(
        val jetIndex: Long,
        val rockIndex: Long,
    )

    private fun heightAfter(input: List<Dir2>, totRocks: Long): Long {
        val triple = findRepeatSim(input)
        return (totRocks / triple.second) * triple.first + triple.third[totRocks % triple.second]!!
    }

    private fun findRepeatSim(input: List<Dir2>): Triple<Long, Long, Map<Long, Long>> {
        val chamber = mutableSetOf<Point2>()
        var current: Rock? = null
        var max = 0L

        var state = State(
            0,
            0,
        )

        val states = mutableMapOf<State, Long>()

        val maxAt = mutableMapOf(
            0L to 0L
        )

        var nRocks = 0L
        var counter = 0
        while (true) {
            counter++
            if (current == null) {
                val n = rocksOrder[state.rockIndex.toInt()]
                if (states.contains(state)) {
                    return Triple(max - maxAt[states[state]!!]!!, nRocks - states[state]!!, maxAt)
                }
                if (chamber.contains(Point2(4, max.toInt())) && state.rockIndex == 0L) {
                    states[state] = nRocks
                }
                maxAt[nRocks] = max

                current = Rock(n.content.map { Point2(it.x + 3, it.y + max.toInt() + 4) }.toSet())
            }

            if (counter % 2 == 1) {
                val next = current.move(input[state.jetIndex.toInt()])
                if (next.content.none {
                        it.x == leftWall ||
                                it.x == rightWall ||
                                chamber.contains(it)
                    }) {
                    current = next
                }
                state = state.copy(
                    jetIndex = (state.jetIndex + 1) % input.size
                )
            } else {
                val next = current.move(Down)
                current = if (
                    next.content.any {
                        chamber.contains(it) ||
                                it.y == 0
                    }
                ) {
                    state = state.copy(
                        rockIndex = (state.rockIndex + 1) % rocksOrder.size,
                    )
                    nRocks++
                    chamber.addAll(current.content)
                    max = if (max < current.content.maxOf { it.y }) {
                        current.content.maxOf { it.y }.toLong()
                    } else {
                        max
                    }
                    null
                } else {
                    next
                }
            }
        }
    }

    private val rocksOrder = listOf(
        Minus,
        Plus,
        L,
        I,
        Box
    )

    open class Rock(
        val content: Set<Point2>,
    ) {

        fun move(d: Dir2): Rock {
            return Rock(this.content.map { it + d.p }.toSet())
        }

        object Minus : Rock(
            setOf(
                Point2(0, 0),
                Point2(1, 0),
                Point2(2, 0),
                Point2(3, 0),
            )
        )

        object Plus : Rock(
            setOf(
                Point2(1, 0),
                Point2(1, 1),
                Point2(1, 2),
                Point2(0, 1),
                Point2(2, 1),
            )
        )

        object L : Rock(
            setOf(
                Point2(0, 0),
                Point2(1, 0),
                Point2(2, 0),
                Point2(2, 1),
                Point2(2, 2),
            )
        )

        object I : Rock(
            setOf(
                Point2(0, 0),
                Point2(0, 1),
                Point2(0, 2),
                Point2(0, 3),
            )
        )

        object Box : Rock(
            setOf(
                Point2(0, 0),
                Point2(0, 1),
                Point2(1, 0),
                Point2(1, 1),
            )
        )
    }
}