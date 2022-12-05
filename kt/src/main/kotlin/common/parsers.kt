package common

import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.chain
import cc.ekblad.konbini.parser
import cc.ekblad.konbini.regex

val newline = regex("\n")
val space = regex(" ")

fun <T> lines(p: Parser<T>) = parser { chain(p, newline).terms }
