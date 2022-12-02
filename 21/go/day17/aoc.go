package main

import (
	"fmt"
	"io/ioutil"
	"math"
	"os"
	"strings"
)

type Pair struct{ X, Y int }
type Velocity Pair
type Point Pair
type Target struct{ xBound, yBound IntBound }
type IntBound struct{ min, max int }

func (b IntBound) isWithin(i int) bool {
	return i <= b.max && i >= b.min
}

func (p Point) hits(t Target) bool {
	return t.yBound.isWithin(p.Y) && t.xBound.isWithin(p.X)
}

func (p Point) hasMissed(t Target) bool {
	return p.X > t.xBound.max || p.Y < t.yBound.min
}

func (v Velocity) highest() int {
	if v.Y > 0 {
		return v.Y * (v.Y + 1) / 2
	}

	return 0
}

func (v Velocity) farthest() int {
	if v.X == 0 {
		return 0
	}

	if v.X > 0 {
		return v.X * (v.X + 1) / 2
	}

	uvX := int(math.Abs(float64(v.X)))
	return -(uvX * (uvX + 1) / 2)
}

func (v Velocity) willHit(t Target) bool {
	p := Point{}
	for {
		p.X += v.X
		p.Y += v.Y
		if v.X > 0 {
			v.X--
		}
		v.Y--

		if p.hits(t) {
			return true
		} else if p.hasMissed(t) {
			return false
		}
	}
}

func maxYVelThatHits(t Target) int {
	if t.yBound.min > 0 {
		return t.yBound.max
	}

	if t.yBound.max < 0 {
		return int(math.Abs(float64(t.yBound.min + 1)))
	}

	if math.Abs(float64(t.yBound.min)) > math.Abs(float64(t.yBound.max)) {
		return t.yBound.min + 1
	}

	return t.yBound.max
}

func minYVelThatHits(t Target) int {
	if t.yBound.min < 0 {
		return t.yBound.min
	}

	return t.yBound.max
}

func maxXVelThatHits(t Target) int {
	return t.xBound.max
}

func minXVelThatHits(t Target) int {
	p := 1
	q := -2*t.xBound.min
	return int(-p/2 - int(math.Sqrt(math.Pow(float64(p/2), 2.0)) - float64(q)))
}

func getSolutionPart1(t Target) int {
	vy := maxYVelThatHits(t)
	return Velocity{0, vy}.highest()
}

func getSolutionPart2(t Target) int {
	count := 0
	xMax := maxXVelThatHits(t)
	xMin := minXVelThatHits(t)
	yMax := maxYVelThatHits(t)
	yMin := minYVelThatHits(t)

	for vx := xMin; vx <= xMax; vx++ {
		for vy := yMin; vy <= yMax; vy++ {
			v := Velocity{vx,vy}
			if v.willHit(t) {
				count++
			}
		}
	}
	return count
}

func parseInput(input string) Target {
	input = strings.TrimSpace(input)
	var t Target
	fmt.Sscanf(input,
		"target area: x=%d..%d, y=%d..%d",
		&t.xBound.min,
		&t.xBound.max,
		&t.yBound.min,
		&t.yBound.max,
	)
	return t
}

func readInput() Target {
	inputBytes, err := ioutil.ReadFile("input.txt")
	if err != nil {
		panic("couldn't read input")
	}

	return parseInput(string(inputBytes))
}

func main() {
	input := readInput()

	part := os.Getenv("part")

	if part == "part2" {
		fmt.Println(getSolutionPart2(input))
	} else {
		fmt.Println(getSolutionPart1(input))
	}
}
