package days

import common.Point2
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2212Test {

    val testInputRaw =
        """Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi"""

    val testInput: Day2212.HeightMap = Day2212.HeightMap(
        mapOf(
            Point2(0, 0) to 0,
            Point2(1, 0) to 1,
            Point2(2, 0) to 2,
            Point2(3, 0) to 17,
            Point2(4, 0) to 16,
            Point2(5, 0) to 15,
            Point2(6, 0) to 14,
            Point2(7, 0) to 13,
            Point2(0, 1) to 1,
            Point2(1, 1) to 2,
            Point2(2, 1) to 3,
            Point2(3, 1) to 18,
            Point2(4, 1) to 25,
            Point2(5, 1) to 24,
            Point2(6, 1) to 24,
            Point2(7, 1) to 12,
            Point2(0, 2) to 1,
            Point2(1, 2) to 3,
            Point2(2, 2) to 3,
            Point2(3, 2) to 19,
            Point2(4, 2) to 26,
            Point2(5, 2) to 27,
            Point2(6, 2) to 24,
            Point2(7, 2) to 11,
            Point2(0, 3) to 1,
            Point2(1, 3) to 3,
            Point2(2, 3) to 3,
            Point2(3, 3) to 20,
            Point2(4, 3) to 21,
            Point2(5, 3) to 22,
            Point2(6, 3) to 23,
            Point2(7, 3) to 10,
            Point2(0, 4) to 1,
            Point2(1, 4) to 2,
            Point2(2, 4) to 4,
            Point2(3, 4) to 5,
            Point2(4, 4) to 6,
            Point2(5, 4) to 7,
            Point2(6, 4) to 8,
            Point2(7, 4) to 9
        ),
        start = Point2(0, 0),
        end = Point2(5, 2)
    )

    @Test
    fun inputParser() {
        assertEquals(testInput, Day2212().parseInput(testInputRaw))
    }

    @Test
    fun solve1() {
        assertEquals(31, Day2212().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(29, Day2212().solve2(testInput))
    }
}
