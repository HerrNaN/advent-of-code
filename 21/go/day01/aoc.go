package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"strconv"
	"strings"
)

func getSolutionPart1(input []int) int {
	increases := 0
	for i := 1; i < len(input); i++ {
		if input[i-1] < input[i] {
			increases++
		}
	}
	return increases
}

func getSolutionPart2(input []int) int {
	windows := make([]int, len(input)-2)
	for i := 2; i < len(input); i++ {
		windows[i-2] = input[i] + input[i-1] + input[i-2]
	}
	return getSolutionPart1(windows)
}

func parseInput(input string) ([]int, error) {
	var ints []int

	lines := strings.Split(strings.TrimSpace(input), "\n")

	for _, line := range lines {
		i, err := strconv.Atoi(line)
		if err != nil {
			return nil, err
		}

		ints = append(ints, i)
	}

	return ints, nil
}

func main() {
	inputBytes, err := ioutil.ReadFile("input.txt")
	if err != nil {
		panic("couldn't read input")
	}

	input, err := parseInput(string(inputBytes))
	if err != nil {
		panic("couldn't parse input")
	}

	part := os.Getenv("part")

	if part == "part2" {
		fmt.Println(getSolutionPart2(input))
	} else {
		fmt.Println(getSolutionPart1(input))
	}
}
