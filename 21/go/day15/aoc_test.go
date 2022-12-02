package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var input = Cavern([][]int{
	{1,1,6,3,7,5,1,7,4,2},
	{1,3,8,1,3,7,3,6,7,2},
	{2,1,3,6,5,1,1,3,2,8},
	{3,6,9,4,9,3,1,5,6,9},
	{7,4,6,3,4,1,7,1,1,1},
	{1,3,1,9,1,2,8,1,3,7},
	{1,3,5,9,9,1,2,4,2,1},
	{3,1,2,5,4,2,1,6,3,9},
	{1,2,9,3,1,3,8,5,2,1},
	{2,3,1,1,9,4,4,5,8,1},
})

func TestAOC_parseInput(t *testing.T) {
	inputString := `1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581
`
	actualParsedInput := parseInput(inputString)
	assert.Equal(t, input, actualParsedInput)
}

func TestAOC_cavern_neighbourPoint(t *testing.T) {
	c := Cavern(make([][]int, 10))

	// Middle
	assert.Equal(t, 4, len(c.neighbourPoints(Point{1, 1})))
	assert.Equal(t, []Point{{1, 2}, {0, 1}, {1, 0}, {2, 1}}, c.neighbourPoints(Point{1, 1}))

	// Top-Left
	assert.Equal(t, 2, len(c.neighbourPoints(Point{0, 0})))
	assert.Equal(t, []Point{{0, 1}, {1, 0}}, c.neighbourPoints(Point{0, 0}))

	// Bottom-Right
	assert.Equal(t, 2, len(c.neighbourPoints(Point{9, 9})))
	assert.Equal(t, []Point{{8, 9}, {9, 8}}, c.neighbourPoints(Point{9, 9}))
}

func TestAOC_getSolutionPart1(t *testing.T) {
	expectedSolution := 40
	actualSolution := getSolutionPart1(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	expectedSolution := 315
	actualSolution := getSolutionPart2(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_Cavern_Scaled(t *testing.T) {
	c := Cavern([][]int{{8}})
	expected := Cavern([][]int{
		{8,9,1,2,3},
		{9,1,2,3,4},
		{1,2,3,4,5},
		{2,3,4,5,6},
		{3,4,5,6,7},
	})
	assert.Equal(t, expected, c.Scaled(5))
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
