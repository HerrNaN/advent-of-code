package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.integer
import common.lines


class Day2201 : Day<List<List<Long>>>() {
    override fun inputParser(): Parser<List<List<Long>>> = lines(lines(integer))

    override fun solve1(input: List<List<Long>>): Long = input.map { it.sum() }.maxOf { it }

    override fun solve2(input: List<List<Long>>): Long = input.map { it.sum() }.sortedDescending().take(3).sum()
}