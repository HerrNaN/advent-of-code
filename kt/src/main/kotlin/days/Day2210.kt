package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.integer
import cc.ekblad.konbini.oneOf
import cc.ekblad.konbini.parser
import common.lines
import common.space
import java.util.*

class Day2210 : Day<List<Day2210.Instr>>() {

    override fun inputParser(): Parser<List<Instr>> = lines(oneOf(addx, noop))

    override fun solve1(input: List<Instr>): Int =
        (2..220).scan(
            Pair(1, CPU(1, input))
        ) { acc, cycle ->
//            println(acc.toString())
            Pair(cycle, acc.second.tick())
        }.filter { setOf(20, 60, 100, 140, 180, 220).contains(it.first) }
//            .map { println(it); it }
            .sumOf { it.second.x * it.first }


    override fun solve2(input: List<Instr>): Int = TODO()

    sealed class Instr(val cycles: Int) {
        data class AddX(val v: Int) : Instr(2)
        object Noop : Instr(1)
    }

    internal data class CPU(
        val x: Int = 1,
        val ops: Stack<Instr> = Stack(),
    ) {

        constructor(x: Int, instructions: List<Instr>) : this(x) {
            val compiled = instructions.flatMap {
                when (it) {
                    is Instr.AddX -> listOf(Instr.Noop, it)
                    else -> listOf(it)
                }
            }.reversed()
            compiled.forEach { ops.push(it) }
        }

        fun tick(): CPU = when (val i = ops.pop()) {
            is Instr.AddX -> CPU(x + i.v, ops)
            is Instr.Noop -> this
        }
    }

    private val addx = parser {
        string("addx")
        space()
        val v = integer()
        Instr.AddX(v.toInt())
    }

    private val noop = parser {
        string("noop")
        Instr.Noop
    }

}