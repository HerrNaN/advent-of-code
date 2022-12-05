package days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day2202Test {

    private val testInput =
        listOf(
            Round(Shape.Rock, Shape.Paper),
            Round(Shape.Paper, Shape.Rock),
            Round(Shape.Scissors, Shape.Scissors),
        )

    @Test
    fun inputParser() {
        assertEquals(
            testInput,
            Day2202().parseInput(
                """
                A Y
                B X
                C Z
            """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(15, Day2202().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(12, Day2202().solve2(testInput))
    }
}