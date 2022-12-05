package days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day2201Test {

    private val testInput =
        listOf<List<Long>>(
            listOf(1000, 2000, 3000),
            emptyList(),
            listOf(4000),
            emptyList(),
            listOf(5000, 6000),
            emptyList(),
            listOf(7000, 8000, 9000),
            emptyList(),
            listOf(10000),
        )

    @Test
    fun inputParser() {
        assertEquals(
            testInput,
            Day2201().parseInput(
                """
                1000
                2000
                3000
                
                4000
                
                5000
                6000
                
                7000
                8000
                9000
                
                10000
            """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(24000, Day2201().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(45000, Day2201().solve2(testInput))
    }
}