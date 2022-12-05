package days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day2203Test {

    private val testInput =
        listOf(
            "vJrwpWtwJgWrhcsFMMfFFhFp".toCharArray().asList(),
            "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL".toCharArray().asList(),
            "PmmdzqPrVvPwwTWBwg".toCharArray().asList(),
            "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn".toCharArray().asList(),
            "ttgJtRGJQctTZtZT".toCharArray().asList(),
            "CrZsJsPPZsGzwwsLwLmpwMDw".toCharArray().asList(),
        )

    @Test
    fun inputParser() {
        assertEquals(
            testInput,
            Day2203().parseInput(
                """
            vJrwpWtwJgWrhcsFMMfFFhFp
            jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
            PmmdzqPrVvPwwTWBwg
            wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
            ttgJtRGJQctTZtZT
            CrZsJsPPZsGzwwsLwLmpwMDw
        """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(157, Day2203().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(70, Day2203().solve2(testInput))
    }
}