package days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2220Test {

    val testInput = listOf(
        1,
        2,
        -3,
        3,
        -2,
        0,
        4
    )

    @Test
    fun inputParser() {
        assertEquals(
            testInput,
            Day2220().parseInput(
                """
                1
                2
                -3
                3
                -2
                0
                4
            """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(3, Day2220().solve1(testInput))
    }

    @Test
    fun solve2() {
        TODO()
    }
}