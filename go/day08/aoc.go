package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"sort"
	"strconv"
	"strings"
)

var (
	Referencestrings = []string{
		"abcefg",
		"cf",
		"acdeg",
		"acdfg",
		"bcdf",
		"abdfg",
		"abdefg",
		"acf",
		"abcdefg",
		"abcdfg",
	}

	stringAsInt = map[string]int{
		Referencestrings[0]: 0,
		Referencestrings[1]: 1,
		Referencestrings[2]: 2,
		Referencestrings[3]: 3,
		Referencestrings[4]: 4,
		Referencestrings[5]: 5,
		Referencestrings[6]: 6,
		Referencestrings[7]: 7,
		Referencestrings[8]: 8,
		Referencestrings[9]: 9,
	}
)

var (
	stringLength     = [10]int{6, 2, 5, 5, 4, 5, 6, 3, 7, 6}
	SegmentFrequency = [7]int{8, 6, 8, 7, 4, 9, 7}
)

type Display struct {
	references []string
	digits     []string
}

func getSolutionPart1(ds []Display) int {
	sum := 0
	for _, disp := range ds {
		sum += countstringsWithUniqueNumOfSegments(disp)
	}
	return sum
}

func countstringsWithUniqueNumOfSegments(disp Display) int {
	sum := 0
	for _, digit := range disp.digits {
		switch len(digit) {
		case 2, 3, 4, 7:
			sum++
		}
	}
	return sum
}

func getSolutionPart2(ds []Display) int {
	sum := 0
	for _, disp := range ds {
		sum += outputValue(disp)
	}
	return sum
}

func outputValue(disp Display) int {
	var segmentReference map[rune]rune = getSegmentMap(disp.references)

	outputString := ""
	for _, digit := range disp.digits {
		decodedDigitValue := decode(digit, segmentReference)
		outputString += fmt.Sprintf("%d", decodedDigitValue)
	}

	output, err := strconv.Atoi(outputString)
	if err != nil {
		panic(err)
	}

	return output
}

func decode(digit string, segmentReference map[rune]rune) int {
	var decodedRunes []rune
	for _, r := range digit {
		decodedRunes = append(decodedRunes, segmentReference[rune(r)])
	}
	return stringAsInt[sortString(string(decodedRunes))]
}

func getSegmentMap(reference []string) map[rune]rune {
	referenceLengthMapped := make(map[int][]string)
	for _, s := range reference {
		referenceLengthMapped[len(s)] = append(referenceLengthMapped[len(s)], s)
	}

	decodeMap := make(map[rune]rune)
	encodeMap := make(map[rune]rune)

	segmentFreq := freqMap(reference)

	decodeMap[getOnlyKey(segmentFreq[9])] = 'f'
	encodeMap['f'] = getOnlyKey(segmentFreq[9])

	decodeMap[getOnlyKey(segmentFreq[6])] = 'b'
	encodeMap['b'] = getOnlyKey(segmentFreq[6])

	decodeMap[getOnlyKey(segmentFreq[4])] = 'e'
	encodeMap['e'] = getOnlyKey(segmentFreq[4])

	// c is the segment in digit 7 which isn't segment 7
	for _, b := range referenceLengthMapped[2][0] {
		if rune(b) != encodeMap['f'] {
			decodeMap[rune(b)] = 'c'
			encodeMap['c'] = rune(b)
			delete(segmentFreq[8], encodeMap['c'])
		}
	}

	decodeMap[getOnlyKey(segmentFreq[8])] = 'a'
	encodeMap['a'] = getOnlyKey(segmentFreq[8])

	// Deduce mapping for segment c
	for _, b := range referenceLengthMapped[4][0] {
		switch rune(b) {
		case encodeMap['b'],
			encodeMap['c'],
			encodeMap['f']:
			continue
		default:
			encodeMap['d'] = rune(b)
			decodeMap[rune(b)] = 'd'
			delete(segmentFreq[7], encodeMap['d'])
		}
	}

	decodeMap[getOnlyKey(segmentFreq[7])] = 'g'

	return decodeMap
}

func getOnlyKey(m map[rune]bool) rune {
	if len(m) > 1 {
		panic("more than one key in map")
	}
	for k := range m {
		return k
	}
	panic("no key in empty map")
}

func freqMap(reference []string) map[int]map[rune]bool {
	freqs := make(map[rune]int)
	for _, s := range reference {
		for _, b := range s {
			freqs[rune(b)]++
		}
	}

	freqmap := make(map[int]map[rune]bool)
	for r, f := range freqs {
		if freqmap[f] == nil {
			freqmap[f] = make(map[rune]bool)
		}

		freqmap[f][r] = true
	}

	return freqmap
}

func findDigitWithLength(reference []string, l int) string {
	for _, digit := range reference {
		if len(digit) == l {
			return digit
		}
	}
	panic("couldn't find digit")
}

func parseInput(input string) []Display {
	input = strings.TrimSpace(input)
	var ds []Display
	for _, i := range strings.Split(input, "\n") {
		ds = append(ds, parseDisplay(i))
	}
	return ds
}

func parseDisplay(input string) Display {
	split := strings.Split(input, "|")
	return Display{
		references: parseDigits(split[0]),
		digits:     parseDigits(split[1]),
	}
}

func parseDigits(input string) []string {
	input = strings.TrimSpace(input)
	var ds []string
	for _, s := range strings.Split(input, " ") {
		ds = append(ds, parseDigit(s))
	}
	return ds
}

func parseDigit(input string) string {
	return sortString(strings.TrimSpace(input))
}

func sortString(s string) string {
	rs := []rune(s)
	sort.Slice(rs, func(i, j int) bool { return rs[i] < rs[j] })
	return string(rs)
}

func readInput() []Display {
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
