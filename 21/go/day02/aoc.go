package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"strconv"
	"strings"
)


type command struct {
	name string
	value int
}

func getSolutionPart1(input []command) int {
	var h, d int
	for _, c := range input {
		switch c.name {
		case "forward": h += c.value
		case "up": d -= c.value
		case "down": d += c.value
		default: panic("unknown direction")
		}
	}

	return h*d
}

func getSolutionPart2(input []command) int {
	var h, d, a int
	for _, c := range input {
		switch c.name {
		case "forward":
			h += c.value
			d += a * c.value
		case "up": a -= c.value
		case "down": a += c.value
		default: panic("unknown direction")
		}
	}

	return h*d

}

func parseInput(input string) ([]command, error) {
	var parsed []command

	lines := strings.Split(strings.TrimSpace(input), "\n")

	for _, line := range lines {
		parts := strings.Split(line, " ")

		name := parts[0]
		value, _ := strconv.Atoi(parts[1])

		parsed = append(parsed, command{name: name, value: value})
	}

	return parsed, nil
}

func readInput() ([]command, error) {
	inputBytes, err := ioutil.ReadFile("input.txt")
	if err != nil {
		panic("couldn't read input")
	}

	return parseInput(string(inputBytes))
}

func main() {
	input, err := readInput()
	if err != nil {
		panic(err)
	}

	part := os.Getenv("part")

	if part == "part2" {
		fmt.Println(getSolutionPart2(input))
	} else {
		fmt.Println(getSolutionPart1(input))
	}
}
