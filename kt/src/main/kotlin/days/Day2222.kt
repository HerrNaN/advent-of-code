package days

import Day
import cc.ekblad.konbini.*
import common.*
import days.Day2222.Notes

class Day2222 : Day<Notes>() {
    override fun inputParser(): Parser<Notes> {
        val map = parser {
            lines(parser {
                many1(parser {
                    oneOf(
                        parser { char(' ') },
                        parser { char('.') },
                        parser { char('#') },
                    )
                })
            }).mapIndexed { y, row -> row.mapIndexed { x, c -> Pair(Point2(x + 1, y + 1), c) } }
                .flatten()
                .filter { (_, c) -> c != ' ' }.associate { (p, c) ->
                    Pair(
                        p, when (c) {
                            '.' -> Tile.Open
                            '#' -> Tile.Wall
                            else -> throw Error("invalid tile char")
                        }
                    )
                }
        }

        val instr = oneOf(
            parser {
                Instr.Turn(
                    when (letter()) {
                        'R' -> Rot90(1)
                        'L' -> Rot90(-1)
                        else -> throw Error("invalid rotation")
                    }
                )
            },
            integer.map { Instr.Walk(it) }
        )
        val instrs = many1(instr)

        return parser {
            val m = map()
            newline(); newline()
            val ins = instrs()
            Notes(m, ins)
        }
    }

    override fun solve1(input: Notes): Int {
        var cur = input.map.filterValues { it == Tile.Open }.keys.groupBy { it.y }
            .minBy { it.key }.value.minBy { it.x }

        var dir = MDir2.Right
        for (i in input.path) {
            when (i) {
                is Instr.Turn -> dir = dir.rot(i.d)
                is Instr.Walk -> cur = input.map.move(cur, dir, i.n)
            }
        }

        return 1000 * cur.y + 4 * cur.x + dir.pwHint()
    }

    override fun solve2(input: Notes): Int = TODO()

    data class Notes(val map: Map<Point2, Tile>, val path: List<Instr>)
    enum class Tile {
        Wall,
        Open
    }

    sealed class Instr {
        data class Walk(val n: Long) : Instr()
        data class Turn(val d: Rot90) : Instr()
    }

    private fun Map<Point2, Tile>.move(from: Point2, dir: MDir2, n: Long): Point2 {
        var cur = from
        for (i in (1..n)) {
            var next = cur + dir.p
            if (!this.contains(next)) {
                next = when (dir) {
                    MDir2.Right -> this.filterKeys { it.y == from.y }.minBy { it.key.x }.key
                    MDir2.Down -> this.filterKeys { it.x == from.x }.minBy { it.key.y }.key
                    MDir2.Left -> this.filterKeys { it.y == from.y }.maxBy { it.key.x }.key
                    MDir2.Up -> this.filterKeys { it.x == from.x }.maxBy { it.key.y }.key
                }
            }
            if (this[next] == Tile.Wall) {
                break
            }
            cur = next
        }
        return cur
    }

    enum class MDir2(val p: Point2) {
        Up(Dir2.Down.p),
        Right(Dir2.Right.p),
        Down(Dir2.Up.p),
        Left(Dir2.Left.p)
    }

    private fun MDir2.pwHint(): Int = when (this) {
        MDir2.Right -> 0
        MDir2.Down -> 1
        MDir2.Left -> 2
        MDir2.Up -> 3
    }

    private fun MDir2.rot(r: Rot90): MDir2 = when {
        r.n > 0 -> when (this) {
            MDir2.Up -> MDir2.Right
            MDir2.Right -> MDir2.Down
            MDir2.Down -> MDir2.Left
            MDir2.Left -> MDir2.Up
        }.rot(r.copy(n = r.n - 1))

        r.n < 0 -> when (this) {
            MDir2.Up -> MDir2.Left
            MDir2.Left -> MDir2.Down
            MDir2.Down -> MDir2.Right
            MDir2.Right -> MDir2.Up
        }.rot(r.copy(n = r.n + 1))

        else -> this
    }
}


