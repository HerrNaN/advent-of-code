package days

import common.Point2
import days.Day2214.Material.Rock
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2214Test {

    val testInput: Slice = mutableMapOf(
        // Formation 1
        Point2(498, 4) to Rock,
        Point2(498, 5) to Rock,
        Point2(498, 6) to Rock,
        Point2(497, 6) to Rock,
        Point2(496, 6) to Rock,

        // Formation 2
        Point2(503, 4) to Rock,
        Point2(502, 4) to Rock,
        Point2(502, 5) to Rock,
        Point2(502, 6) to Rock,
        Point2(502, 7) to Rock,
        Point2(502, 8) to Rock,
        Point2(502, 9) to Rock,
        Point2(501, 9) to Rock,
        Point2(500, 9) to Rock,
        Point2(499, 9) to Rock,
        Point2(498, 9) to Rock,
        Point2(497, 9) to Rock,
        Point2(496, 9) to Rock,
        Point2(495, 9) to Rock,
        Point2(494, 9) to Rock,
    )


    @Test
    fun inputParser() {
        assertEquals(
            testInput,
            Day2214().parseInput(
                """
                498,4 -> 498,6 -> 496,6
                503,4 -> 502,4 -> 502,9 -> 494,9
                """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(24, Day2214().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(93, Day2214().solve2(testInput))
    }
}