package days

import Day
import cc.ekblad.konbini.*
import common.letter
import common.lines
import common.space
import days.Day2221.MExpr.*
import days.Day2221.MExpr.EOp.*
import days.Day2221.Op.*


class Day2221 : Day<Map<String, Day2221.MJob>>() {
    override fun inputParser(): Parser<Map<String, MJob>> {
        val monkeyName = parser { many1(letter).joinToString("") }
        val monkeyNum = parser {
            val n = integer()
            MJob.JNum(n)
        }
        val monkeyOp = parser {
            val m1 = monkeyName()
            space()
            val op = oneOf(
                parser { char('+') }.map { Add },
                parser { char('*') }.map { Mul },
                parser { char('/') }.map { Div },
                parser { char('-') }.map { Sub },
            )
            space()
            val m2 = monkeyName()
            MJob.JOp(op, m1, m2)
        }
        val monkeyJob = oneOf(monkeyNum, monkeyOp)

        return parser {
            lines {
                val m = monkeyName()
                string(": ")
                val j = monkeyJob()
                Pair(m, j)
            }.toMap()
        }


    }

    override fun solve1(input: Map<String, MJob>): Long =
        input.toMExpr("root").eval()

    override fun solve2(input: Map<String, MJob>): Long {
        val r = input.humanify().toMExpr("root") as EOp
        val rhs = solveEq(r.e1, r.e2)
        return rhs.eval()
    }

    private fun solveEq(lhs: MExpr, rhs: MExpr): MExpr =
        when {
            // We have extracted the variable
            lhs is EVar -> rhs

            // Move var to lhs
            !lhs.containsVar() ->
                solveEq(rhs, lhs)

            else -> when (lhs) {
                is EDiv -> solveEq(lhs.e1, EMul(rhs, lhs.e2))
                is EMul -> when {
                    lhs.e1.containsVar() ->
                        solveEq(lhs.e1, EDiv(rhs, lhs.e2))

                    else ->
                        solveEq(lhs.e2, EDiv(rhs, lhs.e1))
                }

                is EAdd -> when {
                    lhs.e1.containsVar() ->
                        solveEq(lhs.e1, ESub(rhs, lhs.e2))

                    else ->
                        solveEq(lhs.e2, ESub(rhs, lhs.e1))
                }

                is ESub -> when {
                    lhs.e1.containsVar() ->
                        solveEq(lhs.e1, EAdd(rhs, lhs.e2))

                    else ->
                        solveEq(ESub(lhs.e1, rhs), lhs.e2)
                }

                is ENum, is EVar -> throw Error("unreachable")
            }
        }

    private fun Map<String, MJob>.toMExpr(m: String): MExpr =
        this[m].let {
            when (it) {
                is MJob.JNum -> ENum(it.n)
                is MJob.JOp -> when (it.op) {
                    Add -> EAdd(this.toMExpr(it.m1), this.toMExpr(it.m2))
                    Sub -> ESub(this.toMExpr(it.m1), this.toMExpr(it.m2))
                    Mul -> EMul(this.toMExpr(it.m1), this.toMExpr(it.m2))
                    Div -> EDiv(this.toMExpr(it.m1), this.toMExpr(it.m2))
                }

                is MJob.Humn -> EVar

                null -> throw Error("monkey: '$m' not found")
            }
        }

    fun Map<String, MJob>.humanify(): Map<String, MJob> =
        this.plus(Pair("humn", MJob.Humn))


    sealed class MJob {
        data class JNum(val n: Long) : MJob()
        data class JOp(val op: Op, val m1: String, val m2: String) : MJob()
        object Humn : MJob()
    }

    enum class Op(val f: (Long, Long) -> Long) {
        Mul(Long::times),
        Div(Long::div),
        Add(Long::plus),
        Sub(Long::minus)
    }

    sealed class MExpr {
        sealed class EOp(val op: Op, val e1: MExpr, val e2: MExpr) : MExpr() {
            data class EDiv(private val _e1: MExpr, private val _e2: MExpr) : EOp(Div, _e1, _e2)
            data class EMul(private val _e1: MExpr, private val _e2: MExpr) : EOp(Mul, _e1, _e2)
            data class EAdd(private val _e1: MExpr, private val _e2: MExpr) : EOp(Add, _e1, _e2)
            data class ESub(private val _e1: MExpr, private val _e2: MExpr) : EOp(Sub, _e1, _e2)
        }

        data class ENum(val n: Long) : MExpr()
        object EVar : MExpr()

        fun eval(): Long = when (this) {
            is EOp -> op.f(e1.eval(), e2.eval())
            is ENum -> n
            is EVar -> throw Error("cannot evaluate variable")
        }

        fun containsVar(): Boolean =
            when (this) {
                is EVar -> true
                is ENum -> false
                is EOp -> e1.containsVar() || e2.containsVar()
            }

    }


}