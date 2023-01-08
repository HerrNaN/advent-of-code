package days

import Day
import cc.ekblad.konbini.*
import common.*
import days.Day2222.Notes
import kotlin.math.sqrt

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

    data class SideRef(val name: String, val rot: Rot90)
    data class Cube(
        val sideConnectedTo: Map<String, Map<MDir2, SideRef>>,
        val sides: Map<String, Map<Point2, Tile>>,
        val size: Int,
        val sideConv: Set<Pair<Pair<Int, Int>, String>>,
    ) {
        private fun toCubePos(p: Point2): Pair<Point2, String> =
            Pair(
                Point2(p.x % size, p.y % size),
                sideConv.find { it.first == Pair(p.x / size, p.y / size) }!!.second
            )

        private fun fromCubePos(p: Point2, side: String): Point2 {
            val conv = sideConv.find { it.second == side }!!.first
            return Point2(p.x + conv.first, p.y + conv.second)
        }

        fun followPath(from: Point2, dir: MDir2, path: List<Instr>): Pair<Point2, MDir2> {
            var (curPos, curSide) = toCubePos(from)
            var curDir = dir
            for (i in path) {
                val (nextPos, nextSide, nextDir) = when (i) {
                    is Instr.Turn -> Triple(curPos, curSide, curDir.rot(i.d))
                    is Instr.Walk -> {
                        move(curPos, curSide, curDir, i.n)

                    }
                }
                if (nextPos == curPos && nextSide == curSide) break
                curPos = nextPos
                curSide = nextSide
                curDir = nextDir
            }
            return Pair(fromCubePos(curPos, curSide), curDir)
        }

        private fun move(from: Point2, side: String, dir: MDir2, n: Long): Triple<Point2, String, MDir2> {
            var cur = from
            var curDir = dir
            var curSide = side
            for (i in (1..n)) {
                val (next, nextSide, nextDir) = this.next(cur, curSide, curDir)

                if (nextSide == curSide && cur == next) {
                    break
                }
                cur = next
                curDir = nextDir
                curSide = nextSide
            }
            return Triple(cur, curSide, curDir)
        }

        private fun flip(p: Point2): Point2 =
            when {
                p.y == size -> p.copy(y = 1)
                p.x == size -> p.copy(x = 1)
                p.y == 1 -> p.copy(y = size)
                p.x == 1 -> p.copy(x = size)
                else -> throw Error("Don't flip non-edge positions")
            }

        private fun next(p: Point2, side: String, dir: MDir2): Triple<Point2, String, MDir2> {
            val next = p + dir.p
            return when (this.sides[side]!![next]) {
                Tile.Open -> Triple(next, side, dir)
                Tile.Wall -> Triple(p, side, dir)
                else -> {
                    // pos <- flip + rot^(-1)(connected_side)
                    val newSide = sideConnectedTo[side]!![dir]!!
                    val newP = Grid(size).rotatePoint(flip(p), -newSide.rot)
                    Triple(newP, newSide.name, dir.rot(newSide.rot))
                }
            }
        }
    }

    private fun cubeFrom(ns: Notes): Cube {
        val size = sqrt(ns.map.size / 6.0).toInt()
        val sideGrouping = ns.map.entries.groupBy { Pair((it.key.x - 1) / size, (it.key.y - 1) / size) }
        val sideConv = sideGrouping
            .map { it.key }
            .mapIndexed { index, conv -> Pair(conv, ('A' + index).toString()) }
            .toSet()

        return Cube(
            sideConnectedTo = buildSideConnections(sideConv),
            sides = sideGrouping
                .mapValues {
                    it.value.associate { Pair(it.key, it.value) }
                }
                .mapKeys {
                    sideConv.find { conv -> it.key == conv.first }!!.second
                },
            size = size,
            sideConv = sideConv
        )
    }

    private fun buildSideConnections(sideConv: Set<Pair<Pair<Int, Int>, String>>): Map<String, Map<MDir2, SideRef>> {
        var sideConnectedTo = initializeSideConnection(sideConv)

        while (sideConnectedTo.any { it.value.size != 4 }) {
            sideConnectedTo = sideConnectedTo.mapValues {
                var dirMap = it.value
                MDir2.values().forEach { side ->
                    if (!dirMap.containsKey(side)) {
                        dirMap = tryFillSide(side, dirMap, sideConnectedTo)
                    }
                }
                dirMap
            }
        }

        return sideConnectedTo
    }

    private fun tryFillSide(
        side: MDir2,
        dirMap: Map<MDir2, SideRef>,
        sideConnectedTo: Map<String, Map<MDir2, SideRef>>,
    ): Map<MDir2, SideRef> {
        val newDirMap = dirMap.toMutableMap()
        side.adjacent().forEach { adj ->
            if (newDirMap.contains(adj)) {
                val r = when (side) {
                    adj.rot(Rot90(1)) -> Rot90(1)
                    else -> Rot90(-1)
                }
                val via = newDirMap[adj]!!
                val connected = sideConnectedTo[via.name]!!


                connected[side.rot(-via.rot)]?.let {
                    newDirMap[side] = it.copy(rot = it.rot + via.rot + r)
                }
            }
        }
        return newDirMap
    }

    private fun MDir2.adjacent(): List<MDir2> =
        listOf(
            this.rot(Rot90(1)),
            this.rot(Rot90(-1))
        )

    private fun initializeSideConnection(
        sideConv: Set<Pair<Pair<Int, Int>, String>>,
    ): Map<String, Map<MDir2, SideRef>> {
        return sideConv.associate {
            Pair(it.second, MDir2.values().mapNotNull { dir ->
                sideConv.find { c -> c.first.asPoint2() == it.first.asPoint2() + dir.p }?.let { connected ->
                    Pair(dir, SideRef(connected.second, Rot90(0)))
                }
            }.toMap())
        }
    }

    fun Pair<Int, Int>.asPoint2(): Point2 =
        Point2(this.first, this.second)

    data class Grid(val size: Int) {

        fun rotatePoint(p: Point2, rot: Rot90): Point2 =
            fromGridPoint(rotateGridPoint(toGridPoint(p), rot))

        private fun toGridPoint(p: Point2): Point2 =
            Point2(toGridVal(p.x), toGridVal(p.y))

        private fun toGridVal(v: Int): Int =
            if (v > (size / 2)) v - (size / 2)
            else v - 1 - (size / 2)

        private fun fromGridPoint(p: Point2): Point2 =
            Point2(fromGridVal(p.x), fromGridVal(p.y))

        private fun fromGridVal(v: Int): Int =
            if (v > 0)
                v + (size / 2)
            else
                v + 1 + (size / 2)

        private fun rotateGridPoint(p: Point2, r: Rot90): Point2 =
            if (r.n == 0)
                p
            else if (r.n > 0)
                rotateGridPoint(Point2(-p.y, p.x), r - Rot90(1))
            else
                rotateGridPoint(Point2(p.y, -p.x), r + Rot90(1))
    }

    override fun solve2(input: Notes): Int {
        val startPos = input.map.filterValues { it == Tile.Open }.keys.groupBy { it.y }
            .minBy { it.key }.value.minBy { it.x }
        val startDir = MDir2.Right

        val (finalPos, finalDir) = cubeFrom(input).followPath(startPos, startDir, input.path)

        return 1000 * finalPos.y + 4 * finalPos.x + finalDir.pwHint()

    }

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


    private fun MDir2.pwHint(): Int = when (this) {
        MDir2.Right -> 0
        MDir2.Down -> 1
        MDir2.Left -> 2
        MDir2.Up -> 3
    }

}

enum class MDir2(val p: Point2) {
    Up(Dir2.Down.p),
    Right(Dir2.Right.p),
    Down(Dir2.Up.p),
    Left(Dir2.Left.p)
}

fun MDir2.rot(r: Rot90): MDir2 = when {
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


