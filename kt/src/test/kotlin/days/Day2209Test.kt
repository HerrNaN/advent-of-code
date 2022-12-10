package days

import common.Dir2
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class Day2209Test {

    private val testInput = listOf(
        Motion(4, Dir2.Right),
        Motion(4, Dir2.Up),
        Motion(3, Dir2.Left),
        Motion(1, Dir2.Down),
        Motion(4, Dir2.Right),
        Motion(1, Dir2.Down),
        Motion(5, Dir2.Left),
        Motion(2, Dir2.Right),
    )

    private val testInput2 = listOf(
        Motion(5, Dir2.Right),
        Motion(8, Dir2.Up),
        Motion(8, Dir2.Left),
        Motion(3, Dir2.Down),
        Motion(17, Dir2.Right),
        Motion(10, Dir2.Down),
        Motion(25, Dir2.Left),
        Motion(20, Dir2.Up),
    )

    @Test
    fun inputParser() {
        assertContentEquals(
            testInput,
            Day2209().parseInput(
                """
                R 4
                U 4
                L 3
                D 1
                R 4
                D 1
                L 5
                R 2
                """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(13, Day2209().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(1, Day2209().solve2(testInput))
        assertEquals(36, Day2209().solve2(testInput2))
    }
}