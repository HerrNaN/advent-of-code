package days

import Day
import cc.ekblad.konbini.*
import common.Dir2

class Day2217 : Day<List<Dir2>>() {
    override fun inputParser(): Parser<List<Dir2>> =
        many1(
            parser {
                oneOf(
                    parser { char('<') }.map { Dir2.Left },
                    parser { char('>') }.map { Dir2.Right }
                )
            }
        )

    override fun solve1(input: List<Dir2>): Int = TODO()

    override fun solve2(input: List<Dir2>): Int = TODO()
}