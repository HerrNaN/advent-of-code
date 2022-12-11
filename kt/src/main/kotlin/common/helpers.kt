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