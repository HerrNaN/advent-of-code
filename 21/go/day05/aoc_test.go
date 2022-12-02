package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var input = []Vent{
	{ Point{0, 9}, Point{5, 9} },
	{ Point{8, 0}, Point{0, 8} },
	{ Point{9, 4}, Point{3, 4} },
	{ Point{2, 2}, Point{2, 1} },
	{ Point{7, 0}, Point{7, 4} },
	{ Point{6, 4}, Point{2, 0} },
	{ Point{0, 9}, Point{2, 9} },
	{ Point{3, 4}, Point{1, 4} },
	{ Point{0, 0}, Point{8, 8} },
	{ Point{5, 5}, Point{8, 2} },
}

func TestAOC_parseInput(t *testing.T) {
	inputString := `0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
`

	actualParsedInput := parseInput(inputString)
	assert.Equal(t, input, actualParsedInput)
}

func TestAOC_covered(t *testing.T) {
	assert.Equal(t, []Point{{1,2},{1,3},{1,4}}, covers(Point{1,2}, Point{1,4}))
	assert.Equal(t, []Point{{2,1},{3,1},{4,1}}, covers(Point{2,1}, Point{4,1}))
	assert.Equal(t, []Point{{1,-2},{1,-3},{1,-4}}, covers(Point{1,-2}, Point{1,-4}))
	assert.Equal(t, []Point{{-2,1},{-3,1},{-4,1}}, covers(Point{-2,1}, Point{-4,1}))
	assert.Equal(t, []Point{{9,7},{8,8},{7,9}}, covers(Point{9,7}, Point{7,9}))
}

func TestAOC_getSolutionPart1(t *testing.T) {
	expectedSolution := 5

	actualSolution := getSolutionPart1(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	expectedSolution := 12

	actualSolution := getSolutionPart2(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func Benchmark_Parse(b *testing.B) {
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		readInput()
	}
}

func Benchmark_Part1(b *testing.B) {
	b.ReportAllocs()

	input := readInput()

	for i := 0; i < b.N; i++ {
		getSolutionPart1(input)
	}
}

func Benchmark_Part2(b *testing.B) {
	b.ReportAllocs()

	input := readInput()

	for i := 0; i < b.N; i++ {
		getSolutionPart2(input)
	}
}
