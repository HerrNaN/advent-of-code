package days

import days.Day2213.Packet.PList
import days.Day2213.Packet.PVal
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2213Test {

    val testRawInput = """
        [1,1,3,1,1]
        [1,1,5,1,1]

        [[1],[2,3,4]]
        [[1],4]

        [9]
        [[8,7,6]]

        [[4,4],4,4]
        [[4,4],4,4,4]

        [7,7,7,7]
        [7,7,7]

        []
        [3]

        [[[]]]
        [[]]

        [1,[2,[3,[4,[5,6,7]]]],8,9]
        [1,[2,[3,[4,[5,6,0]]]],8,9]
        """.trimIndent()

    @Test
    fun inputParser() {
        assertEquals(
            listOf(
                Pair(
                    PList(
                        listOf(
                            PVal(1),
                            PVal(2),
                            PList(
                                listOf(
                                    PVal(3),
                                    PList(
                                        listOf(
                                            PVal(4),
                                        )
                                    ),
                                    PVal(5),
                                )
                            ),
                            PVal(6),
                            PList(
                                listOf(
                                    PList(
                                        listOf(
                                            PVal(7)
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    PList(
                        listOf(
                            PVal(1),
                            PVal(2),
                            PList(
                                listOf(
                                    PVal(3),
                                    PVal(4),
                                    PVal(6),
                                    PList(
                                        listOf(
                                            PList(
                                                listOf(
                                                    PVal(7)
                                                )
                                            )
                                        )
                                    )
                                )
                            ),
                        )
                    ),
                )
            ),
            Day2213().parseInput(
                """
                [1,2,[3,[4],5],6,[[7]]]
                [1,2,[3,4,6,[[7]]]]
                """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(13, Day2213().solve1(Day2213().parseInput(testRawInput)))
    }

    @Test
    fun solve2() {
        assertEquals(140, Day2213().solve2(Day2213().parseInput(testRawInput)))
    }
}