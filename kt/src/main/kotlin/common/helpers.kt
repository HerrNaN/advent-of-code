package common

import kotlin.math.abs
import kotlin.math.max

data class Point2(val x: Int = 0, val y: Int = 0) {
    operator fun unaryMinus(): Point2 = Point2(-x, -y)
    operator fun plus(other: Point2) = Point2(x + other.x, y + other.y)
    operator fun minus(other: Point2) = Point2(x - other.x, y - other.y)

    fun distTo(other: Point2): Int {
        val d = this - other
        return max(abs(d.x), abs(d.y))
    }

    fun manhattan(other: Point2): Int {
        val d = this - other
        return abs(d.x) + abs(d.y)
    }
}

fun Point2.move(dir: Dir2): Point2 = this + dir.p

enum class Dir2(val p: Point2) {
    Up(Point2(0, 1)),
    Down(Point2(0, -1)),
    Right(Point2(1, 0)),
    Left(Point2(-1, 0)),

    UpRight(Up.p + Right.p),
    UpLeft(Up.p + Left.p),
    DownRight(Down.p + Right.p),
    DownLeft(Down.p + Left.p)
}

fun neighbours(p: Point2): List<Point2> = listOf(Dir2.Up, Dir2.Down, Dir2.Left, Dir2.Right).map { p + it.p }

fun MutableSet<Point2>.toGrid(): String {
    val minY = this.minBy { it.y }.y
    val minX = this.minBy { it.x }.x
    val maxY = this.maxBy { it.y }.y
    val maxX = this.maxBy { it.x }.x

    val grid = mutableListOf<String>()
    for (y in (maxY downTo minY)) {
        val row = mutableListOf<Char>()
        for (x in minX..maxX) {
            row += if (this.contains(Point2(x, y))) {
                'x'
            } else {
                '.'
            }
        }
        grid += row.joinToString(" ")
    }
    return grid.joinToString("\n")
}

fun <K1, K2, V> Map<K1, Map<K2, V>>.invert(): Map<K2, Map<K1, V>> {
    return this.flatMap { e1 ->
        e1.value.map { e2 ->
            Triple(e1.key, e2.key, e2.value)
        }
    }.groupBy { it.second }
        .mapValues { e2 ->
            e2.value.associate { t ->
                Pair(t.first, t.third)
            }
        }
}

fun <T> Set<T>.possibleSplitsIn2(): List<Pair<Set<T>, Set<T>>> {
    if (this.size < 2) throw IllegalArgumentException()
    return this.toList().possibleOrders().flatMap {
        (1 until this.size).map { n ->
            Pair(it.take(n).toSet(), it.drop(n).toSet())
        }
    }.distinctBy { it.first }
}

fun <T> List<T>.possibleOrders(): List<List<T>> {
    if (this.isEmpty()) return emptyList()
    if (this.size == 1) return listOf(this.toList())
    return this.flatMap { e ->
        (this - e).possibleOrders().map { listOf(e) + it }
    }
}

