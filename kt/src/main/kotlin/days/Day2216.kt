package days

import Day
import cc.ekblad.konbini.*
import common.exactly
import common.letter
import common.lines
import days.Day2216.ValveReport
import days.Day2216.Volcano.Elephant
import java.util.*

typealias ValveReports = List<ValveReport>

class Day2216 : Day<ValveReports>() {
    override fun inputParser(): Parser<ValveReports> = lines(valveReport)

    override fun solve1(input: ValveReports): Int {
        return Volcano(
            distMap(input),
            input.rates()
        ).maxPressure(
            listOf(Elephant("AA", null)),
            30,
            input.filter { it.rate != 0 }.map { it.name }.toSet(), 0
        )
    }


    // Tried 2046 -> Too low
    override fun solve2(input: ValveReports): Int {
        return Volcano(
            distMap(input),
            input.rates()
        ).maxPressure(
            listOf(Elephant("AA", null), Elephant("AA", null)),
            26,
            input.filter { it.rate != 0 }.map { it.name }.toSet(), 0
        )
    }

    class Volcano(
        private val distFromTo: Map<String, Map<String, Int>>,
        private val rates: Map<String, Int>,
    ) {

        internal class Cache(
            private val c: MutableMap<State, Int>,
        ) {
            var counter = 0
//            var max = 0

            fun containsKey(s: State): Boolean {
                return c.containsKey(s)
            }

            fun get(s: State): Int? {
                return c[s]
            }

            fun set(s: State, v: Int) {
                counter++
                if (counter % 100000 == 0) {
                    println(c.maxOf { it.value })
                }
                c[s] = v
            }
        }

        private val cache = Cache(mutableMapOf())

        internal data class State(
            val ps: List<Elephant>,
            val minutesLeft: Int,
            val available: Set<String>,
        )

        data class Elephant(val cur: String, val goal: Pair<String, Int>?)

        fun maxPressure(ps: List<Elephant>, minutesLeft: Int, available: Set<String>, ppm: Int): Int {
            if (minutesLeft < 0) throw Error("time exceeded!")
            if (minutesLeft == 0) return 0

            val state = State(ps, minutesLeft, available.toSet())
            if (cache.containsKey(state)) {
                return cache.get(state)!!
            }

            if (ps.isEmpty()) return ppm * minutesLeft

            // Open valves
            var newPPM = ppm
            for (p in ps) {
                if (p.goal != null && p.goal.second == 0) {
                    newPPM += rates[p.goal.first]!!
                }
            }

            var newMinutesLeft = minutesLeft

            if (newPPM != ppm) {
                newMinutesLeft--
            }

            val newPs = ps.map {
                if (it.goal != null) {
                    if (it.goal.second == 0) {
                        it.copy(cur = it.goal.first, goal = null)
                    } else {
                        it.copy(goal = it.goal.copy(second = it.goal.second - 1))
                    }
                } else {
                    it
                }
            }

            // Make sure everyone that can has a goal
            if (newPs.any { it.goal == null }) {
                cache.set(state, newPs.filter { it.goal == null }.maxOf { p ->
                    val reachable = available.filter { distFromTo[p.cur]!![it]!! < newMinutesLeft - 1 }
                    if (reachable.isEmpty()) return maxPressure(newPs - p, newMinutesLeft, available, newPPM)

                    reachable.maxOf {
                        maxPressure(
                            newPs - p + p.copy(goal = Pair(it, distFromTo[p.cur]!![it]!!)),
                            newMinutesLeft,
                            available - it,
                            newPPM
                        )
                    }
                })
                return cache.get(state)!!
            }


            // Move to valves
            val p = newPs.minBy { it.goal!!.second }
            cache.set(
                state, newPPM * p.goal!!.second + maxPressure(
                    newPs.map {
                        it.copy(goal = Pair(it.goal!!.first, it.goal.second - p.goal.second))
                    },
                    newMinutesLeft - p.goal.second,
                    available,
                    newPPM
                )
            )
            return cache.get(state)!!
        }

//        fun maxP(): Int {
//            while
//        }

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