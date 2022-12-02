package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var input = Formula{
	template: "NNCB",
	polymer: map[string]int{
		"NN": 1,
		"NC": 1,
		"CB": 1,
	},
	rules: Rules{
		"CH": [2]string{"CB","BH"},
		"HH": [2]string{"HN","NH"},
		"CB": [2]string{"CH","HB"},
		"NH": [2]string{"NC","CH"},
		"HB": [2]string{"HC","CB"},
		"HC": [2]string{"HB","BC"},
		"HN": [2]string{"HC","CN"},
		"NN": [2]string{"NC","CN"},
		"BH": [2]string{"BH","HH"},
		"NC": [2]string{"NB","BC"},
		"NB": [2]string{"NB","BB"},
		"BN": [2]string{"BB","BN"},
		"BB": [2]string{"BN","NB"},
		"BC": [2]string{"BB","BC"},
		"CC": [2]string{"CN","NC"},
		"CN": [2]string{"CC","CN"},
	},
}

func TestAOC_parseInput(t *testing.T) {
	inputString := `NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C
`
	actualParsedInput := parseInput(inputString)
	assert.Equal(t, input, actualParsedInput)
}

func TestAOC_getSolutionPart1(t *testing.T) {
	expectedSolution := 1588
	actualSolution := getSolutionPart1(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	expectedSolution := 2188189693529
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
