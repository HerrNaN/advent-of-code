import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.ParserResult.Error
import cc.ekblad.konbini.ParserResult.Ok
import cc.ekblad.konbini.parseToEnd

abstract class Day<T> {

    abstract fun inputParser(): Parser<T>

    fun parseInput(rawInput: String): T {
        return when (val pRes = inputParser().parseToEnd(rawInput)) {
            is Ok -> pRes.result
            is Error -> throw Error("couldn't parse input: " + pRes.reason)
        }
    }

    open fun solve1(input: T): Any {
        TODO()
    }

    open fun solve2(input: T): Any {
        TODO()
    }
}

