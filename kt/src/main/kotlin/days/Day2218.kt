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

    override fun solve1(input: List<Point3>): Int =
        input.surface().size

    override fun solve2(input: List<Point3>): Int {
        val maxX = input.maxOf { it.x }
        val minX = input.minOf { it.x }
        val maxY = input.maxOf { it.y }
        val minY = input.minOf { it.y }
        val maxZ = input.maxOf { it.z }
        val minZ = input.minOf { it.z }

        val cube: MutableSet<Point3> = cubeOf(
            (minX - 1..maxX + 1),
            (minY - 1..maxY + 1),
            (minZ - 1..maxZ + 1),
        ).toMutableSet()
        cube -= input.toSet()
        cube -= cube.toSet().connectedTo(Point3(minX - 1, minY - 1, minZ - 1))

        val interior = cube.toList().surface().size
        return input.surface().size - interior
    }

    private fun Set<Point3>.connectedTo(start: Point3): Set<Point3> {
        val connected = mutableSetOf<Point3>()
        val toConnect = mutableSetOf(start)

        while (toConnect.isNotEmpty()) {
            val p = toConnect.first()
            toConnect -= p
            connected += p

            toConnect.addAll(p.neighbours().minus(connected + toConnect) intersect this)
        }

        return connected
    }

    private fun cubeOf(xs: IntRange, ys: IntRange, zs: IntRange): Set<Point3> =
        xs.flatMap { x ->
            ys.flatMap { y ->
                zs.map { z ->
                    Point3(x, y, z)
                }
            }
        }.toSet()

    data class Side3(val s: Pair<Point3, Point3>)

    private fun List<Point3>.surface(): Set<Side3> =
        this.map { it.sides() }
            .fold(emptySet()) { acc, sides -> (acc - sides) + (sides - acc) }

    private fun Point3.sides(): Set<Side3> =
        setOf(
            Side3(Pair(this, this + Point3(1, 0, 0))),
            Side3(Pair(this, this + Point3(0, 1, 0))),
            Side3(Pair(this, this + Point3(0, 0, 1))),
            Side3(Pair(this + Point3(-1, 0, 0), this)),
            Side3(Pair(this + Point3(0, -1, 0), this)),
            Side3(Pair(this + Point3(0, 0, -1), this)),
        )

}
