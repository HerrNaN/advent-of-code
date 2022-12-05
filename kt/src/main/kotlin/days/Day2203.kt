package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.many
import common.letter
import common.lines

typealias Rucksacks = List<Rucksack>
typealias Rucksack = List<Item>
typealias Item = Char

fun Item.priority() = when (this) {
    in 'a'..'z' -> 1 + this.code - 'a'.code
    in 'A'..'Z' -> 1 + this.code - 'A'.code + 26
    else -> throw Error("invalid item")
}

fun Rucksack.takeFirstHalf() = this.take(this.size / 2)
fun Rucksack.takeLastHalf() = this.takeLast(this.size / 2)

fun Rucksack.misplacedItem() =
    (this.takeFirstHalf() intersect this.takeLastHalf()).first()

fun Rucksacks.badge() = this.reduce { acc, v -> (acc intersect v).toList() }.first()

class Day2203 : Day<Rucksacks>() {
    override fun inputParser(): Parser<Rucksacks> = lines(many(letter))

    override fun solve1(input: Rucksacks): Int =
        input.sumOf { it.misplacedItem().priority() }

    override fun solve2(input: Rucksacks): Int = input.chunked(3).sumOf { it.badge().priority() }
}