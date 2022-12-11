package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"strconv"
	"strings"
)


func getSolutionPart1(input []string) int {
	bits := len(input[0])
	var gamma, epsilon string
	for i := 0; i < bits; i++ {
		zeroes, ones := splitBitSelect(input, i)

		if len(ones) > len(zeroes) {
			gamma += "1"
			epsilon += "0"
		} else {
			gamma += "0"
			epsilon += "1"
		}
	}

	g, _ := binStringToDec(gamma, 32)
	e, _ := binStringToDec(epsilon, 32)

	return g * e
}

func getSolutionPart2(input []string) int {
	generator := filterAt(input, 0, '0')
	scrubber := filterAt(input, 0, '1')

	g, _ := binStringToDec(generator, 32)
	s, _ := binStringToDec(scrubber, 32)

	return g * s

}

func filterAt(input []string, pos int, r rune) string {
	if len(input) == 1 {
		return input[0]
	}

	zeroes, ones := splitBitSelect(input, pos)

	switch r {
	case '1':
		if len(ones) >= len(zeroes) {
			return filterAt(ones, pos+1, r)
		}
		return filterAt(zeroes, pos+1, r)
	case '0':
		if len(zeroes) <= len(ones) {
			return filterAt(zeroes, pos+1, r)
		}
		return filterAt(ones, pos+1, r)
	default: panic("wrong r")
	}
}

func splitBitSelect(ss []string, pos int) ([]string, []string) {
	var zeroes, ones []string
	for _, s := range ss {
		switch s[pos] {
		case '0': zeroes = append(zeroes, s)
		case '1': ones = append(ones, s)
		default: panic("not a bit")
		}
	}
	return zeroes, ones
}

func binStringToDec(s string, bits int) (int, error) {
	i, err := strconv.ParseInt(s, 2, bits)
	if err != nil {
		return 0, err
	}
	return int(i), nil
}

func parseInput(input string) ([]string, error) {
	lines := strings.Split(strings.TrimSpace(input), "\n")

	return lines, nil
}

func readInput() ([]string, error) {
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
