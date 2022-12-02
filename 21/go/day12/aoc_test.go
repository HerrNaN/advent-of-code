package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var input = CaveMap(map[Cave][]Cave{
	"fs": {"end", "he", "DX", "pj"},
	"end": {"fs", "zg"},
	"he": {"DX", "fs", "pj", "RW", "WI", "zg"},
	"DX": {"he", "start", "pj", "fs"},
	"start": {"DX", "pj", "RW"},
	"pj": {"DX", "zg", "he", "RW", "start","fs"},
	"zg": {"end", "sl", "pj", "RW", "he"},
	"sl": {"zg"},
	"RW": {"he", "pj", "zg","start"},
	"WI": {"he"},
})

func TestAOC_parseInput(t *testing.T) {
	inputString := `fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW
`
	actualParsedInput := parseInput(inputString)
	assert.Equal(t, input, actualParsedInput)
}

func TestAOC_getSolutionPart1(t *testing.T) {
	expectedSolution := 226
	actualSolution := getSolutionPart1(input)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	expectedSolution := 3509
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
