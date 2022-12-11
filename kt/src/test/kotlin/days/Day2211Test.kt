package days

import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class Day2211Test {

    val testInput: List<Day2211.Monkey> = listOf(
        Day2211.Monkey(
            startingItems = mutableListOf(BigInteger.valueOf(79), BigInteger.valueOf(98)),
            op = { old -> old * BigInteger.valueOf(19) },
            divider = BigInteger.valueOf(23),
            trueThrow = 2,
            falseThrow = 3,
        ),
        Day2211.Monkey(
            startingItems = mutableListOf(
                BigInteger.valueOf(54),
                BigInteger.valueOf(65),
                BigInteger.valueOf(75),
                BigInteger.valueOf(74)
            ),
            op = { old -> old + BigInteger.valueOf(6) },
            divider = BigInteger.valueOf(19),
            trueThrow = 2,
            falseThrow = 0,
        ),
        Day2211.Monkey(
            startingItems = mutableListOf(BigInteger.valueOf(79), BigInteger.valueOf(60), BigInteger.valueOf(97)),
            op = { old -> old * old },
            divider = BigInteger.valueOf(13),
            trueThrow = 1,
            falseThrow = 3,
        ),
        Day2211.Monkey(
            startingItems = mutableListOf(BigInteger.valueOf(74)),
            op = { old -> old + BigInteger.valueOf(3) },
            divider = BigInteger.valueOf(17),
            trueThrow = 0,
            falseThrow = 1,
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
        assertEquals(2713310158, Day2211().solve2(testInput))
    }
}