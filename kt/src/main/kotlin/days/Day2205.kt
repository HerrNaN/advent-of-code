package days

import Day
import cc.ekblad.konbini.*
import common.lines
import common.newline
import common.space

data class Procedure(val stacks: List<CrateStack>, val instructions: Instructions)
typealias CrateStack = ArrayDeque<Char>
typealias Instructions = List<Instruction>

data class Instruction(val n: Long, val from: Long, val to: Long)

val instruction = parser {
    string("move ")
    val n = integer()
    string(" from ")
    val from = integer()
    string(" to ")
    val to = integer()
    Instruction(n, from, to)
}

fun List<CrateStack>.perform1(i: Instruction) {
    (1..i.n)
        .map { this[i.from.toInt() - 1].removeFirst() }
        .forEach { this[i.to.toInt() - 1].addFirst(it) }
}

fun List<CrateStack>.perform2(i: Instruction) {
    (1..i.n)
        .map { this[i.from.toInt() - 1].removeFirst() }
        .reversed()
        .forEach { this[i.to.toInt() - 1].addFirst(it) }
}

val stacks: Parser<List<CrateStack>> = parser {
    val crates = chain(parser { chain(crate, space).terms }, newline).terms
        .flatMapIndexed { row, chars ->
            chars.mapIndexed { col, c -> Triple(row, col + 1, c) }
        }.groupBy { it.second }
        .toList()
        .map { Pair(it.first, ArrayDeque(it.second.mapNotNull { it.third })) }
        .sortedBy { it.first }
        .map { it.second }
        .toList()
    space()
    chain(integer, parser { regex(" *") }).terms
    space()

    crates
}

val crate = oneOf(
    bracket(char('['), char(']'), char),
    string("   ")
).map {
    when (it) {
        is Char -> it
        else -> null
    }
}


class Day2205 : Day<Procedure>() {
    override fun inputParser(): Parser<Procedure> = parser {
        val s = stacks()
        newline()
        newline()
        val instructions = lines(instruction)
        Procedure(s, instructions)
    }

    override fun solve1(input: Procedure): String {
        input.instructions.forEach { input.stacks.perform1(it) }
        return input.stacks.map { it.first() }.joinToString("")
    }

    override fun solve2(input: Procedure): String {
        input.instructions.forEach { input.stacks.perform2(it) }
        return input.stacks.map { it.first() }.joinToString("")
    }
}