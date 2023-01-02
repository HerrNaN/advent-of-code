package days

import Day
import cc.ekblad.konbini.*
import common.*
import days.Day2219.Resource
import days.Day2219.Resource.*
import days.Day2219.State
import java.util.*

typealias Blueprint = Map<Resource, Counters<Resource>>

class Day2219 : Day<List<Blueprint>>() {
    override fun solve1(input: List<Blueprint>): Int =
        input.mapIndexed { i, b ->
            val m = maxOf(b, Geode, 24)
            (i + 1) * m
        }.sum()

    override fun solve2(input: List<Blueprint>): Int =
        input.take(3).map { maxOf(it, Geode, 32) }.reduce(Int::times)


    data class State(
        val resources: Counters<Resource>,
        val bots: Counters<Resource>,
        val remaining: Int,
        val produced: Counters<Resource>,
    ) : Comparable<State> {

        fun nextStates(b: Blueprint): List<State> =
            b
                .filter { canBuy(it.value) }
                .filter { shouldBuy(it.key, it.value, b) }
                .map {
                    produce(it.key, it.value)
                } + copy(
                resources = resources unionPlus (bots scalarTimes remaining),
                produced = produced unionPlus (bots scalarTimes remaining),
                remaining = 0
            )

        private fun shouldBuy(r: Resource, cost: Counters<Resource>, b: Blueprint): Boolean {
            // Always buy more Geode bots
            if (r == Geode) return true

            // If we don't have time to utilize the bot there's no use
            if (remaining == 1) return false

            // No need to buy this bot if we can produce enough of its
            // material every minute to create any other bot
            val maxNeededOf = b.mapValues { maxNeededOf(it.key, b) }
            if (maxNeededOf.getOrDefault(r, 0) <= bots.getOrDefault(r, 0)) {
                return false
            }

            return true
        }

        private fun maxNeededOf(r: Resource, b: Blueprint): Int =
            b.maxOf { it.value[r] ?: 0 }

        private fun canBuy(cost: Counters<Resource>): Boolean {
            return couldAffordEventually(cost)
        }

        private fun produce(r: Resource, cost: Counters<Resource>): State {
            var newState = this
            while (newState.remaining > 0 && newState.bots == this.bots) {
                val available = newState.resources
                newState = newState.copy(
                    resources = newState.resources unionPlus newState.bots,
                    produced = newState.produced unionPlus newState.bots,
                    remaining = newState.remaining - 1,
                )

                if (available.canAfford(cost)) {
                    newState = newState.copy(
                        resources = newState.resources unionMinus cost,
                        bots = newState.bots unionPlus mapOf(r to 1)
                    )
                }
            }

            return newState
        }

        private fun couldAffordEventually(cost: Counters<Resource>): Boolean {
            val maxResources = resources unionPlus (bots scalarTimes (remaining - 1))
            return maxResources.canAfford(cost)
        }

        private fun Counters<Resource>.canAfford(cost: Counters<Resource>): Boolean =
            (this unionMinus cost).minOf { it.value } >= 0

        fun bestEstimateFor(r: Resource): Int =
            resources.getOrDefault(r, 0) +
                    (0..remaining).sumOf { bots.getOrDefault(r, 0) + it }

        // Used for the priority queue in the A* search
        override fun compareTo(other: State): Int =
            other.bestEstimateFor(Geode).compareTo(this.bestEstimateFor(Geode))

        override fun toString(): String {
            return "$resources, $bots"
        }
    }


    enum class Resource {
        Ore,
        Clay,
        Obsidian,
        Geode
    }

    override fun inputParser(): Parser<List<Blueprint>> {
        val resourceAmount: Parser<Pair<Resource, Int>> = parser {
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

        val blueprint = parser {
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
            mapOf(
                Ore to oreCosts.toMap(),
                Clay to clayCosts.toMap(),
                Obsidian to obsidianCosts.toMap(),
                Geode to geodeCosts.toMap()
            )

        }

        return lines(blueprint)
    }

}

fun newState(remaining: Int): State =
    State(
        resources = emptyMap(),
        bots = mapOf(Ore to 1),
        remaining = remaining,
        produced = emptyMap(),
    )

fun maxOf(b: Blueprint, r: Resource, after: Int): Int {
    val candidates = PriorityQueue<State>().apply { add(newState(after)) }
    val seen = mutableSetOf<State>()
    var mostOfResource = 0

    while (candidates.isNotEmpty()) {
        val s = candidates.poll()

        if (s.remaining == 0) {
            return s.resources.getOrDefault(r, 0)
        }

        if (seen.contains(s)) {
            continue
        }

        mostOfResource = maxOf(mostOfResource, s.resources.getOrDefault(r, 0))

        seen += s

        candidates += s.nextStates(b)
            .filter { it.remaining >= 0 }
            .filter { it.bestEstimateFor(Geode) >= mostOfResource }
            .filter { !seen.contains(it) }
    }

    return mostOfResource
}
