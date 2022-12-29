package days

import Day
import cc.ekblad.konbini.*
import common.*
import days.Day2219.Blueprint
import days.Day2219.Resource.*
import java.util.*
import kotlin.math.max

class Day2219 : Day<List<Blueprint>>() {
    override fun inputParser(): Parser<List<Blueprint>> = lines(blueprint)

    override fun solve1(input: List<Blueprint>): Int =
        input.mapIndexed { i, it ->
            (i + 1) * Factory(
                blueprint = it,
                timeLeft = 24,
                resources = mapOf(),
                bots = mapOf(Ore to 1),
                produced = mapOf()
            ).maxGeodes()
        }.sum()


    override fun solve2(input: List<Blueprint>): Int = TODO()

    private val blueprint = parser {
        string("Blueprint ")
        integer()
        string(": Each ore robot costs ")
        val oreCosts = chain(resourceAmount, parser { string(" and ") }).terms
        string(". Each clay robot costs ")
        val clayCosts = chain(resourceAmount, parser { string(" and ") }).terms
        string(". Each obsidian robot costs ")
        val obsidianCosts = chain(resourceAmount, parser { string(" and ") }).terms
        string(". Each geode robot costs ")
        val geodeCosts = chain(resourceAmount, parser { string(" and ") }).terms
        char('.')
        Blueprint(
            costs = mapOf(
                Ore to oreCosts.toMap(),
                Clay to clayCosts.toMap(),
                Obsidian to obsidianCosts.toMap(),
                Geode to geodeCosts.toMap()
            )
        )
    }

    private val resourceAmount: Parser<Pair<Resource, Int>> = parser {
        val n = integer()
        space()
        val t = oneOf(
            parser { string("ore") }.map { Ore },
            parser { string("clay") }.map { Clay },
            parser { string("obsidian") }.map { Obsidian },
            parser { string("geode") }.map { Geode },
        )
        Pair(t, n.toInt())
    }

    data class Factory(
        val blueprint: Blueprint,
        val timeLeft: Int,
        val bots: Map<Resource, Int>,
        val resources: Map<Resource, Int>,
        val produced: Map<Resource, Int>,
    ) : Comparable<Factory> {
        private fun bestEstimate(r: Resource): Int {
            val rBots = timeLeft + bots.getOrDefault(r, 0)
            val nRes = timeLeft * rBots
            return nRes + produced.getOrDefault(r, 0)
        }


        private fun nextStates(): List<Factory> {
            return blueprint.costs.keys.mapNotNull { produceBot(it) } +
                    this.copy(
                        resources = resources unionPlus (bots scalarTimes timeLeft),
                        produced = produced unionPlus (bots scalarTimes timeLeft),
                        timeLeft = 0
                    )
        }

        private fun key(): Map<Resource, Int> =
            produced unionPlus bots

        fun maxGeodes(): Int {
            val heap = PriorityQueue<Factory>()
            heap.add(this)

            val seen = mutableSetOf<Map<Resource, Int>>()
            val seenFactories = mutableSetOf<Factory>()
            var max = 0

            while (heap.isNotEmpty()) {
                val s = heap.poll()

//                if (s.timeLeft == 0) {
//                    return s.resources[Geode] ?: 0
//                }

                if (seen.contains(s.key())) {
                    continue
                }

                seen += s.key()
                seenFactories += s
                max = max(max, s.produced[Geode] ?: 0)

                val ns = s.nextStates()

                val fns = ns.filter {
                    !seen.contains(it.key())
                }.filter {
                    it.bestEstimate(Geode) >= max
                }

                fns.forEach { heap.add(it) }
            }
            return max
//            throw Error("Unreachable")
        }

        private fun produceBot(r: Resource): Factory? =
            produceBot(r, this)

        private fun couldEverProduce(r: Resource): Boolean {
            val total = resources unionPlus (bots scalarTimes timeLeft)
            return total.canAfford(r)
        }

        private fun Map<Resource, Int>.canAfford(r: Resource): Boolean =
            this.canAfford(r, blueprint)

        override fun compareTo(other: Factory): Int =
            other.bestEstimate(Geode).compareTo(this.bestEstimate(Geode))
    }


    data class Blueprint(
        val costs: Map<Resource, Map<Resource, Int>>,
    )

    enum class Resource {
        Ore,
        Clay,
        Obsidian,
        Geode
    }
}

fun Map<Day2219.Resource, Int>.canAfford(r: Day2219.Resource, blueprint: Blueprint): Boolean =
    (this unionMinus blueprint.costs[r]!!).values.all { it >= 0 }

fun Day2219.Factory.couldEverProduce(r: Day2219.Resource): Boolean {
    val total = resources unionPlus (bots scalarTimes timeLeft)
    return total.canAfford(r, blueprint)
}

fun produceBot(r: Day2219.Resource, cur: Day2219.Factory): Day2219.Factory? {
    if (
        !cur.couldEverProduce(r)
        || cur.timeLeft == 1
        || (cur.resources unionMinus cur.bots).canAfford(r, cur.blueprint)
    ) return null

    var newState = cur
    while (newState.timeLeft > 0 && cur.bots == newState.bots) {
        val available = newState.resources
        newState = newState.copy(
            timeLeft = newState.timeLeft - 1,
            resources = newState.resources unionPlus newState.bots,
            produced = newState.produced unionPlus newState.bots,
        )

        if (available.canAfford(r, cur.blueprint)) {
            newState = newState.copy(
                bots = newState.bots unionPlus mapOf(r to 1),
                resources = newState.resources unionMinus cur.blueprint.costs[r]!!
            )
        }
    }
    return newState
}
