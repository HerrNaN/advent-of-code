package common

import cc.ekblad.konbini.*

val newline = regex("\n")
val space = regex(" ")
val letter = regex("[a-zA-Z]").map { it[0] }

//fun <T> lines(p: Parser<T>) = parser { chain(p, newline).terms }
//fun <T> lines(p: Parser<T>) = parser { lines { p } }
//
fun <T> ParserState.lines(p: Parser<T>) = chain(p, newline).terms