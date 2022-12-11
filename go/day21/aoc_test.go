package main

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var inputString = `Player 1 starting position: 4
Player 2 starting position: 8
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

func TestDice_RollNTimes(t *testing.T) {
	type fields struct {
		Sides       int
		LastRoll    int
		TimesRolled int
	}
	type args struct {
		n int
	}
	tests := []struct {
		name     string
		fields   fields
		args     args
		wantDice Dice
		wantSum  int
	}{
		{
			name: "",
			fields: fields{
				Sides:       6,
				LastRoll:    4,
				TimesRolled: 12,
			},
			args: args{
				n: 3,
			},
			wantDice: Dice{
				Sides:       6,
				LastRoll:    1,
				TimesRolled: 15,
			},
			wantSum: 12,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			d := Dice{
				Sides:       tt.fields.Sides,
				LastRoll:    tt.fields.LastRoll,
				TimesRolled: tt.fields.TimesRolled,
			}
			gotDice, gotSum := d.RollNTimes(tt.args.n)
			assert.Equal(t, tt.wantDice, gotDice)
			assert.Equal(t, tt.wantSum, gotSum)
		})
	}
}

func TestDice_Roll(t *testing.T) {
	type fields struct {
		Sides       int
		LastRoll    int
		TimesRolled int
	}
	tests := []struct {
		name   string
		fields fields
		want   Dice
	}{
		{
			name: "",
			fields: fields{
				Sides:       6,
				LastRoll:    1,
				TimesRolled: 0,
			},
			want: Dice{
				Sides:       6,
				LastRoll:    2,
				TimesRolled: 1,
			},
		},
		{
			name: "",
			fields: fields{
				Sides:       6,
				LastRoll:    6,
				TimesRolled: 0,
			},
			want: Dice{
				Sides:       6,
				LastRoll:    1,
				TimesRolled: 1,
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			d := Dice{
				Sides:    tt.fields.Sides,
				LastRoll: tt.fields.LastRoll,
			}
			assert.Equal(t, tt.want, d.Roll())
		})
	}
}

func TestGetSolutionPart1(t *testing.T) {
	assert.Equal(t, 739785, GetSolutionPart1(ParseInput(inputString)))
}

func TestGetSolutionPart2(t *testing.T) {
}

func TestParseInput(t *testing.T) {
	tests := []struct {
		name  string
		input string
		want  []Player
	}{
		{
			name: "",
			input: `Player 1 starting position: 4
Player 2 starting position: 8
`,
			want: []Player{
				{
					Pos:   4,
					Score: 0,
				},
				{
					Pos:   8,
					Score: 0,
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
