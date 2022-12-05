package common

import cc.ekblad.konbini.*

val newline = regex("\n")
val space = regex(" ")

fun <T> lines(p: Parser<T>) = parser { chain(p, newline).terms }
fun <T> maybe(p: Parser<T>) = oneOf(p)
