package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestAOC_parseInput(t *testing.T) {
	input := "1337\n42"
	expectedParsedInput := []int{1337,42}

	actualParsedInput, err := parseInput(input)
	assert.NoError(t, err)
	assert.Equal(t, expectedParsedInput, actualParsedInput)
}

func TestAOC_getSolutionPart1(t *testing.T) {
	input := []int{199,200,208,210,200,207,240,269,260,263}
	expectedSolution := 7

	actualSolution := getSolutionPart1(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	input := []int{199,200,208,210,200,207,240,269,260,263}
	expectedSolution := 5

	actualSolution := getSolutionPart2(input)
	assert.Equal(t, expectedSolution, actualSolution)
}
