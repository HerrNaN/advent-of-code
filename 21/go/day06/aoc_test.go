package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var input = []int{0, 1, 1, 2, 1, 0, 0, 0, 0}

func TestAOC_parseInput(t *testing.T) {
	inputString := `3,4,3,1,2
`

	actualParsedInput := parseInput(inputString)
	assert.Equal(t, input, actualParsedInput)
}

func TestAOC_getSolutionPart1(t *testing.T) {
	expectedSolution := 5934
	is := make([]int,9)
	copy(is, input)

	actualSolution := getSolutionPart1(is)
	assert.Equal(t, expectedSolution, actualSolution)
}

func TestAOC_getSolutionPart2(t *testing.T) {
	expectedSolution := 26984457539
	is := make([]int,9)
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

func TestShift(t *testing.T) {
	type args struct {
		is []int
	}
	tests := []struct {
		name string
		args args
		wantCarry int
		wantIs []int
	}{
		{
			name: "",
			args: args{
				is: []int{1,2,3},
			},
			wantCarry: 1,
			wantIs: []int{2,3,0},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			carry := Shift(tt.args.is)
			assert.Equal(t, tt.wantCarry, carry)
			assert.Equal(t, tt.wantIs, tt.args.is)
		})
	}
}
