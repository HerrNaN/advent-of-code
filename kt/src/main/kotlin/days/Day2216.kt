package days

import Day
import cc.ekblad.konbini.*
import common.*
import days.Day2216.ValveReport
import java.util.*

typealias ValveReports = List<ValveReport>

class Day2216 : Day<ValveReports>() {
    override fun inputParser(): Parser<ValveReports> = lines(valveReport)

    override fun solve1(input: ValveReports): Int {
        return Volcano(
            distMap(input),
            input.rates()
        ).maxPressure(
            "AA",
            30,
            input.filter { it.rate != 0 }.map { it.name }.toSet(),
            0
        )
    }


    override fun solve2(input: ValveReports): Int {
        return Volcano(
            distMap(input),
            input.rates()
        ).maxPressure2(
            "AA",
            input.filter { it.rate != 0 }.map { it.name }.toSet()
        )
    }

    class Volcano(
        private val distFromTo: Map<String, Map<String, Int>>,
        private val rates: Map<String, Int>,
    ) {

        private val cache = mutableMapOf<State, Int>()

        internal data class State(
            val cur: String,
            val minutesLeft: Int,
            val available: Set<String>,
        )

        fun maxPressure(cur: String, minutesLeft: Int, available: Set<String>, ppm: Int): Int {
            if (minutesLeft < 0) throw Error("time exceeded!")
            if (minutesLeft == 0) return 0

            val state = State(cur, minutesLeft, available)
            if (cache.containsKey(state)) {
                return cache[state]!!
            }

            val reachable = available.filter { distFromTo[cur]!![it]!! < minutesLeft }
            if (reachable.isEmpty()) return minutesLeft * ppm

            cache[state] = reachable.maxOf {
                val time = distFromTo[cur]!![it]!! + 1
                time * ppm + maxPressure(
                    it,
                    minutesLeft - time,
                    available - it,
                    ppm + rates[it]!!
                )
            }
            return cache[state]!!
        }

        fun maxPressure2(start: String, available: Set<String>): Int {
            val subsets: List<Set<String>> =
                getSubsetSelectors(available.size).map { uintAsSubset(it, available.toList()).toSet() }
            val complements: List<Set<String>> =
                getSubsetSelectors(available.size).map { uintAsSubset(it.complement(), available.toList()).toSet() }


            var max = 0
            for (i in subsets.indices) {
                val p1 = maxPressure(start, 26, subsets[i], 0)
                val p2 = maxPressure(start, 26, complements[i], 0)
                cache.clear()
                if (p1 + p2 > max) {
                    max = p1 + p2
                }
            }

            return max
        }
    }

    data class ValveReport(
        val name: String,
        val rate: Int,
        val leadsTo: List<String>,
    )

    val valveName = parser { exactly(2, letter).joinToString("") }
    private val valveReport = parser {
        string("Valve ")
        val name = valveName()
        string(" has flow rate=")
        val rate = integer()
        string("; ");
        oneOf(
            parser { string("tunnel leads") },
            parser { string("tunnels lead") }
        )
        string(" to ")
        oneOf(
            parser { string("valve ") },
            parser { string("valves ") }
        )
        val leadsTo = chain(valveName, parser { string(", ") }).terms
        ValveReport(name, rate.toInt(), leadsTo)
    }

    private fun ValveReports.neighbours(): Map<String, Set<String>> =
        this.associate { Pair(it.name, it.leadsTo.toSet()) }

    private fun ValveReports.rates(): Map<String, Int> =
        this.associate { Pair(it.name, it.rate) }

    // dist[from][to] = <cost to go from 'from' to 'to'>
    private fun distMap(valveReports: ValveReports): Map<String, Map<String, Int>> {
        val ns = valveReports.neighbours()
        return valveReports.associate {
            Pair(it.name, valveReports.shortestPathFrom(it.name))
        }
    }

    private fun ValveReports.shortestPathFrom(from: String): Map<String, Int> {
        val dist = mutableMapOf<String, Int>()
        val prev = mutableMapOf<String, String>()
        val q = PriorityQueue<String> { v1, v2 ->
            val toP1 = dist[v1]!!
            val toP2 = dist[v2]!!
            toP1.compareTo(toP2)
        }
        this.forEach { dist[it.name] = Int.MAX_VALUE }
        dist[from] = 0
        this.forEach { q.add(it.name) }

        while (!q.isEmpty()) {
            val u = q.remove()

            this.neighbours()[u]!!
                .filter { q.contains(it) }
                .forEach {
                    val alt = dist[u]!! + 1
                    if (alt <= dist[it]!!) {
                        dist[it] = alt
                        prev[it] = u
                        q.remove(it)
                        q.add(it)
                    }
                }
        }

        return dist
    }
}