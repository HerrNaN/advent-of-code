package main

import (
	"fmt"
	"testing"

	"github.com/stretchr/testify/assert"
)

var inputString = `[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
[[[5,[2,8]],4],[5,[[9,9],0]]]
[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
[[[[5,4],[7,7]],8],[[8,3],8]]
[[9,3],[[9,9],[6,[4,9]]]]
[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
`

func TestAOC_parseInput(t *testing.T) {
	tests := []struct {
		name           string
		input          string
		expectedNumber SnailFishNumber
	}{
		{
			name:  "Parse [9,1]",
			input: "[9,1]",
			expectedNumber: SnailFishNumber{
				left: &SnailFishNumber{
					value: 9,
				},
				right: &SnailFishNumber{
					value: 1,
				},
			},
		},
		{
			name:  "Parse [[9,1],2]",
			input: "[[9,1],2]",
			expectedNumber: SnailFishNumber{
				left: &SnailFishNumber{
					left: &SnailFishNumber{
						value: 9,
					},
					right: &SnailFishNumber{
						value: 1,
					},
				},
				right: &SnailFishNumber{
					value: 2,
				},
			},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			sn, idx := parseSnailFishNumberAt(tt.input, 0)
			assert.Equal(t, len(tt.input), idx)
			assert.Equal(t, tt.expectedNumber, sn)
		})
	}
}

func TestAOC_SN_String(t *testing.T) {
	inputs := []string{
		"[1,2]",
		"[[1,2],3]",
		"[9,[8,7]]",
		"[[1,9],[8,5]]",
		"[[[[1,2],[3,4]],[[5,6],[7,8]]],9]",
		"[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]",
		"[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]",
	}

	for _, inp := range inputs {
		t.Run(inp, func(t *testing.T) {
			sn, _ := parseSnailFishNumberAt(inp, 0)
			assert.Equal(t, inp, sn.String())
		})
	}
}

func TestAOC_SF_Magnitude(t *testing.T) {
	tests := []struct {
		name              string
		sn                SnailFishNumber
		expectedMagnitude int
	}{
		{
			name: "[9,1] => 29",
			sn: SnailFishNumber{
				left: &SnailFishNumber{
					value: 9,
				},
				right: &SnailFishNumber{
					value: 1,
				},
			},
			expectedMagnitude: 29,
		},
		{
			name: "[1,9] => 21",
			sn: SnailFishNumber{
				left: &SnailFishNumber{
					value: 1,
				},
				right: &SnailFishNumber{
					value: 9,
				},
			},
			expectedMagnitude: 21,
		},
		{
			name: "[[9,1],[1,9]] => 129",
			sn: SnailFishNumber{
				left: &SnailFishNumber{
					left: &SnailFishNumber{
						value: 9,
					},
					right: &SnailFishNumber{
						value: 1,
					},
				},
				right: &SnailFishNumber{
					left: &SnailFishNumber{
						value: 1,
					},
					right: &SnailFishNumber{
						value: 9,
					},
				},
			},
			expectedMagnitude: 129,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.expectedMagnitude, tt.sn.Magnitude())
		})
	}
}

func TestAOC_SN_explodeAtN(t *testing.T) {

	tests := []struct {
		before, after string
		n int
	}{
		{
			before:    "[[[[[9,8],1],2],3],4]",
			after:     "[[[[0,9],2],3],4]",
			n: 4,
		},
		{
			before:    "[7,[6,[5,[4,[3,2]]]]]",
			after:     "[7,[6,[5,[7,0]]]]",
			n: 4,
		},
		{
			before:    "[[6,[5,[4,[3,2]]]],1]",
			after:     "[[6,[5,[7,0]]],3]",
			n: 4,
		},
		{
			before:    "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]",
			after:     "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
			n: 4,
		},
		{
			before:    "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
			after:     "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
			n: 4,
		},
	}
	for _, tt := range tests {
		t.Run(fmt.Sprintf("%s -> %s", tt.before, tt.after), func(t *testing.T) {
			sn, _ := parseSnailFishNumberAt(tt.before, 0)
			actual, _, _, _ := sn.explodeAtN(tt.n)
			assert.Equal(t, tt.after, actual.String())
		})
	}
}

func TestAOC_SF_splitAtNOrGreater(t *testing.T) {
	tests := []struct {
		before, after string
		n             int
	}{
		{
			before: "[[[[0,7],4],[9,[0,9]]],[1,1]]",
			after:  "[[[[0,7],4],[[4,5],[0,9]]],[1,1]]",
			n:      9,
		},
		{
			before: "[[[[0,7],4],[[4,5],[0,9]]],[1,1]]",
			after:  "[[[[0,7],4],[[4,5],[0,[4,5]]]],[1,1]]",
			n:      9,
		},
		{
			before: "[1,1]",
			after:  "[1,1]",
			n:      10,
		},
	}
	for _, tt := range tests {
		t.Run(fmt.Sprintf("%s -> %s", tt.before, tt.after), func(t *testing.T) {
			sn, _ := parseSnailFishNumberAt(tt.before, 0)
			actual, _ := sn.splitAtNOrGreater(tt.n)
			assert.Equal(t, tt.after, actual.String())
		})
	}
}

func TestAOC_Sum(t *testing.T) {
	tests := []struct {
		list        string
		expectedSum string
	}{
		{
			list: "[1,1]\n[2,2]\n[3,3]\n[4,4]",
			expectedSum: "[[[[1,1],[2,2]],[3,3]],[4,4]]",
		},
		{
			list: "[1,1]\n[2,2]\n[3,3]\n[4,4]\n[5,5]",
			expectedSum: "[[[[3,0],[5,3]],[4,4]],[5,5]]",
		},
		{
			list: "[1,1]\n[2,2]\n[3,3]\n[4,4]\n[5,5]\n[6,6]",
			expectedSum: "[[[[5,0],[7,4]],[5,5]],[6,6]]",
		},
		{
			list: `[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
[7,[5,[[3,8],[1,4]]]]
[[2,[2,2]],[8,[8,1]]]
[2,9]
[1,[[[9,3],9],[[9,0],[0,7]]]]
[[[5,[7,4]],7],1]
[[[[4,2],2],6],[8,7]]`,
			expectedSum: "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]",
		},
	}

	for _, tt := range tests {
		t.Run("", func(t *testing.T) {
			actual := Sum(parseInput(tt.list))
			assert.Equal(t, tt.expectedSum, actual.String())
		})
	}
}

func TestAOC_SN_Reduce(t *testing.T) {
	tests := []struct{
		before, after string
	}{
		{
			before: "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]",
			after:  "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
		},
		{
			before: "[[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]],[2,9]]",
			after:  "[[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]",
		},
		{
			before: "[[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]",
			after:  "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]",
		},
		{
			before: "[[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]],[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]]",
			after:  "[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]",
		},
		{
			before: "[[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]],[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]]",
			after:  "[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]",
		},
		{
			before: "[[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]],[7,[5,[[3,8],[1,4]]]]]",
			after:  "[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]",
		},
	}

	for _, tt := range tests {
		t.Run(fmt.Sprintf("%s -> %s", tt.before, tt.after), func(t *testing.T) {
			expected, _ := parseSnailFishNumberAt(tt.after, 0)
			sn, _ := parseSnailFishNumberAt(tt.before, 0)
			actual := sn.Reduce(4, 10)
			assert.Equal(t, expected.String(), actual.String())
		})
	}
}

func TestAOC_getSolutionPart1(t *testing.T) {
	assert.Equal(t, 4140, getSolutionPart1(parseInput(inputString)))
}

func TestAOC_getSolutionPart2(t *testing.T) {
	assert.Equal(t, 3993, getSolutionPart2(parseInput(inputString)))
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
