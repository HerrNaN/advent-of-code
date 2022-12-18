package days

import days.Day2216.ValveReport
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class Day2216Test {

    val testInput: List<ValveReport> = listOf(
        ValveReport("AA", 0, listOf("DD", "II", "BB")),
        ValveReport("BB", 13, listOf("CC", "AA")),
        ValveReport("CC", 2, listOf("DD", "BB")),
        ValveReport("DD", 20, listOf("CC", "AA", "EE")),
        ValveReport("EE", 3, listOf("FF", "DD")),
        ValveReport("FF", 0, listOf("EE", "GG")),
        ValveReport("GG", 0, listOf("FF", "HH")),
        ValveReport("HH", 22, listOf("GG")),
        ValveReport("II", 0, listOf("AA", "JJ")),
        ValveReport("JJ", 21, listOf("II")),
    )

    @Test
    fun inputParser() {
        assertContentEquals(
            testInput,
            Day2216().parseInput(
                """
            Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
            Valve BB has flow rate=13; tunnels lead to valves CC, AA
            Valve CC has flow rate=2; tunnels lead to valves DD, BB
            Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
            Valve EE has flow rate=3; tunnels lead to valves FF, DD
            Valve FF has flow rate=0; tunnels lead to valves EE, GG
            Valve GG has flow rate=0; tunnels lead to valves FF, HH
            Valve HH has flow rate=22; tunnel leads to valve GG
            Valve II has flow rate=0; tunnels lead to valves AA, JJ
            Valve JJ has flow rate=21; tunnel leads to valve II
            """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(1651, Day2216().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(1707, Day2216().solve2(testInput))
    }
}