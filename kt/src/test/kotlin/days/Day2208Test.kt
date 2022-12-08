package days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class Day2208Test {

    private val testInput: Forest = arrayOf(
        arrayOf(3, 0, 3, 7, 3),
        arrayOf(2, 5, 5, 1, 2),
        arrayOf(6, 5, 3, 3, 2),
        arrayOf(3, 3, 5, 4, 9),
        arrayOf(3, 5, 3, 9, 0)
    )

    @Test
    fun inputParser() {
        assertTrue(
            testInput.contentDeepEquals(
                Day2208().parseInput(
                    """
                30373
                25512
                65332
                33549
                35390
                """.trimIndent()
                )
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(21, Day2208().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(8, testInput.scenicScore(2, 3))
        assertEquals(8, Day2208().solve2(testInput))
    }
}