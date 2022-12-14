package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.parser
import common.Point2
import common.gridMap
import common.letter
import common.neighbours
import java.util.*


class Day2212 : Day<Day2212.HeightMap>() {
    override fun inputParser(): Parser<HeightMap> = parser {
        val map = gridMap(letter)()
        HeightMap(
            map.mapValues {
                when (it.value) {
                    'S' -> 0
                    'E' -> 26
                    in 'a'..'z' -> it.value - 'a' + 1
                    else -> this.fail("unknown height")
                }
            },
            map.filterValues { it == 'S' }.keys.first(),
            map.filterValues { it == 'E' }.keys.first(),
        )
    }

    override fun solve1(input: HeightMap): Int = input.shortestPath().first[input.start]!!

    override fun solve2(input: HeightMap): Int = input.shortestPath().first
        .filter { input.map[it.key] == 1 && it.value >= 0 }
        .minBy { it.value }.value

    data class HeightMap(
        val map: Map<Point2, Int>,
        val start: Point2,
        val end: Point2,
    ) {
        fun shortestPath(): Pair<Map<Point2, Int>, Map<Point2, Point2>> {
            val dist = mutableMapOf<Point2, Int>()
            val prev = mutableMapOf<Point2, Point2>()
            val q = PriorityQueue<Point2> { p1, p2 ->
                val toP1 = dist[p1]!!
                val toP2 = dist[p2]!!
                toP1.compareTo(toP2)
            }
            map.keys.forEach { dist[it] = Int.MAX_VALUE }
            dist[end] = 0
            map.keys.forEach { q.add(it) }

            while (!q.isEmpty()) {
                val u = q.remove()

                neighbours(u)
                    .filter { q.contains(it) }
                    .filter { map[u]!! - map[it]!! <= 1 }
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

            return Pair(dist, prev)
        }
    }
}
