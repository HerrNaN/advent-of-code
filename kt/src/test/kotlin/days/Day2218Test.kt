package days

import common.Point3
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class Day2218Test {

    val testInput = listOf(
        Point3(2, 2, 2),
        Point3(1, 2, 2),
        Point3(3, 2, 2),
        Point3(2, 1, 2),
        Point3(2, 3, 2),
        Point3(2, 2, 1),
        Point3(2, 2, 3),
        Point3(2, 2, 4),
        Point3(2, 2, 6),
        Point3(1, 2, 5),
        Point3(3, 2, 5),
        Point3(2, 1, 5),
        Point3(2, 3, 5),
    )

    @Test
    fun inputParser() {
        assertContentEquals(
            testInput,
            Day2218().parseInput(
                """
                2,2,2
                1,2,2
                3,2,2
                2,1,2
                2,3,2
                2,2,1
                2,2,3
                2,2,4
                2,2,6
                1,2,5
                3,2,5
                2,1,5
                2,3,5
            """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(
            64,
            Day2218().solve1(testInput)
        )
    }

    @Test
    fun solve2() {
        assertEquals(
            58,
            Day2218().solve2(testInput)
        )
    }
}