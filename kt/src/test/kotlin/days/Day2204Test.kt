package days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day2204Test {

    private val testInput =
        listOf(
            Pair(LongRange(2, 4), LongRange(6, 8)),
            Pair(LongRange(2, 3), LongRange(4, 5)),
            Pair(LongRange(5, 7), LongRange(7, 9)),
            Pair(LongRange(2, 8), LongRange(3, 7)),
            Pair(LongRange(6, 6), LongRange(4, 6)),
            Pair(LongRange(2, 6), LongRange(4, 8))
        )

    @Test
    fun inputParser() {
        assertEquals(
            testInput,
            Day2204().parseInput(
                """
                2-4,6-8
                2-3,4-5
                5-7,7-9
                2-8,3-7
                6-6,4-6
                2-6,4-8
        """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(2, Day2204().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(4, Day2204().solve2(testInput))
    }
}