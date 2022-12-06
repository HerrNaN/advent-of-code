package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.char
import cc.ekblad.konbini.integer
import cc.ekblad.konbini.parser
import common.lines

typealias Assignments = List<SectionPair>
typealias SectionPair = Pair<Section, Section>
typealias Section = LongRange

val section = parser {
    val s = integer()
    char('-')
    val e = integer()
    Section(s, e)
}

val sectionPair = parser {
    val s1 = section()
    char(',')
    val s2 = section()
    Pair(s1, s2)
}

infix fun Section.fullyContains(other: Section) =
    this.first <= other.first && this.last >= other.last

infix fun Section.doesIntersectWith(other: Section) =
    this.contains(other.first) || this.contains(other.last)
            || other.contains(this.first) || other.contains(this.last)

class Day2204 : Day<Assignments>() {
    override fun inputParser(): Parser<Assignments> = parser { lines(sectionPair) }

    override fun solve1(input: Assignments): Int =
        input.count { it.first fullyContains it.second || it.second fullyContains it.first }

    override fun solve2(input: Assignments): Int = input.count { it.first doesIntersectWith it.second }
}