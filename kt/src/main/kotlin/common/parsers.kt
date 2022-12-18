package common

import cc.ekblad.konbini.*

val newline = regex("\n")
val space = regex(" ")
val letter = regex("[a-zA-Z]").map { it[0] }
val digit = regex("[0-9]").map { it.toInt() }

fun <T> lines(p: Parser<T>) = parser { lines(p) }
fun <T> ParserState.lines(p: Parser<T>) = chain(p, newline).terms

inline fun <reified T> grid(noinline p: Parser<T>): Parser<Array<Array<T>>> =
    lines(parser { many1(p) }.map { it.toTypedArray() }).map { it.toTypedArray() }

inline fun <reified T> gridMap(noinline p: Parser<T>): Parser<Map<Point2, T>> =
    grid(p).map {
        val grid = mutableMapOf<Point2, T>()
        it.forEachIndexed { y, row -> row.forEachIndexed { x, v -> grid[Point2(x, y)] = v } }
        grid
    }

fun <T, S> ParserState.grid(p: Parser<T>, separator: Parser<S>): List<List<T>> =
    lines(parser { chain(p, separator).terms })

fun <T> ParserState.exactly(n: Int, p: Parser<T>): List<T> =
    (1..n).map { p() }