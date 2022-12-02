package main

import (
	"fmt"
	"io/ioutil"
	"math"
	"os"
	"sort"
	"strconv"
	"strings"
)

func getSolutionPart1(cs []int) int {
	sort.Ints(cs)
	to := cs[len(cs)/2]
	return totalFuelCost(cs, to, false)
}

func getSolutionPart2(cs []int) int {
	avg := average(cs)
	pos1, pos2 := int(avg), int(avg+1)

	return int(math.Min(float64(totalFuelCost(cs, pos1, true)), float64(totalFuelCost(cs, pos2, true))))
}

func average(cs []int) float64 {
	sum := 0
	for _, c := range cs {
		sum += c
	}
	return float64(sum) / float64(len(cs))
}

func totalFuelCost(cs []int, alignPos int, crabEngineering bool) int {
	cost := 0
	for _, c := range cs {
		steps := int(math.Abs(float64(alignPos - c)))
		cost += crabFuel(steps, crabEngineering)
	}
	return cost
}

func crabFuel(n int, crabEngineering bool) int {
	if crabEngineering {
		return int(float64(n*(n+1)) / 2.0)
	}
	return n
}

func parseInput(input string) []int {
	input = strings.TrimSpace(input)
	split := strings.Split(input, ",")

	var cs []int
	for _, s := range split {
		i, _ := strconv.Atoi(s)
		cs = append(cs, i)
	}

	return cs
}

func readInput() []int {
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
