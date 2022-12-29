package days

import days.Day2219.Resource.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2219Test {

    val testInput = listOf<Blueprint>(
//    Blueprint 1:
//    Each ore robot costs 4 ore.
//    Each clay robot costs 2 ore.
//    Each obsidian robot costs 3 ore and 14 clay.
//    Each geode robot costs 2 ore and 7 obsidian.
        mapOf(
            Ore to mapOf(
                Ore to 4
            ),
            Clay to mapOf(
                Ore to 2
            ),
            Obsidian to mapOf(
                Ore to 3,
                Clay to 14
            ),
            Geode to mapOf(
                Ore to 2,
                Obsidian to 7
            )
        ),
//    Blueprint 2:
//    Each ore robot costs 2 ore.
//    Each clay robot costs 3 ore.
//    Each obsidian robot costs 3 ore and 8 clay.
//    Each geode robot costs 3 ore and 12 obsidian.
        mapOf(
            Ore to mapOf(
                Ore to 2
            ),
            Clay to mapOf(
                Ore to 3
            ),
            Obsidian to mapOf(
                Ore to 3,
                Clay to 8
            ),
            Geode to mapOf(
                Ore to 3,
                Obsidian to 12
            )
        )
    )

    val testInputRaw = """
        Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
        Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
    """.trimIndent()

    @Test
    fun inputParser() {
        assertEquals(
            testInput,
            Day2219().parseInput(testInputRaw)
        )
    }

    @Test
    fun solve1() {
        assertEquals(33, Day2219().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(62 * 56, Day2219().solve2(testInput))
    }

    @Nested
    inner class MaxOf {
        @Test
        fun part1_ex1() {
            assertEquals(9, maxOf(testInput[0], Geode, 24))
        }

        @Test
        fun part1_ex2() {
            assertEquals(12, maxOf(testInput[1], Geode, 24))
        }

        @Test
        fun part2_ex1() {
            assertEquals(56, maxOf(testInput[0], Geode, 32))
        }

        @Test
        fun part2_ex2() {
            assertEquals(62, maxOf(testInput[1], Geode, 32))
        }
    }
}