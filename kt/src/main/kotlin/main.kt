import days.Day2201
import days.Day2202
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


