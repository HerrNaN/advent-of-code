package days

import Day
import cc.ekblad.konbini.*
import common.newline
import common.space

class Day2211 : Day<List<Day2211.Monkey>>() {
    override fun inputParser(): Parser<List<Monkey>> = parser { chain(monkey, whitespace).terms }

    override fun solve1(input: List<Monkey>): Int {
        val rounds = (1..20).scan(
            input
        ) { acc, _ -> acc.doRound() }

//        rounds.drop(1).forEachIndexed { r, ms ->
//            println("After round ${r + 1}:")
//            ms.forEachIndexed { i, m -> println("Money $i: ${m.startingItems}") }
//        }

//        rounds.last().forEachIndexed { i, m -> println("Monkey $i inspected items ${m.inspectCount} times.") }

        return rounds.last()
            .sortedByDescending { it.inspectCount }
            .take(2)
            .fold(1) { acc, monkey -> acc * monkey.inspectCount }
    }

    override fun solve2(input: List<Monkey>): Int = TODO()

    data class Monkey(
        var startingItems: MutableList<Int>,
        var inspectCount: Int = 0,
    ) {
        lateinit var op: (Int) -> Int
        lateinit var next: (Int) -> Int

        constructor(startingItems: MutableList<Int>, op: (Int) -> Int, next: (Int) -> Int) : this(startingItems) {
            this.op = op
            this.next = next
        }
    }

    private fun testFun(divBy: Int, trueThrow: Int, falseThrow: Int): (Int) -> Int = {
        if (it % divBy == 0) {
            trueThrow
        } else {
            falseThrow
        }
    }

    private fun List<Monkey>.doRound(): List<Monkey> {
        val newMonkeys = this.toMutableList()

        for (i in this.indices) {
//            println("Monkey $i")
            this[i].startingItems.forEach {
//                println("  Monkey inspects an item with a worry level of $it")
                val inspectionWorry = this[i].op(it)
//                println("    Worry level is changed to $inspectionWorry")
                val boredWorry = inspectionWorry / 3
//                println("    Monkey gets bored with item. Worry level is divided by 3 to $boredWorry")
                val throwTo = this[i].next(boredWorry)
//                println("    Item with worry $boredWorry is thrown to monkey $throwTo")
                newMonkeys[throwTo].startingItems += boredWorry
                this[i].inspectCount++
                this[i].startingItems = this[i].startingItems.drop(1).toMutableList()
            }
        }

        return newMonkeys
    }

    val monkey = parser {
        string("Monkey ")
        integer()
        string(":")
        newline()
        space(); space()
        string("Starting items: ")
        val startingItems = chain(integer.map { it.toInt() }, parser { string(", ") }).terms
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
        Monkey(startingItems.toMutableList(), op, testFun(divBy.toInt(), trueThrow.toInt(), falseThrow.toInt()))
    }

    val operation = parser {
        val first = oneOf(parser { string("old") }, integer)
        space()
        val op = oneOf(parser { char('+') }, parser { char('*') })
        space()
        val second = oneOf(parser { string("old") }, integer)
        fun(it: Int): Int {
            var f: Int = it
            var s: Int = it

            if (first is Long) {
                f = first.toInt()
            }

            if (second is Long) {
                s = second.toInt()
            }

            return if (op == '*') {
                f * s
            } else {
                f + s
            }
        }
    }
}