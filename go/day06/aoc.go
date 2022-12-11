package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"strconv"
	"strings"
)

func getSolutionPart1(is []int) int {
	return solve(is, 80)
}

func getSolutionPart2(is []int) int {
	return solve(is, 256)
}

func solve(is []int, steps int) int {
	for i := 0; i < steps; i++ {
		step(is)
	}

	return countFish(is)
}

func countFish(is []int) int {
	sum := 0
	for _, i := range is {
		sum += i
	}
	return sum
}

func step(is []int) {
	carry := Shift(is)

	is[6] += carry
	is[8] = carry
}

func Shift(is []int) int {
	tmp := 0
	for i := len(is)-1; i >= 0; i-- {
		a := is[i]
		is[i] = tmp
		tmp = a
	}
	return tmp
}

func parseInput(input string) []int {
	input = strings.TrimSpace(input)
	split := strings.Split(input, ",")

	is := make([]int,9)
	for _, s := range split {
		i, _ := strconv.Atoi(s)
		is[i]++
	}

	return is
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
