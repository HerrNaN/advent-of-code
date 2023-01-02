import aoc.Part
import days.*
import input.getInput
import kotlin.system.exitProcess

fun main() {
    val year: Int = when (val y = System.getenv()["year"]?.toIntOrNull()) {
        in 2015..2022 -> y as Int
        else -> {
            println("invalid year selected")
            exitProcess(1)
        }
    }

    val day: Int = when (val d = System.getenv()["day"]?.toIntOrNull()) {
        in 1..25 -> d as Int
        else -> {
            println("invalid day selected")
            exitProcess(1)
        }
    }

    val part = when (System.getenv()["part"]) {
        "both" -> Part.Both
        "part2" -> Part.Two
        else -> Part.One
    }

    val input = getInput(year, day).getOrThrow()

    when (val solution = days[day]) {
        null -> {
            println("day not implemented")
            exitProcess(1)
        }

        else -> {
            println("=== Day %02d ===".format(day))
            runPart(solution, part, input)
        }
    }
}

val days = mapOf(
    1 to Day2201(),
    2 to Day2202(),
    3 to Day2203(),
    4 to Day2204(),
    5 to Day2205(),
    6 to Day2206(),
    7 to Day2207(),
    8 to Day2208(),
    9 to Day2209(),
    10 to Day2210(),
    11 to Day2211(),
    12 to Day2212(),
    13 to Day2213(),
    14 to Day2214(),
    15 to Day2215(),
    16 to Day2216(),
    17 to Day2217(),
    18 to Day2218(),
    19 to Day2219(),
    20 to Day2220(),
)

fun <T : Any> runPart(day: Day<T>, part: Part, rawInput: String) {
    try {
        if (part == Part.One || part == Part.Both) {
            print("Part 1: ")
            println(day.solve1(day.parseInput(rawInput)))
        }

        if (part == Part.Two || part == Part.Both) {
            print("Part 2: ")
            println(day.solve2(day.parseInput(rawInput)))
        }
    } catch (e: NotImplementedError) {
        println(e.message)
    }
}


