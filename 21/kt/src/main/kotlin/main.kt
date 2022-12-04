import days.*
import input.getInput

fun main() {
    val day = System.getenv()["day"]!!.toInt()
    val part = when (System.getenv()["part"]) {
        "part2" -> 2
        else -> 1
    }


    val input = when (System.`in`.available()) {
        0 -> getInput(2021, day).getOrThrow()
        else -> System.`in`.readBytes().toString()
    }

    runPart(days[day]!!, part, input)
}

val days = mapOf(
    1 to Day01(),
    2 to Day02(),
    3 to Day03(),
    4 to Day04(),
    5 to Day05(),
    6 to Day06(),
    7 to Day07(),
    8 to Day08(),
    9 to Day09(),
    10 to Day10(),
    11 to Day11(),
    12 to Day12(),
    13 to Day13(),
    14 to Day14(),
    15 to Day15(),
    16 to Day16(),
    17 to Day17(),
    18 to Day18(),
    19 to Day19(),
    20 to Day20(),
    21 to Day21(),
    22 to Day22(),
    23 to Day23(),
    24 to Day24(),
    25 to Day25(),
)

fun <T : Any> runPart(day: Day<T>, part: Int, rawInput: String) {
    print("Part $part: ")
    try {
        when (part) {
            1 -> println(day.solve1(day.parseInput(rawInput)))
            2 -> println(day.solve2(day.parseInput(rawInput)))
        }
    } catch (e: NotImplementedError) {
        println(e.message)
    }
}


