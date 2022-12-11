package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"strings"
)

const (
	CaveNameStart = "start"
	CaveNameEnd   = "end"
)

type Cave string
type CaveMap map[Cave][]Cave

func getSolutionPart1(m CaveMap) int {
	return m.numUniquePathsFrom(CaveNameStart, make([]Cave,0,20), true)
}

func (m CaveMap) numUniquePathsFrom(cave Cave, visited []Cave, visitedSmallCaveTwice bool) int {
	if cave == CaveNameEnd {
		return 1
	}

	if cave.isSmall() && contains(visited, cave) {
		if visitedSmallCaveTwice || cave == CaveNameStart {
			return 0
		}

		visitedSmallCaveTwice = true
	}

	paths := 0
	for i := range m[cave] {
		paths += m.numUniquePathsFrom(m[cave][i], append(visited, cave), visitedSmallCaveTwice)
	}

	return paths
}

func (c Cave) isSmall() bool {
	return c[0] >= 97
}

func contains(ss []Cave, s Cave) bool {
	for i := range ss {
		if ss[i] == s {
			return true
		}
	}
	return false
}

func getSolutionPart2(m CaveMap) int {
	return m.numUniquePathsFrom(CaveNameStart, make([]Cave,0,20), false)
}

func parseInput(input string) CaveMap {
	input = strings.TrimSpace(input)
	m := make(map[Cave][]Cave)
	for _, line := range strings.Split(input, "\n") {
		splits := strings.Split(line, "-")
		m[Cave(splits[0])] = append(m[Cave(splits[0])], Cave(splits[1]))
		m[Cave(splits[1])] = append(m[Cave(splits[1])], Cave(splits[0]))
	}
	return m
}

func readInput() CaveMap {
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
