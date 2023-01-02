package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.integer
import cc.ekblad.konbini.parser
import common.lines

class Day2220 : Day<List<Long>>() {

    private val decKey: Long = 811589153
    override fun inputParser(): Parser<List<Long>> =
        parser { lines(integer).map { l -> l } }

    override fun solve1(input: List<Long>): Long = decryptFile(input, 1, 1)

    override fun solve2(input: List<Long>): Long = decryptFile(input, 10, decKey)

    private fun decryptFile(file: List<Long>, iterations: Int, key: Long): Long {
        val ps = file.mapIndexed { index, i -> Pair(index, i * key) }

        var l = ps

        repeat((1..iterations).count()) {
            ps.forEach {
                l = l.move(it)
            }
        }


        val zeroIdx = l.indexOf(ps.find { it.second == 0L })

        return l.getAtWrapped(zeroIdx + 1000).second +
                l.getAtWrapped(zeroIdx + 2000).second +
                l.getAtWrapped(zeroIdx + 3000).second
    }

    private fun <E> List<E>.getAtWrapped(idx: Int): E = this[idx % size]

    private fun List<Pair<Int, Long>>.move(p: Pair<Int, Long>): List<Pair<Int, Long>> {
        val d = this.find { it == p }!!
        val idx = indexOf(d)
        val newList = this.filter { it != d }
        val newIdx = (((idx + d.second) % (size - 1) + (size - 1)) % (size - 1)).toInt()
        return if (newIdx == 0) {
            newList + d
        } else {
            newList.subList(0, newIdx) + d + newList.subList(newIdx, lastIndex)
        }
    }

}

