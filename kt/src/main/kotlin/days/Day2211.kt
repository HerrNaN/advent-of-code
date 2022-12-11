package days

import Day
import cc.ekblad.konbini.*
import common.newline
import common.space
import java.math.BigInteger

class Day2211 : Day<List<Day2211.Monkey>>() {
    override fun inputParser(): Parser<List<Monkey>> = parser { chain(monkey, whitespace).terms }

    override fun solve1(input: List<Monkey>): Int {
        val round = input.playNRounds(20, true)

        return round
            .sortedByDescending { it.inspectCount }
            .take(2)
            .fold(1) { acc, monkey -> acc * monkey.inspectCount }
    }

    override fun solve2(input: List<Monkey>): Long = input.businessAfterN(10000, false)

    private fun List<Monkey>.playNRounds(n: Int, relief: Boolean): List<Monkey> {
        val rounds = (1..n).scan(
            this
        ) { acc, _ -> acc.doRound(relief) }

        return rounds.last()
    }

    private fun List<Monkey>.businessAfterN(n: Int, relief: Boolean): Long =
        this.playNRounds(n, relief).business()

    private fun List<Monkey>.business(): Long =
        this.sortedByDescending { it.inspectCount }
            .take(2)
            .fold(1) { acc, monkey -> acc * monkey.inspectCount.toLong() }

    data class Monkey(
        val startingItems: MutableList<BigInteger>,
        val op: (BigInteger) -> BigInteger,
        val divider: BigInteger,
        val trueThrow: Int,
        val falseThrow: Int,
        val inspectCount: Int = 0,
    ) {
        fun next(worry: BigInteger): Int =
            if (worry.mod(divider) == BigInteger.ZERO) {
                trueThrow
            } else {
                falseThrow
            }

        override fun equals(other: Any?): Boolean {
            return other is Monkey &&
                    this.startingItems == other.startingItems &&
                    this.inspectCount == other.inspectCount &&
                    this.divider == other.divider &&
                    this.falseThrow == other.falseThrow &&
                    this.trueThrow == other.trueThrow

        }
    }


    private fun List<Monkey>.doRound(relief: Boolean): List<Monkey> {
        val newMonkeys = this.toMutableList()

        val bigDiv = this.fold(BigInteger.ONE) { acc, m -> acc.multiply(m.divider) }

        newMonkeys.forEachIndexed { i, m ->
            m.startingItems.forEach {
                var inspectionWorry = m.op(it)
                inspectionWorry = if (relief) {
                    inspectionWorry.div(BigInteger.valueOf(3))
                } else {
                    inspectionWorry.mod(bigDiv)
                }
                val throwTo = m.next(inspectionWorry)
                newMonkeys[throwTo].startingItems += inspectionWorry
                newMonkeys[i] = m.copy(inspectCount = newMonkeys[i].inspectCount + 1)
            }
            newMonkeys[i].startingItems.clear()
        }

        return newMonkeys.toList()
    }

    private val monkey = parser {
        string("Monkey ")
        integer()
        string(":")
        newline()
        space(); space()
        string("Starting items: ")
        val startingItems = chain(integer.map { BigInteger.valueOf(it) }, parser { string(", ") }).terms
        whitespace()
        string("Operation: new = ")
        val op = operation()
        whitespace()
        string("Test: divisible by ")
        val divBy = integer()
        whitespace()
        string("If true: throw to monkey ")
        val trueThrow = integer()
        whitespace()
        string("If false: throw to monkey ")
        val falseThrow = integer()
        Monkey(startingItems.toMutableList(), op, BigInteger.valueOf(divBy), trueThrow.toInt(), falseThrow.toInt())
    }

    val operation = parser {
        val first = oneOf(parser { string("old") }, integer)
        space()
        val op = oneOf(parser { char('+') }, parser { char('*') })
        space()
        val second = oneOf(parser { string("old") }, integer)
        fun(it: BigInteger): BigInteger {
            var f = it
            var s = it

            if (first is Long) {
                f = BigInteger.valueOf(first)
            }

            if (second is Long) {
                s = BigInteger.valueOf(second)
            }

            return if (op == '*') {
                f.multiply(s)
            } else {
                f.add(s)
            }
        }
    }
}