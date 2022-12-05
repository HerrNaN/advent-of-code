package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.integer
import common.lines

typealias Backpacks = List<Backpack>
typealias Backpack = List<Calorie>
typealias Calorie = Long

class Day2201 : Day<Backpacks>() {
    override fun inputParser(): Parser<Backpacks> = lines(lines(integer))

    override fun solve1(input: Backpacks): Long = input.map { it.sum() }.maxOf { it }

    override fun solve2(input: Backpacks): Long = input.map { it.sum() }.sortedDescending().take(3).sum()
}