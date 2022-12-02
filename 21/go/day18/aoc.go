package main

import (
	"fmt"
	"io/ioutil"
	"math"
	"os"
	"strconv"
	"strings"
)

type SnailFishNumber struct {
	left, right *SnailFishNumber
	value       int
}

type Side string

const (
	SideLeft  Side = "left"
	SideRight Side = "right"
)

func (sn SnailFishNumber) String() string {
	if sn.IsValue() {
		return fmt.Sprintf("%d", sn.value)
	}

	return fmt.Sprintf("[%s,%s]", sn.left.String(), sn.right.String())
}

func (sn SnailFishNumber) IsValue() bool {
	return sn.left == nil && sn.right == nil
}

func (sn SnailFishNumber) IsValuePair() bool {
	return sn.left != nil && sn.left.IsValue() && sn.right != nil && sn.right.IsValue()
}

func (sn SnailFishNumber) Magnitude() int {
	if sn.IsValue() {
		return sn.value
	}

	return 3*sn.left.Magnitude() + 2*sn.right.Magnitude()
}

func (sn SnailFishNumber) AddToSideMost(side Side, n int) SnailFishNumber {
	if sn.IsValue() {
		sn.value += n
	} else if side == SideLeft {
		l := sn.left.AddToSideMost(side, n)
		sn.left = &l
	} else {
		r := sn.right.AddToSideMost(side, n)
		sn.right = &r
	}
	return sn
}

func Sum(sns []SnailFishNumber) SnailFishNumber {
	snSum := sns[0]
	for i := 1; i < len(sns); i++ {
		snSum = snSum.Add(sns[i])
	}
	return snSum
}

func (sn SnailFishNumber) Add(other SnailFishNumber) SnailFishNumber {
	return sn.add(other).Reduce(4, 10)
}

func (sn SnailFishNumber) add(other SnailFishNumber) SnailFishNumber {
	return SnailFishNumber{
		left:  &sn,
		right: &other,
	}
}

func (sn SnailFishNumber) Reduce(explodeAt, splitAt int) SnailFishNumber {
	didReduce := true

	for {
		sn, didReduce, _, _ = sn.explodeAtN(explodeAt)
		if didReduce {
			continue
		}

		sn, didReduce = sn.splitAtNOrGreater(splitAt)
		if didReduce {
			continue
		}

		return sn
	}
}

func (sn SnailFishNumber) explodeAtN(n int) (SnailFishNumber, bool, *int, *int) {
	if sn.IsValue() && n > 0 {
		return sn, false, nil, nil
	}

	if n <= 0 && sn.IsValuePair() {
		return SnailFishNumber{value: 0}, true, &sn.left.value, &sn.right.value
	}

	if sn.left != nil {
		if l, exploded, leftAdd, rightAdd := sn.left.explodeAtN(n - 1); exploded {
			if rightAdd != nil {
				r := sn.right.AddToSideMost(SideLeft, *rightAdd)
				sn.right = &r
			}
			sn.left = &l
			return sn, true, leftAdd, nil
		}
	}

	if sn.right != nil {
		if r, exploded, leftAdd, rightAdd := sn.right.explodeAtN(n - 1); exploded {
			if leftAdd != nil {
				l := sn.left.AddToSideMost(SideRight, *leftAdd)
				sn.left = &l
			}

			sn.right = &r
			return sn, true, nil, rightAdd
		}
	}

	return sn, false, nil, nil
}

func (sn SnailFishNumber) splitAtNOrGreater(n int) (SnailFishNumber, bool) {
	if sn.IsValue() && sn.value >= n {
		return SnailFishNumber{
			left: &SnailFishNumber{
				value: sn.value / 2,
			},
			right: &SnailFishNumber{
				value: int(math.Ceil(float64(sn.value) / 2.0)),
			},
		}, true
	}

	if sn.left != nil {
		if l, didSplit := sn.left.splitAtNOrGreater(n); didSplit {
			sn.left = &l
			return sn, true
		}
	}

	if sn.right != nil {
		if r, didSplit := sn.right.splitAtNOrGreater(n); didSplit {
			sn.right = &r
			return sn, true
		}
	}

	return sn, false
}

func parseSnailFishNumberAt(input string, idx int) (SnailFishNumber, int) {
	if val, err := strconv.Atoi(string(input[idx])); err == nil {
		return SnailFishNumber{value: val}, idx + 1
	}

	left, idx := parseSnailFishNumberAt(input, idx+1)
	right, idx := parseSnailFishNumberAt(input, idx+1)

	return SnailFishNumber{left: &left, right: &right}, idx + 1
}

func getSolutionPart1(sns []SnailFishNumber) int {
	return Sum(sns).Magnitude()
}

func getSolutionPart2(sns []SnailFishNumber) int {
	max := 0
	for _, s1 := range sns {
		for _, s2 := range sns {
			if s1.String() == s2.String() {
				continue
			}

			m := s1.Add(s2).Magnitude()
			if m > max {
				max = m
			}
		}
	}
	return max
}

func parseInput(input string) []SnailFishNumber {
	input = strings.TrimSpace(input)
	var sns []SnailFishNumber
	for _, line := range strings.Split(input, "\n") {
		sl, _ := parseSnailFishNumberAt(line, 0)
		sns = append(sns, sl)
	}
	return sns
}

func readInput() []SnailFishNumber {
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
