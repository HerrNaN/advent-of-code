package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"sort"
	"strings"
)

var errorPoints = map[rune]int {
		')': 3,
		']': 57,
		'}': 1197,
		'>': 25137,
}

var closingPoints = map[rune]int {
		')': 1,
		']': 2,
		'}': 3,
		'>': 4,
}

func getSolutionPart1(lines []string) int {
	illegals := make(map[rune]int)
	for _, l := range lines {
		r, _ := runSyntaxCheck(l)
		if r == 0 {
			continue
		}
		illegals[r]++
	}
	sum := 0
	for c, n := range illegals {
		sum += n * errorPoints[c]
	}
	return sum
}

func runSyntaxCheck(l string) (rune, []rune) {
	var expected []rune
	for _, r := range l {
		if isClosing(r) {
			if len(expected) == 0 || r != expected[0] {
				return r, expected
			}
			expected = expected[1:]
		} else {
			expected = append([]rune{opposite(r)}, expected...)
		}
	}

	return 0, expected
}

func isClosing(r rune) bool {
	return r == ')' || r == ']' || r == '}' || r == '>'
}

func opposite(r rune) rune {
	switch r {
	case '(': return ')'
	case ')': return '('
	case '[': return ']'
	case ']': return '['
	case '{': return '}'
	case '}': return '{'
	case '<': return '>'
	case '>': return '<'
	default: panic("unrecognized rune")
	}
}

func getSolutionPart2(lines []string) int {
	var completions []string
	for _, l := range lines {
		erroring, expected := runSyntaxCheck(l)
		if erroring == 0 {
			completions = append(completions, string(expected))
		}
	}

	return winner(completions)
}

func winner(completions []string) int {
	var scores []int
	for _, compl := range completions {
		scores = append(scores, score(compl))
	}
	sort.Ints(scores)
	return scores[len(scores)/2]
}

func score(compl string) int {
	score := 0
	for _, r := range compl {
		score *= 5
		score += closingPoints[r]
	}
	return score
}

func parseInput(input string) []string {
	input = strings.TrimSpace(input)
	return strings.Split(input, "\n")
}

func readInput() []string {
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
