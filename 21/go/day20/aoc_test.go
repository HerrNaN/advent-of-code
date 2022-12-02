package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var inputString = `..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

#..#.
#....
##..#
..#..
..###
`

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
		GetSolutionPart1(input)
	}
}

func Benchmark_Part2(b *testing.B) {
	b.ReportAllocs()

	input := readInput()
	for i := 0; i < b.N; i++ {
		GetSolutionPart2(input)
	}
}

func TestGetSolutionPart1(t *testing.T) {
	assert.Equal(t, 35, GetSolutionPart1(ParseInput(inputString)))
}

func TestGetSolutionPart2(t *testing.T) {
	assert.Equal(t, 3351, GetSolutionPart2(ParseInput(inputString)))
}

func TestParseInput(t *testing.T) {
	tests := []struct {
		name  string
		input string
		want  Input
	}{
		{
			name:  "",
			input: ".#.\n\n#.\n.#\n",
			want:  Input{
				Alg: [512]bool{false, true, false},
				Img: map[Point]bool{
					{0,0}: true,
					{1,0}: false,
					{0,1}: false,
					{1,1}: true,
				},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.want, ParseInput(tt.input))
		})
	}
}

func TestParseImage(t *testing.T) {
	tests := []struct {
		name  string
		input string
		want  Image
	}{
		{
			name:  "",
			input: "#..#.\n#....\n##..#\n..#..\n..###",
			want:  map[Point]bool{
				{0,0}: true,
				{1,0}: false,
				{2,0}: false,
				{3,0}: true,
				{4,0}: false,

				{0,1}: true,
				{1,1}: false,
				{2,1}: false,
				{3,1}: false,
				{4,1}: false,

				{0,2}: true,
				{1,2}: true,
				{2,2}: false,
				{3,2}: false,
				{4,2}: true,

				{0,3}: false,
				{1,3}: false,
				{2,3}: true,
				{3,3}: false,
				{4,3}: false,

				{0,4}: false,
				{1,4}: false,
				{2,4}: true,
				{3,4}: true,
				{4,4}: true,
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.want, ParseImage(tt.input))
		})
	}
}

func TestParseAlgorithm(t *testing.T) {
	tests := []struct {
		name  string
		input string
		want  Algorithm
	}{
		{
			name:  "",
			input: ".#.#..#",
			want:  [512]bool{false, true, false, true, false, false, true},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			assert.Equal(t, tt.want, ParseAlgorithm(tt.input))
		})
	}
}
