package main

import (
	"fmt"
	"io/ioutil"
	"math"
	"os"
	"strings"
)

type Rules map[string][2]string
type Formula struct {
	template string
	polymer  map[string]int
	rules    Rules
}

func getSolutionPart1(f Formula) int {
	return f.solve(10)
}

func (f Formula) solve(n int) int {
	last := rune(f.template[len(f.template)-1])
	freqs := count(f.rules.stepN(n, f.polymer), last)
	min, max := min(freqs), max(freqs)
	return max - min
}

func (r Rules) stepN(n int, m map[string]int) map[string]int {
	for i := 0; i < n; i++ {
		m = r.step(m)
	}
	return m
}

func (r Rules) step(m map[string]int) map[string]int {
	newMap := make(map[string]int)
	for p, n := range m {
		newMap[r[p][0]] += n
		newMap[r[p][1]] += n
	}
	return newMap
}

func count(m map[string]int, last rune) map[rune]int {
	counts := make(map[rune]int)
	for p, n := range m {
		counts[rune(p[0])] += n
	}
	counts[last]++
	return counts
}

func max(m map[rune]int) int {
	var n int
	for _, v := range m {
		if v > n {
			n = v
		}
	}
	return n
}

func min(m map[rune]int) int {
	n := math.MaxInt
	for _, v := range m {
		if v < n {
			n = v
		}
	}
	return n
}

func getSolutionPart2(f Formula) int {
	return f.solve(40)
}

func parseInput(input string) Formula {
	input = strings.TrimSpace(input)
	splits := strings.Split(input, "\n\n")
	return Formula{
		template: splits[0],
		polymer:  parsePolymer(splits[0]),
		rules:    parseRules(splits[1]),
	}
}

func parsePolymer(input string) map[string]int {
	m := make(map[string]int)
	for i := 0; i < len(input)-1; i++ {
		m[input[i:i+2]]++
	}
	return m
}

func parseRules(input string) Rules {
	input = strings.TrimSpace(input)
	rules := make(Rules)
	for _, line := range strings.Split(input, "\n") {
		splits := strings.Split(line, " -> ")
		rules[splits[0]] = [2]string{
			string(rune(splits[0][0])) + string(rune(splits[1][0])),
			string(rune(splits[1][0])) + string(rune(splits[0][1])),
		}
	}
	return rules
}

func readInput() Formula {
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
