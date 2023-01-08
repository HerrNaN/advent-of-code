package days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2222Test {

//    val testInput = null

    val rawTestInput =
        """        ...#
        .#..
        #...
        ....
...#.......#
........#...
..#....#....
..........#.
        ...#....
        .....#..
        .#......
        ......#.

10R5L5R10L4R5L5"""

    @Test
    fun inputParser() {
        assertEquals(96, Day2222().parseInput(rawTestInput).map.size)
        assertEquals(13, Day2222().parseInput(rawTestInput).path.size)
    }

    @Test
    fun solve1() {
        assertEquals(6032, Day2222().solve1(Day2222().parseInput(rawTestInput)))
    }

    @Test
    fun solve2() {
        assertEquals(5031, Day2222().solve2(Day2222().parseInput(rawTestInput)))
    }
}