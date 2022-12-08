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

fun <T, S> ParserState.grid(p: Parser<T>, separator: Parser<S>): List<List<T>> =
    lines(parser { chain(p, separator).terms })
