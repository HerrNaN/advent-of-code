package days

import Day
import cc.ekblad.konbini.*
import common.newline
import days.Day2213.Packet.PList
import days.Day2213.Packet.PVal

class Day2213 : Day<List<Pair<Day2213.Packet, Day2213.Packet>>>() {
    override fun inputParser(): Parser<List<Pair<Packet, Packet>>> =
        parser { chain(packetPair, parser { newline(); newline() }).terms }

    override fun solve1(input: List<Pair<Packet, Packet>>): Int = input.foldRightIndexed(0) { index, pair, acc ->
        if (pair.first < pair.second) {
            acc + index + 1
        } else {
            acc
        }
    }

    override fun solve2(input: List<Pair<Packet, Packet>>): Int {
        val d1 = PList(listOf(PList(listOf(PVal(2)))))
        val d2 = PList(listOf(PList(listOf(PVal(6)))))

        val s = input.flatMap { listOf(it.first, it.second) }
            .plus(d1)
            .plus(d2)
            .sorted()

        return (s.indexOf(d1) + 1) * (s.indexOf(d2) + 1)
    }

    sealed class Packet : Comparable<Packet> {
        data class PList(val content: List<Packet>) : Packet()
        data class PVal(val v: Int) : Packet()

        override operator fun compareTo(other: Packet): Int =
            when {
                this is PVal && other is PVal ->
                    this.v.compareTo(other.v)

                this is PList && other is PList ->
                    this.content
                        .zip(other.content)
                        .map { it.first.compareTo(it.second) }
                        .firstOrNull { it != 0 }
                        ?: this.content.size.compareTo(other.content.size)

                this is PVal && other is PList -> PList(listOf(this)).compareTo(other)
                this is PList && other is PVal -> this.compareTo(PList(listOf(other)))
                else -> throw Error("don't know how to compare '$this' to '$other'")
            }
    }

    private val packetPair = parser {
        val p1 = packet()
        newline()
        val p2 = packet()
        Pair(p1, p2)
    }

    private val packetVal: Parser<PVal> = integer.map { PVal(it.toInt()) }
    private val packetList: Parser<PList> = bracket(
        char('['),
        char(']'),
        parser {
            val content = chain(packet, parser { char(',') }).terms
            PList(content)
        }

    )
    private val packet: Parser<Packet> =
        oneOf(packetVal, packetList)
}