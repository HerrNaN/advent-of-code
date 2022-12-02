package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"sort"
	"strings"
)

type Point struct{ row, col int }

func getSolutionPart1(grid [][]int) int {
	sum := 0
	for _, p := range getLowPoints(grid) {
		sum += grid[p.row][p.col] + 1
	}
	return sum
}

func lowPoint(row, col int, grid [][]int) bool {
	ns := neighbourValues(row, col, grid)
	for _, n := range ns {
		if n <= grid[row][col] {
			return false
		}
	}
	return true
}

func getLowPoints(grid [][]int) []Point {
	var lowPoints []Point
	for row := range grid {
		for col := range grid[row] {
			if lowPoint(row, col, grid) {
				lowPoints = append(lowPoints, Point{row: row, col: col})
			}
		}
	}
	return lowPoints
}

func highPoint(row, col int, grid [][]int) bool {
	ns := neighbourValues(row, col, grid)
	for _, n := range ns {
		if n > grid[row][col] {
			return false
		}
	}
	return true
}

func getSolutionPart2(grid [][]int) int {
	lowPoints := getLowPoints(grid)
	bm := basinMapFromGrid(grid)

	var basinSizes []int
	for _, p := range lowPoints {
		basinSizes = append(basinSizes, countBasin(p.row, p.col, bm))
	}

	sort.Sort(sort.Reverse(sort.IntSlice(basinSizes)))

	return basinSizes[0] * basinSizes[1] * basinSizes[2]
}

func neighbourValues(row, col int, grid [][]int) []int {
	neighbours := make([]int,0,4)

	if row > 0 {
		neighbours = append(neighbours, grid[row-1][col])
	}
	if col > 0 {
		neighbours = append(neighbours, grid[row][col-1])
	}
	if len(grid[row])-1 >= col+1 {
		neighbours = append(neighbours, grid[row][col+1])
	}
	if len(grid)-1 >= row+1 {
		neighbours = append(neighbours, grid[row+1][col])
	}

	return neighbours
}

func neighbourPoints(row, col int, grid [][]int) []Point {
	var neighbours []Point

	if row > 0 {
		neighbours = append(neighbours, Point{row: row - 1, col: col})
	}
	if col > 0 {
		neighbours = append(neighbours, Point{row: row, col: col - 1})
	}
	if len(grid[row])-1 >= col+1 {
		neighbours = append(neighbours, Point{row: row, col: col + 1})
	}
	if len(grid)-1 >= row+1 {
		neighbours = append(neighbours, Point{row: row + 1, col: col})
	}

	return neighbours
}

func countBasin(row, col int, basinMap [][]int) int {
	visited := make(map[Point]bool)
	toVisit := []Point{{row: row, col: col}}
	size := 0
	for {
		if len(toVisit) == 0 {
			break
		}
		if visited[toVisit[0]] {
			toVisit = toVisit[1:]
			continue
		}

		visited[Point{row: toVisit[0].row, col: toVisit[0].col}] = true

		if basinMap[toVisit[0].row][toVisit[0].col] != 0 {
			continue
		}

		size++

		ps := neighbourPoints(toVisit[0].row, toVisit[0].col, basinMap)
		toVisit = append(toVisit, ps...)

	}

	return size
}

func basinMapFromGrid(grid [][]int) [][]int {
	basinMap := make([][]int, len(grid))
	copy(basinMap, grid)
	for row := range grid {
		for col := range grid[row] {
			if grid[row][col] != 9 {
				grid[row][col] = 0
			}
		}
	}
	return basinMap
}

func printGrid(grid [][]int) {
	for _, row := range grid {
		fmt.Println(row)
	}
}

func parseInput(input string) [][]int {
	input = strings.TrimSpace(input)
	var grid [][]int
	for _, line := range strings.Split(input, "\n") {
		grid = append(grid, parseLine(line))
	}
	return grid
}

func parseLine(line string) []int {
	var row []int
	for _, r := range line {
		row = append(row, int(r-'0'))
	}
	return row
}

func readInput() [][]int {
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
