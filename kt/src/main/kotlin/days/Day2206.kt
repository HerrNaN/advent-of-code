package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.many1
import common.letter

typealias DataStream = List<Char>

class Day2206 : Day<DataStream>() {
    override fun inputParser(): Parser<DataStream> = many1(letter)

    override fun solve1(input: DataStream): Int {
        for (i in 4 until input.size) {
            if (input.slice(i - 3..i).distinct().size == 4) {
                return i + 1
            }
        }

        throw Error("Solution not found")
    }

    override fun solve2(input: DataStream): Int {
        for (i in 14 until input.size) {
            if (input.slice(i - 13..i).distinct().size == 14) {
                return i + 1
            }
        }

        throw Error("Solution not found")
    }
}