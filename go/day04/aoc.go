package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"strconv"
	"strings"
)

type Board [][]int

type Bingo struct {
	Current int
	Numbers []int
	Drawn   map[int]bool
	Boards  []Board
}

func (b *Bingo) DrawNumber() {
	n := b.Numbers[0]
	b.Numbers = b.Numbers[1:]
	b.Current = n
	b.Drawn[n] = true
}

func (b *Bingo) HasWon(boardIdx int) bool {
	for _, row := range b.Boards[boardIdx] {
		won := true
		for _, v := range row {
			if !b.Drawn[v] {
				won = false
			}
		}

		if won {
			return true
		}
	}

	for col := 0; col < 5; col++ {
		won := true
		for row := 0; row < 5; row++ {
			if !b.Drawn[b.Boards[boardIdx][row][col]] {
				won = false
			}
		}

		if won {
			return true
		}
	}

	return false
}

func (b *Bingo) WinnerBoardIndex() int {
	for i := range b.Boards {
		if b.HasWon(i) {
			return i
		}
	}

	return -1
}

func (b *Bingo) BoardScore(boardIdx int) int {
	unmarkedSum := 0
	for _, row := range b.Boards[boardIdx] {
		for _, v := range row {
			if !b.Drawn[v] {
				unmarkedSum += v
			}
		}
	}

	return b.Current * unmarkedSum
}

func getSolutionPart1(b *Bingo) int {
	for {
		b.DrawNumber()

		wbi := b.WinnerBoardIndex()
		if wbi != -1 {
			return b.BoardScore(wbi)
		}
	}
}

func getSolutionPart2(b *Bingo) int {
	for {
		b.DrawNumber()

		for {
			wbi := b.WinnerBoardIndex()
			if wbi == -1 {
				break
			}

			if len(b.Boards) == 1 {
				return b.BoardScore(wbi)
			}

			if wbi == len(b.Boards)-1 {
				b.Boards = b.Boards[:wbi]
			} else {
				b.Boards = append(b.Boards[:wbi], b.Boards[wbi+1:]...)
			}
		}
	}
}

func parseInput(input string) *Bingo {
	input = strings.TrimSpace(input)
	split := strings.Split(input, "\n\n")

	var bingo Bingo

	bingo.Numbers = parseNumbers(split[0])
	bingo.Boards = parseBoards(split[1:])
	bingo.Drawn = make(map[int]bool)

	return &bingo
}

func parseBoards(input []string) []Board {
	var boards []Board

	for _, s := range input {
		board := parseBoard(s)
		boards = append(boards, board)
	}

	return boards
}

func parseBoard(boardString string) Board {
	var board Board

	for _, rowString := range strings.Split(boardString, "\n") {
		row := parseRow(rowString)
		board = append(board, row)
	}

	return board
}

func parseRow(rowString string) []int {
	var row []int

	for _, s := range strings.Split(rowString, " ") {
		if s == "" {
			continue
		}

		v, err := strconv.Atoi(s)
		if err != nil {
			panic(err)
		}

		row = append(row, v)
	}

	return row
}

func parseNumbers(commaSeparated string) []int {
	var numbers []int

	for _, i := range strings.Split(commaSeparated, ",") {
		n, err := strconv.Atoi(i)
		if err != nil {
			panic(err)
		}
		numbers = append(numbers, n)
	}

	return numbers
}

func readInput() *Bingo {
	inputBytes, err := ioutil.ReadFile("input.txt")
	if err != nil {
		panic("couldn't read input")
	}

	return parseInput(string(inputBytes))
}

func main() {
	input := readInput()

	part := os.Getenv("part")

	if part == "part2" {
		fmt.Println(getSolutionPart2(input))
	} else {
		fmt.Println(getSolutionPart1(input))
	}
}
