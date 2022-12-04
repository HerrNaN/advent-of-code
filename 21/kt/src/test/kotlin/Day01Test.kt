import days.Day01
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day01Test {

    @Test
    fun inputParser() {
        assertEquals(
            listOf<Long>(1, 2, 3),
            Day01().parseInput("1\n2\n3")
        )
    }

    @Test
    fun solve1() {
        assertEquals(
            7,
            Day01().solve1(listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263))
        )
    }
}