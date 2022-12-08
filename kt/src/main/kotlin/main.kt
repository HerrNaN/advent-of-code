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
        "part2" -> 2
        else -> 1
    }

    val input = getInput(year, day).getOrThrow()

    when (val solution = days[day]) {
        null -> {
            println("day not implemented")
            exitProcess(1)
        }

        else -> runPart(solution, part, input)
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


