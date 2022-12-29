package days

import days.Day2219.Blueprint
import days.Day2219.Factory
import days.Day2219.Resource.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day2219Test {

    val testInput = listOf(
//    Blueprint 1:
//    Each ore robot costs 4 ore.
//    Each clay robot costs 2 ore.
//    Each obsidian robot costs 3 ore and 14 clay.
//    Each geode robot costs 2 ore and 7 obsidian.
        Blueprint(
            costs = mapOf(
                Ore to mapOf(
                    Ore to 4
                ),
                Clay to mapOf(
                    Ore to 2
                ),
                Obsidian to mapOf(
                    Ore to 3,
                    Clay to 14
                ),
                Geode to mapOf(
                    Ore to 2,
                    Obsidian to 7
                )
            )
        ),
//    Blueprint 2:
//    Each ore robot costs 2 ore.
//    Each clay robot costs 3 ore.
//    Each obsidian robot costs 3 ore and 8 clay.
//    Each geode robot costs 3 ore and 12 obsidian.
        Blueprint(
            costs = mapOf(
                Ore to mapOf(
                    Ore to 2
                ),
                Clay to mapOf(
                    Ore to 3
                ),
                Obsidian to mapOf(
                    Ore to 3,
                    Clay to 8
                ),
                Geode to mapOf(
                    Ore to 3,
                    Obsidian to 12
                )
            )
        )
    )

    val testInputRaw = """
        Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
        Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
    """.trimIndent()

    @Test
    fun inputParser() {
        assertEquals(
            testInput,
            Day2219().parseInput(testInputRaw)
        )
    }

    @Test
    fun factory() {
//        assertEquals(
//            9, Factory(
//                blueprint = Blueprint(
//                    costs = mapOf(
//                        Ore to mapOf(
//                            Ore to 1
//                        ),
//                        Geode to mapOf(
//                            Ore to 2
//                        )
//                    )
//                ),
//                timeLeft = 5,
//                resources = mapOf(),
//                bots = mapOf(Ore to 1),
//                produced = mapOf()
//            ).maxGeodes()
//        )
        assertEquals(
            9, Factory(
                blueprint = testInput[0],
                timeLeft = 24,
                resources = mapOf(),
                bots = mapOf(Ore to 1),
                produced = mapOf()
            ).maxGeodes()
        )
        assertEquals(
            12, Factory(
                blueprint = testInput[1],
                timeLeft = 24,
                resources = mapOf(),
                bots = mapOf(Ore to 1),
                produced = mapOf()
            ).maxGeodes()
        )
    }

    @Test
    fun canAfford() {
        assertEquals(
            true,
            mapOf(
                Ore to 2,
            ).canAfford(
                Clay, Blueprint(
                    mapOf(
                        Clay to mapOf(
                            Ore to 2
                        )
                    )
                )
            )
        )
        assertEquals(
            false,
            mapOf(
                Ore to 2,
            ).canAfford(
                Clay, Blueprint(
                    mapOf(
                        Clay to mapOf(
                            Ore to 4
                        )
                    )
                )
            )
        )
        assertEquals(
            true,
            mapOf(
                Ore to 2,
                Clay to 4,
                Obsidian to 1
            ).canAfford(
                Geode, Blueprint(
                    mapOf(
                        Geode to mapOf(
                            Ore to 1,
                            Clay to 3,
                            Obsidian to 1
                        )
                    )
                )
            )
        )
    }

    @Test
    fun couldEverProduce() {
        assertEquals(
            true,
            Factory(
                blueprint = Blueprint(
                    mapOf(
                        Geode to mapOf(
                            Ore to 10
                        )
                    )
                ),
                timeLeft = 5,
                bots = mapOf(Ore to 2),
                resources = emptyMap(),
                produced = emptyMap()
            ).couldEverProduce(Geode)
        )
        assertEquals(
            false,
            Factory(
                blueprint = Blueprint(
                    mapOf(
                        Geode to mapOf(
                            Ore to 11
                        )
                    )
                ),
                timeLeft = 5,
                bots = mapOf(Ore to 2),
                resources = emptyMap(),
                produced = emptyMap()
            ).couldEverProduce(Geode)
        )
    }

    @Test
    fun produceBot() {
        val f = Factory(
            blueprint = Blueprint(
                costs = mapOf(
                    Ore to mapOf(
                        Ore to 4
                    ),
                    Clay to mapOf(
                        Ore to 2
                    ),
                    Obsidian to mapOf(
                        Ore to 3,
                        Clay to 14
                    ),
                    Geode to mapOf(
                        Ore to 2,
                        Obsidian to 7
                    )
                )
            ),
            timeLeft = 5,
            resources = mapOf(),
            bots = mapOf(
                Ore to 1,
            ),
            produced = mapOf()
        )
        assertEquals(
            f.copy(
                resources = mapOf(
                    Ore to 1
                ),
                bots = mapOf(
                    Ore to 1,
                    Clay to 1
                ),
                produced = mapOf(
                    Ore to 3
                ),
                timeLeft = 2
            ),
            produceBot(Clay, f)
        )
    }

    @Test
    fun solve1() {
        assertEquals(33, Day2219().solve1(testInput))
    }

    @Test
    fun solve2() {
    }
}