package days

import common.Point2
import days.Day2215.Device.Beacon
import days.Day2215.Device.Sensor
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2215Test {

    val testInput: Swarm = mapOf(
        Point2(2, 18) to Sensor(4 + 3),
        Point2(9, 16) to Sensor(1 + 0),
        Point2(13, 2) to Sensor(2 + 1),
        Point2(12, 14) to Sensor(2 + 2),
        Point2(10, 20) to Sensor(0 + 4),
        Point2(14, 17) to Sensor(4 + 1),
        Point2(8, 7) to Sensor(6 + 3),
        Point2(2, 0) to Sensor(0 + 10),
        Point2(0, 11) to Sensor(2 + 1),
        Point2(20, 14) to Sensor(5 + 3),
        Point2(17, 20) to Sensor(4 + 2),
        Point2(16, 7) to Sensor(1 + 4),
        Point2(14, 3) to Sensor(1 + 0),
        Point2(20, 1) to Sensor(5 + 2),

        Point2(-2, 15) to Beacon,
        Point2(10, 16) to Beacon,
        Point2(15, 3) to Beacon,
        Point2(2, 10) to Beacon,
        Point2(21, 22) to Beacon,
        Point2(25, 17) to Beacon,
    )

    @Test
    fun inputParser() {
        assertEquals(
            testInput,
            Day2215().parseInput(
                """
                Sensor at x=2, y=18: closest beacon is at x=-2, y=15
                Sensor at x=9, y=16: closest beacon is at x=10, y=16
                Sensor at x=13, y=2: closest beacon is at x=15, y=3
                Sensor at x=12, y=14: closest beacon is at x=10, y=16
                Sensor at x=10, y=20: closest beacon is at x=10, y=16
                Sensor at x=14, y=17: closest beacon is at x=10, y=16
                Sensor at x=8, y=7: closest beacon is at x=2, y=10
                Sensor at x=2, y=0: closest beacon is at x=2, y=10
                Sensor at x=0, y=11: closest beacon is at x=2, y=10
                Sensor at x=20, y=14: closest beacon is at x=25, y=17
                Sensor at x=17, y=20: closest beacon is at x=21, y=22
                Sensor at x=16, y=7: closest beacon is at x=15, y=3
                Sensor at x=14, y=3: closest beacon is at x=15, y=3
                Sensor at x=20, y=1: closest beacon is at x=15, y=3
                """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(26, testInput.numNotBeaconAtRow(10))
    }

    @Test
    fun solve2() {
        assertEquals(56_000_011, testInput.findDistressTuningFreqIn(0..20))
    }
}