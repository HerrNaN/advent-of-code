package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var input = [][]int{
	{2, 1, 9, 9, 9, 4, 3, 2, 1, 0},
	{3, 9, 8, 7, 8, 9, 4, 9, 2, 1},
	{9, 8, 5, 6, 7, 8, 9, 8, 9, 2},
	{8, 7, 6, 7, 8, 9, 6, 7, 8, 9},
	{9, 8, 9, 9, 9, 6, 5, 6, 7, 8}}

func TestAOC_parseInput(t *testing.T) {
	inputString := `2199943210
3987894921
9856789892
8767896789
9899965678
`
	actualParsedInput := parseInput(inputString)
	assert.Equal(t, input, actualParsedInput)
}

func TestAOC_getSolutionPart1(t *testing.T) {
	expectedSolution := 15
	actualSolution := getSolutionPart1(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	expectedSolution := 1134
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
