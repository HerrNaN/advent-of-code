package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestAOC_parseInput(t *testing.T) {
	input := `forward 5
down 5
forward 8
up 3
down 8
forward 2`
	expectedParsedInput := []command{
		{name: "forward", value: 5},
		{name: "down", value: 5},
		{name: "forward", value: 8},
		{name: "up", value: 3},
		{name: "down", value: 8},
		{name: "forward", value: 2},

	}

	actualParsedInput, err := parseInput(input)
	assert.NoError(t, err)
	assert.Equal(t, expectedParsedInput, actualParsedInput)
}

func TestAOC_getSolutionPart1(t *testing.T) {
	input := []command{
		{name: "forward", value: 5},
		{name: "down", value: 5},
		{name: "forward", value: 8},
		{name: "up", value: 3},
		{name: "down", value: 8},
		{name: "forward", value: 2},

	}
	expectedSolution := 150

	actualSolution := getSolutionPart1(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	input := []command{
		{name: "forward", value: 5},
		{name: "down", value: 5},
		{name: "forward", value: 8},
		{name: "up", value: 3},
		{name: "down", value: 8},
		{name: "forward", value: 2},

	}
	expectedSolution := 900

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
