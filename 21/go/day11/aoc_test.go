package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var input = &Cavern{octopi: [10][10]int{
	{5, 4, 8, 3, 1, 4, 3, 2, 2, 3},
	{2, 7, 4, 5, 8, 5, 4, 7, 1, 1},
	{5, 2, 6, 4, 5, 5, 6, 1, 7, 3},
	{6, 1, 4, 1, 3, 3, 6, 1, 4, 6},
	{6, 3, 5, 7, 3, 8, 5, 4, 7, 8},
	{4, 1, 6, 7, 5, 2, 4, 6, 4, 5},
	{2, 1, 7, 6, 8, 4, 1, 7, 2, 1},
	{6, 8, 8, 2, 8, 8, 1, 1, 3, 4},
	{4, 8, 4, 6, 8, 4, 8, 5, 5, 4},
	{5, 2, 8, 3, 7, 5, 1, 5, 2, 6},
}}

func TestAOC_parseInput(t *testing.T) {
	inputString := `5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526
`
	actualParsedInput := parseInput(inputString)
	assert.Equal(t, input, actualParsedInput)
}

func TestAOC_cavern_neighbourPoint(t *testing.T) {
	c := Cavern{[10][10]int{}}

	// Middle
	assert.Equal(t, 8, len(c.neighbourPoints(Point{1, 1})))
	assert.Equal(t, []Point{{1, 2}, {0, 2}, {0, 1}, {0, 0}, {1, 0}, {2, 0}, {2, 1}, {2, 2}}, c.neighbourPoints(Point{1, 1}))

	// Top-Left
	assert.Equal(t, 3, len(c.neighbourPoints(Point{0, 0})))
	assert.Equal(t, []Point{{0, 1}, {1, 0}, {1, 1}}, c.neighbourPoints(Point{0, 0}))

	// Bottom-Right
	assert.Equal(t, 3, len(c.neighbourPoints(Point{9, 9})))
	assert.Equal(t, []Point{{8, 9}, {8, 8}, {9, 8}}, c.neighbourPoints(Point{9, 9}))
}

func TestAOC_getSolutionPart1(t *testing.T) {
	expectedSolution := 1656
	input := *input
	actualSolution := getSolutionPart1(&input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	expectedSolution := 195
	input := *input
	actualSolution := getSolutionPart2(&input)
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

	for i := 0; i < b.N; i++ {
		input := readInput()
		getSolutionPart1(input)
	}
}

func Benchmark_Part2(b *testing.B) {
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		input := readInput()
		getSolutionPart2(input)
	}
}

func TestCavern_totalFlashesDuring(t *testing.T) {
	assert.Equal(t, 204, input.totalFlashesDuring(10))
}
