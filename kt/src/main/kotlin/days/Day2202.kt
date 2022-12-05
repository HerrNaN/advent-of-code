package days

import Day
import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.parser
import common.lines
import common.space
import days.Outcome.*
import days.Shape.*

typealias Input = List<Round>
typealias Round = Pair<Shape, Shape>

fun Round.eval1(): Int = this.second.score + when {
    this.second.beats(this.first) -> Win.score
    this.first.beats(this.second) -> Lose.score
    else -> Draw.score
}


fun Round.eval2(): Int =
    this.second.asOutcome().score + selectShape(this.second.asOutcome(), this.first).score

fun selectShape(outcome: Outcome, opposing: Shape) = when (outcome) {
    Win -> when (opposing) {
        Rock -> Paper
        Paper -> Scissors
        Scissors -> Rock
    }

    Lose -> when (opposing) {
        Rock -> Scissors
        Scissors -> Paper
        Paper -> Rock
    }

    Draw -> opposing
}

fun Shape.asOutcome(): Outcome = when (this) {
    Rock -> Lose
    Paper -> Draw
    Scissors -> Win
}

enum class Shape(val opponentSign: Char, val playerSign: Char, val score: Int) {
    Rock('A', 'X', 1),
    Paper('B', 'Y', 2),
    Scissors('C', 'Z', 3);

    fun beats(other: Shape): Boolean =
        other == when (this) {
            Rock -> Scissors
            Scissors -> Paper
            Paper -> Rock
        }

}

val shape: Parser<Shape> = parser {
    when (char()) {
        Rock.opponentSign, Rock.playerSign -> Rock
        Paper.opponentSign, Paper.playerSign -> Paper
        Scissors.opponentSign, Scissors.playerSign -> Scissors
        else -> this.fail("invalid shape")
    }
}

val round = parser {
    val op = shape()
    space()
    val pl = shape()
    Pair(op, pl)
}

enum class Outcome(val score: Int) {
    Win(6),
    Draw(3),
    Lose(0)
}

class Day2202 : Day<Input>() {
    override fun inputParser(): Parser<Input> = lines(round)

    override fun solve1(input: Input): Int = input.sumOf { it.eval1() }

    override fun solve2(input: Input): Int = input.sumOf { it.eval2() }
}