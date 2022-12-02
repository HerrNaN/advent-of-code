package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var input = []int{16,1,2,0,4,2,7,1,2,14}

func TestAOC_parseInput(t *testing.T) {
	inputString := `16,1,2,0,4,2,7,1,2,14
`

	actualParsedInput := parseInput(inputString)
	assert.Equal(t, input, actualParsedInput)
}

func TestAOC_getSolutionPart1(t *testing.T) {
	expectedSolution := 37
	is := make([]int, len(input))
	copy(is, input)

	actualSolution := getSolutionPart1(is)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	expectedSolution := 168
	is := make([]int, len(input))
	copy(is, input)

	actualSolution := getSolutionPart2(is)
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
