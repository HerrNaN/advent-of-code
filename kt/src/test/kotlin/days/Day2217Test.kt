package days

import common.Dir2.Left
import common.Dir2.Right
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2217Test {

    val testInput = listOf(
        Right,
        Right,
        Right,
        Left,
        Left,
        Right,
        Left,
        Right,
        Right,
        Left,
        Left,
        Left,
        Right,
        Right,
        Left,
        Right,
        Right,
        Right,
        Left,
        Left,
        Left,
        Right,
        Right,
        Right,
        Left,
        Left,
        Left,
        Right,
        Left,
        Left,
        Left,
        Right,
        Right,
        Left,
        Right,
        Right,
        Left,
        Left,
        Right,
        Right
    )

    val testRawInput =
        ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

    @Test
    fun inputParser() {
        assertEquals(
            testInput,
            Day2217().parseInput(testRawInput)
        )
    }

    @Test
    fun solve1() {
        assertEquals(
            3068,
            Day2217().solve1(testInput)
        )
    }

    @Test
    fun solve2() {
        assertEquals(
            1_514_285_714_288,
            Day2217().solve2(testInput)
        )
    }
}