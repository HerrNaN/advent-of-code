package days

import common.Point2
import days.Day2222.Grid
import org.junit.jupiter.api.Nested
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

    @Nested
    inner class Part2 {
        @Test
        fun toGridVal() {
            assertEquals(-2, Grid(4).toGridVal(0))
            assertEquals(-1, Grid(4).toGridVal(1))
            assertEquals(1, Grid(4).toGridVal(2))
            assertEquals(2, Grid(4).toGridVal(3))
        }

        @Test
        fun fromGridVal() {
            assertEquals(0, Grid(4).fromGridVal(-2))
            assertEquals(1, Grid(4).fromGridVal(-1))
            assertEquals(2, Grid(4).fromGridVal(1))
            assertEquals(3, Grid(4).fromGridVal(2))
        }

        @Test
        fun toCubePos() {
            assertEquals(
                Pair(Point2(0, 0), "A"),
                Day2222().cubeFrom(Day2222().parseInput(rawTestInput)).toCubePos(Point2(9, 1))
            )
        }

        @Test
        fun fromCubePos() {
            assertEquals(
                Point2(9, 1),
                Day2222().cubeFrom(Day2222().parseInput(rawTestInput)).fromCubePos(Point2(0, 0), "A")
            )
        }


        @Test
        fun solve2() {
            assertEquals(5031, Day2222().solve2(Day2222().parseInput(rawTestInput)))
        }
    }
}