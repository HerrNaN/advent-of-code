package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var input = []string{
	"00100",
	"11110",
	"10110",
	"10111",
	"10101",
	"01111",
	"00111",
	"11100",
	"10000",
	"11001",
	"00010",
	"01010",
}

func TestAOC_parseInput(t *testing.T) {
	inputString := `00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010`

	actualParsedInput, err := parseInput(inputString)
	assert.NoError(t, err)
	assert.Equal(t, input, actualParsedInput)
}

func TestAOC_getSolutionPart1(t *testing.T) {
	expectedSolution := 198

	actualSolution := getSolutionPart1(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	expectedSolution := 230

	actualSolution := getSolutionPart2(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_binStringToDec(t *testing.T) {
	val, err := binStringToDec("10110", 16)
	assert.NoError(t, err)
	assert.Equal(t, val, 22)
}

func Benchmark_Parse(b *testing.B) {
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		readInput()
	}
}

func Benchmark_Part1(b *testing.B) {
	b.ReportAllocs()

	input, _ := readInput()

	for i := 0; i < b.N; i++ {
		getSolutionPart1(input)
	}
}

func Benchmark_Part2(b *testing.B) {
	b.ReportAllocs()

	input, _ := readInput()

	for i := 0; i < b.N; i++ {
		getSolutionPart2(input)
	}
}

