package days

import Day
import cc.ekblad.konbini.*

class Day01 : Day<List<Long>>() {
    override fun inputParser(): Parser<List<Long>> =
        parser {
            chain(integer, whitespace).terms
        }

    override fun solve1(input: List<Long>): Int {
        var nLarger = 0
        for (i in 1 until input.size) {
            if (input[i - 1] < input[i]) {
                nLarger++
            }
        }

        return nLarger
    }
}