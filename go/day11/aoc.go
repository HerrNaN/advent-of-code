package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"strings"
)

var neighbourDeltas = []Point{
	{row: 0, col: 1},
	{row: -1, col: 1},
	{row: -1, col: 0},
	{row: -1, col: -1},
	{row: 0, col: -1},
	{row: 1, col: -1},
	{row: 1, col: 0},
	{row: 1, col: 1},
}

type Point struct{ row, col int }

func (p Point) add(other Point) Point {
	p.row += other.row
	p.col += other.col
	return p
}

type Cavern struct {
	octopi [10][10]int
}

func (c Cavern) pointOutOfBounds(p Point) bool {
	return p.row < 0 || p.col < 0 || p.row >= len(c.octopi) || p.col >= len(c.octopi[0])
}

func getSolutionPart1(c *Cavern) int {
	return c.totalFlashesDuring(100)
}

func (c *Cavern) totalFlashesDuring(nSteps int) int {
	flashes := 0
	for i := 0; i < nSteps; i++ {
		flashes += c.step()
		// fmt.Printf("After step %d:\n", i+1)
		// c.print()
	}
	return flashes
}

func (c *Cavern) step() int {
	flashes := 0
	c.inc(1)
	for {
		flashed := c.flashAll()
		flashes += flashed
		if flashed == 0 {
			break
		}
	}
	return flashes
}

func (c *Cavern) flashAll() int {
	flashed := 0
	for row := range c.octopi {
		for col := range c.octopi {
			if c.octopi[row][col] > 9 {
				c.flash(Point{row,col})
				flashed++
			}
		}
	}
	return flashed
}

func (c *Cavern) flash(p Point) {
	c.octopi[p.row][p.col] = 0
	ns := c.neighbourPoints(p)
	for i := range ns {
		if c.octopi[ns[i].row][ns[i].col] != 0 {
			c.octopi[ns[i].row][ns[i].col]++
		}
	}
}

func (c *Cavern) inc(n int) {
	for row := range c.octopi {
		for col := range c.octopi {
			c.octopi[row][col] += n
		}
	}
}

func getSolutionPart2(c *Cavern) int {
	counter := 0
	for {
		counter++
		if c.step() == 100 {
			return counter
		}
	}
}

func (c Cavern) neighbourPoints(p Point) []Point {
	neighbours := make([]Point, 0, 8)

	for i := range neighbourDeltas {
		if !c.pointOutOfBounds(p.add(neighbourDeltas[i])) {
			neighbours = append(neighbours, p.add(neighbourDeltas[i]))
		}
	}

	return neighbours
}

func (c Cavern) print() {
	for _, row := range c.octopi {
		fmt.Println(row)
	}
}

func parseInput(input string) *Cavern {
	input = strings.TrimSpace(input)
	octopi := [10][10]int{}
	for r, line := range strings.Split(input, "\n") {
		octopi[r] = parseLine(line)
	}
	return &Cavern{octopi: octopi}
}

func parseLine(line string) [10]int {
	row := [10]int{}
	for c, r := range line {
		row[c] = int(r-'0')
	}
	return row
}

func readInput() *Cavern {
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
