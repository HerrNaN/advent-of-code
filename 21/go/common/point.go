package common

import "math"

type Point struct { X,Y int }

func ManhattanDistance(from, to Point) int {
	return int(math.Abs(float64(to.X - from.X)) + math.Abs(float64(to.Y - from.Y)))
}
