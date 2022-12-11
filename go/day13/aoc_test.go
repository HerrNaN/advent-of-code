package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var input = Manual{
	paper: map[Point]bool{
		{6, 10}:  true,
		{0, 14}:  true,
		{9, 10}:  true,
		{0, 3}:   true,
		{10, 4}:  true,
		{4, 11}:  true,
		{6, 0}:   true,
		{6, 12}:  true,
		{4, 1}:   true,
		{0, 13}:  true,
		{10, 12}: true,
		{3, 4}:   true,
		{3, 0}:   true,
		{8, 4}:   true,
		{1, 10}:  true,
		{2, 14}:  true,
		{8, 10}:  true,
		{9, 0}:   true,
	},
	folds: []Fold{
		{
			foldType: FoldTypeHorizontal,
			place:    7,
		},
		{
			foldType: FoldTypeVertical,
			place:    5,
		},
	},
}

func TestAOC_parseInput(t *testing.T) {
	inputString := `6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5
`
	actualParsedInput := parseInput(inputString)
	assert.Equal(t, input, actualParsedInput)
}

func TestAOC_getSolutionPart1(t *testing.T) {
	expectedSolution := 17
	actualSolution := getSolutionPart1(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	// expectedSolution := 3509
	// actualSolution := getSolutionPart2(input)
	// assert.Equal(t, expectedSolution, actualSolution)
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
