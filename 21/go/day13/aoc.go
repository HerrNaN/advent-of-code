package main

import (
	"bytes"
	"fmt"
	"io/ioutil"
	"os"
	"strconv"
	"strings"
)

const (
	FoldTypeVertical   = "x"
	FoldTypeHorizontal = "y"
)

type Fold struct {
	foldType string
	place    int
}
type Point struct{ x, y int }
type Paper map[Point]bool
type Manual struct {
	paper Paper
	folds []Fold
}

func getSolutionPart1(m Manual) int {
	m.paper.doFold(m.folds[0])
	return len(m.paper)
}

func (m *Manual) foldAll() {
	for _, fold := range m.folds {
		m.paper.doFold(fold)
	}
}

func (paper Paper) doFold(f Fold) {
	for p := range paper {
		if np, ok := foldedPoint(f, p); ok {
			delete(paper, p)
			paper[np] = true
		}
	}
}

func foldedPoint(f Fold, p Point) (Point, bool) {
	if f.foldType == FoldTypeHorizontal {
		return Point{p.x, p.y - 2*(p.y-f.place)}, p.y > f.place
	}
	return Point{p.x - 2*(p.x-f.place), p.y}, p.x > f.place
}

func getSolutionPart2(m Manual) string {
	m.foldAll()
	return m.code()
}

func (m *Manual) code() string {
	maxX, maxY := m.paper.limits()

	bs := bytes.Buffer{}
	for y := 0; y <= maxY; y++ {
		for x := 0; x <= maxX; x++ {
			if m.paper[Point{x, y}] {
				bs.WriteByte(byte('#'))
			} else {
				bs.WriteByte(byte(' '))
			}
		}
		bs.WriteByte(byte('\n'))
	}
	return bs.String()
}

func (paper Paper) limits() (int, int) {
	var maxX, maxY int
	for p := range paper {
		if p.x > maxX {
			maxX = p.x
		}
		if p.y > maxY {
			maxY = p.y
		}
	}
	return maxX, maxY
}

func parseInput(input string) Manual {
	input = strings.TrimSpace(input)
	splits := strings.Split(input, "\n\n")
	return Manual{
		paper: parsePaper(splits[0]),
		folds: parseFolds(splits[1]),
	}
}

func parsePaper(input string) Paper {
	input = strings.TrimSpace(input)
	m := make(map[Point]bool)
	for _, line := range strings.Split(input, "\n") {
		splits := strings.Split(line, ",")
		x, _ := strconv.Atoi(splits[0])
		y, _ := strconv.Atoi(splits[1])
		m[Point{x, y}] = true
	}
	return m
}

func parseFolds(input string) []Fold {
	input = strings.TrimSpace(input)
	var folds []Fold
	for _, line := range strings.Split(input, "\n") {
		folds = append(folds, parseFold(line))
	}
	return folds
}

func parseFold(input string) Fold {
	input = strings.TrimSpace(input)
	splits := strings.Split(strings.Split(input, " ")[2], "=")
	place, _ := strconv.Atoi(splits[1])
	return Fold{
		foldType: splits[0],
		place:    place,
	}
}

func readInput() Manual {
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
