package common

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow

data class Point2(val x: Int = 0, val y: Int = 0) {
    operator fun unaryMinus(): Point2 = Point2(-x, -y)
    operator fun plus(other: Point2) = Point2(x + other.x, y + other.y)
    operator fun minus(other: Point2) = Point2(x - other.x, y - other.y)
    operator fun times(other: Point2) = Point2(x * other.x, y * other.y)
    operator fun rem(n: Int): Point2 = Point2(x % n, y % n)

    fun distTo(other: Point2): Int {
        val d = this - other
        return max(abs(d.x), abs(d.y))
    }

    fun manhattan(other: Point2): Int {
        val d = this - other
        return abs(d.x) + abs(d.y)
    }
}

data class Point3(val x: Int = 0, val y: Int = 0, val z: Int = 0) {
    operator fun unaryMinus(): Point3 = Point3(-x, -y, -z)
    operator fun plus(other: Point3) = Point3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Point3) = Point3(x - other.x, y - other.y, z - other.z)

    fun distTo(other: Point3): Int {
        val d = this - other
        return max(max(abs(d.x), abs(d.y)), abs(d.z))
    }

    fun manhattan(other: Point3): Int {
        val d = this - other
        return abs(d.x) + abs(d.y) + abs(d.z)
    }

    fun neighbours(): Set<Point3> =
        setOf(
            this + Point3(1, 0, 0),
            this + Point3(0, 1, 0),
            this + Point3(0, 0, 1),
            this + Point3(-1, 0, 0),
            this + Point3(0, -1, 0),
            this + Point3(0, 0, -1)
        )
}

fun Set<Point3>.connectedTo(start: Point3): Set<Point3> {
    val connected = mutableSetOf<Point3>()
    val toConnect = mutableSetOf(start)

    while (toConnect.isNotEmpty()) {
        val p = toConnect.first()
        toConnect -= p
        connected += p

        toConnect.addAll(p.neighbours().minus(connected + toConnect) intersect this)
    }

    return connected
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

data class Rot90(val n: Int) {
    operator fun unaryMinus() = Rot90(-this.n)
    operator fun plus(other: Rot90) = Rot90(this.n + other.n)
    operator fun minus(other: Rot90) = this + -other
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

fun UInt.complement(): UInt = this xor UInt.MAX_VALUE

fun getSubsetSelectors(nBits: Int): List<UInt> = (0U..2.0.pow(nBits.toDouble()).toUInt()).toList()

fun <T> uintAsSubset(n: UInt, l: List<T>): List<T> {
    val subset = mutableListOf<T>()
    for (i in l.indices) {
        if ((n.shr(i) and 1U) == 1U) {
            subset.add(l[i])
        }
    }
    return subset
}


infix fun <K> Map<K, Int>.unionPlus(other: Map<K, Int>): Map<K, Int> =
    (this.keys union other.keys).associateWith {
        (this[it] ?: 0) + (other[it] ?: 0)
    }

infix fun <K> Map<K, Int>.unionMinus(other: Map<K, Int>): Map<K, Int> =
    (this.keys union other.keys).associateWith {
        (this[it] ?: 0) - (other[it] ?: 0)
    }


infix fun <K> Map<K, Int>.scalarTimes(n: Int): Map<K, Int> = this.mapValues { it.value * n }

typealias Counters<T> = Map<T, Int>

