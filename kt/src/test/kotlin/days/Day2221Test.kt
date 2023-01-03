package days

import days.Day2221.MJob.JNum
import days.Day2221.MJob.JOp
import days.Day2221.Op.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2221Test {

    val testInput = mapOf(
        "root" to JOp(Add, "pppw", "sjmn"),
        "dbpl" to JNum(5L),
        "cczh" to JOp(Add, "sllz", "lgvd"),
        "zczc" to JNum(2L),
        "ptdq" to JOp(Sub, "humn", "dvpt"),
        "dvpt" to JNum(3L),
        "lfqf" to JNum(4L),
        "humn" to JNum(5L),
        "ljgn" to JNum(2L),
        "sjmn" to JOp(Mul, "drzm", "dbpl"),
        "sllz" to JNum(4L),
        "pppw" to JOp(Div, "cczh", "lfqf"),
        "lgvd" to JOp(Mul, "ljgn", "ptdq"),
        "drzm" to JOp(Sub, "hmdt", "zczc"),
        "hmdt" to JNum(32L),
    )

    @Test
    fun inputParser() {
        assertEquals(
            testInput, Day2221().parseInput(
                """
            root: pppw + sjmn
            dbpl: 5
            cczh: sllz + lgvd
            zczc: 2
            ptdq: humn - dvpt
            dvpt: 3
            lfqf: 4
            humn: 5
            ljgn: 2
            sjmn: drzm * dbpl
            sllz: 4
            pppw: cczh / lfqf
            lgvd: ljgn * ptdq
            drzm: hmdt - zczc
            hmdt: 32
        """.trimIndent()
            )
        )
    }

    @Test
    fun solve1() {
        assertEquals(152, Day2221().solve1(testInput))
    }

    @Test
    fun solve2() {
        assertEquals(301, Day2221().solve2(testInput))
    }
}