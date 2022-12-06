package days

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class Day2205Test {

    private val testInput =
        Procedure(
            listOf(
                ArrayDeque(listOf('N', 'Z')),
                ArrayDeque(listOf('D', 'C', 'M')),
                ArrayDeque(listOf('P'))
            ),
            listOf(
                Instruction(1, 2, 1),
                Instruction(3, 1, 3),
                Instruction(2, 2, 1),
                Instruction(1, 1, 2),
            )
        )

    @Test
    fun inputParser() {
        assertEquals(
            testInput,
            Day2205().parseInput(
                """
                    [D]    
                [N] [C]    
                [Z] [M] [P]
                 1   2   3 

                move 1 from 2 to 1
                move 3 from 1 to 3
                move 2 from 2 to 1
                move 1 from 1 to 2
        """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals("CMZ", Day2205().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals("MCD", Day2205().solve2(testInput))
    }
}