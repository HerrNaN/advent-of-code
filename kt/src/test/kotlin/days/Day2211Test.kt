package days

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class Day2211Test {

    val testInput: List<Day2211.Monkey> = listOf(
        Day2211.Monkey(
            startingItems = mutableListOf(79, 98),
            op = { old -> old * 19 },
            next = { worry ->
                if (worry % 23 == 0) {
                    2
                } else {
                    3
                }
            }
        ),
        Day2211.Monkey(
            startingItems = mutableListOf(54, 65, 75, 74),
            op = { old -> old + 6 },
            next = { worry ->
                if (worry % 19 == 0) {
                    2
                } else {
                    0
                }
            }
        ),
        Day2211.Monkey(
            startingItems = mutableListOf(79, 60, 97),
            op = { old -> old * old },
            next = { worry ->
                if (worry % 13 == 0) {
                    1
                } else {
                    3
                }
            }
        ),
        Day2211.Monkey(
            startingItems = mutableListOf(74),
            op = { old -> old + 3 },
            next = { worry ->
                if (worry % 17 == 0) {
                    0
                } else {
                    1
                }
            }
        )

    )

    @Test
    fun inputParser() {
        assertContentEquals(
            testInput,
            Day2211().parseInput(
                """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
""".trim()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(10605, Day2211().solve1(testInput))
    }

    @Test
    fun solve2() {
    }
}