package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.chain
import cc.ekblad.konbini.integer
import cc.ekblad.konbini.parser
import common.Point2
import common.newline
import days.Day2215.Device
import days.Day2215.Device.Beacon
import days.Day2215.Device.Sensor

typealias Swarm = Map<Point2, Device>

class Day2215 : Day<Swarm>() {
    override fun inputParser(): Parser<Swarm> = parser {
        chain(sensorReport, newline).terms.flatten().toMap()
    }

    override fun solve1(input: Swarm): Int = input.numNotBeaconAtRow(2_000_000)

    override fun solve2(input: Swarm): Long =
        input.findDistressTuningFreqIn(0..4_000_000)

    private val sensorReport = parser {
        string("Sensor at x=")
        val sx = integer()
        string(", y=")
        val sy = integer()
        val sp = Point2(sx.toInt(), sy.toInt())

        string(": closest beacon is at x=")
        val bx = integer()
        string(", y=")
        val by = integer()
        val bp = Point2(bx.toInt(), by.toInt())

        listOf(
            Pair(sp, Sensor(sp.manhattan(bp))),
            Pair(bp, Beacon)
        )
    }

    sealed class Device {
        object Beacon : Device()
        data class Sensor(val distToBeacon: Int) : Device()
    }
}

internal fun Swarm.numNotBeaconAtRow(row: Int): Int {
    val minX: Int = this
        .filter { it.value is Sensor }
        .map { it.key.x - (it.value as Sensor).distToBeacon }.min()

    val maxX: Int = this
        .filter { it.value is Sensor }
        .map { it.key.x + (it.value as Sensor).distToBeacon }.max()

    return (minX..maxX)
        .map { Point2(it, row) }
        .count { this.cannotContainBeacon(it) }
}

internal fun Swarm.cannotContainBeacon(p: Point2): Boolean =
    this[p] != Beacon &&
            this.any {
                it.value is Sensor &&
                        it.key.manhattan(p) <= (it.value as Sensor).distToBeacon
            }

internal fun Swarm.findDistressTuningFreqIn(range: IntRange): Long {
    val d: Point2 = this.distressIn(range)
    return d.x * 4_000_000L + d.y
}

internal fun Swarm.distressIn(range: IntRange): Point2 {
    val candidates = this
        .filter { it.value is Sensor }
        .flatMap { justOutOfReach(it.key, it.value as Sensor) }
        .filter { it.x in range && it.y in range }

    return candidates.first { !this.cannotContainBeacon(it) && this[it] == null }
}

internal fun justOutOfReach(p: Point2, s: Sensor): List<Point2> {
    val ds = (0..s.distToBeacon + 1).zip(s.distToBeacon + 1 downTo 0)
        .map { Point2(it.first, it.second) }

    val oor = listOf(
        ds.map { Point2(it.x, it.y) },
        ds.map { Point2(-it.x, it.y) },
        ds.map { Point2(-it.x, -it.y) },
        ds.map { Point2(it.x, -it.y) },
    ).flatten().map { p + it }

    return oor
}