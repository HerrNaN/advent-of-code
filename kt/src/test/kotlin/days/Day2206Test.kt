package days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day2206Test {

    private val testInputs1 =
        listOf(
            "bvwbjplbgvbhsrlpgdmjqwftvncz".toCharArray().asList(),
            "nppdvjthqldpwncqszvftbrmjlhg".toCharArray().asList(),
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg".toCharArray().asList(),
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw".toCharArray().asList(),
        )

    private val testInputs2 =
        listOf(
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb".toCharArray().asList(),
            "bvwbjplbgvbhsrlpgdmjqwftvncz".toCharArray().asList(),
            "nppdvjthqldpwncqszvftbrmjlhg".toCharArray().asList(),
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg".toCharArray().asList(),
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw".toCharArray().asList(),
        )

    @Test
    fun inputParser() {
        assertEquals(
            testInputs1[0],
            Day2206().parseInput(
                """
                bvwbjplbgvbhsrlpgdmjqwftvncz
        """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(5, Day2206().solve1(testInputs1[0]))
        assertEquals(6, Day2206().solve1(testInputs1[1]))
        assertEquals(10, Day2206().solve1(testInputs1[2]))
        assertEquals(11, Day2206().solve1(testInputs1[3]))
    }

    @Test
    fun solve2() {
        assertEquals(19, Day2206().solve2(testInputs2[0]))
        assertEquals(23, Day2206().solve2(testInputs2[1]))
        assertEquals(23, Day2206().solve2(testInputs2[2]))
        assertEquals(29, Day2206().solve2(testInputs2[3]))
        assertEquals(26, Day2206().solve2(testInputs2[4]))
    }
}