package main

import (
	"fmt"
	"io/ioutil"
	"math"
	"os"
	"strconv"
	"strings"
)

type Input struct {
	Alg Algorithm
	Img Image
}

type Point struct{ X, Y int }
type Algorithm [512]bool
type Image map[Point]bool

func (img Image) String() string {
	s := ""
	min, max := minMaxCorners(img)
	for y := min.Y - 1; y <= max.Y+1; y++ {
		for x := min.X - 1; x <= max.X+1; x++ {
			if img[Point{x, y}] {
				s += "#"
			} else {
				s += "."
			}
		}
		s += "\n"
	}
	return s
}

func (inp Input) EnhancedImageN(n int) Image {
	for i := 0; i < n; i++ {
		inp.Img = inp.EnhanceImage(inp.Alg[0] && i % 2 == 1)
	}
	return inp.Img
}

func (img Image) Invert() Image {
	inverted := make(Image)
	for p := range img {
		inverted[p] = !img[p]
	}
	return inverted
}

func (inp Input) EnhanceImage(considerOutsideLit bool) Image {
	img := make(map[Point]bool)
	for _, p := range pointsToCheck(inp.Img) {
		img[p] = inp.TransformPixelAt(p, considerOutsideLit)
	}
	return img
}

func (inp Input) TransformPixelAt(p Point, considerOutsideLit bool) bool {
	pixels := inp.Img.getRelevantPixels(p, 1, considerOutsideLit)

	return inp.Alg[pixelsToInt(pixels)]
}

func (img Image) getRelevantPixels(p Point, r int, considerOutsideLit bool) []bool {
	var pixles []bool
	for dy := -r; dy <= r; dy++ {
		for dx := -r; dx <= r; dx++ {
			lit, ok := img[Point{p.X+dx,p.Y+dy}]
			if !ok {
				pixles = append(pixles, considerOutsideLit)
			} else {
				pixles = append(pixles, lit)
			}
		}
	}
	return pixles
}

func pixelsToInt(pixels []bool) int {
	s := ""
	for _, v := range pixels {
		if v {
			s += "1"
		} else {
			s += "0"
		}
	}
	i, err := strconv.ParseUint(s, 2, 9)
	if err != nil {
		panic("cannot parse int")
	}

	return int(i)
}

func pointsToCheck(img Image) []Point {
	var pts []Point
	min, max := minMaxCorners(img)
	for x := min.X - 1; x <= max.X+1; x++ {
		for y := min.Y - 1; y <= max.Y+1; y++ {
			pts = append(pts, Point{x, y})
		}
	}
	return pts
}

func minMaxCorners(img Image) (Point, Point) {
	min, max := Point{math.MaxInt, math.MaxInt}, Point{math.MinInt, math.MinInt}
	for p := range img {
		switch {
		case p.X < min.X:
			min.X = p.X
			fallthrough
		case p.X > max.X:
			max.X = p.X
			fallthrough
		case p.Y < min.Y:
			min.Y = p.Y
			fallthrough
		case p.Y > max.Y:
			max.Y = p.Y
		}
	}
	return min, max
}

func (img Image) CountLit() int {
	count := 0
	for _, lit := range img {
		if lit {
			count++
		}
	}
	return count
}

func GetSolutionPart1(inp Input) int {
	return inp.EnhancedImageN(2).CountLit()
}

func GetSolutionPart2(inp Input) int {
	return inp.EnhancedImageN(50).CountLit()
}

func ParseInput(input string) Input {
	input = strings.TrimSpace(input)
	splits := strings.Split(input, "\n\n")
	return Input{ParseAlgorithm(splits[0]), ParseImage(splits[1])}
}

func ParseImage(input string) Image {
	img := make(map[Point]bool)
	for y, row := range strings.Split(input, "\n") {
		for x, c := range row {
			img[Point{x,y}] = c == '#'
		}
	}

	return img
}

func ParseAlgorithm(input string) Algorithm {
	if len(input) != 512 {
		panic(fmt.Sprintf("wrong alg length: %s", input))
	}

	alg := [512]bool{}
	for i := range input {
		alg[i] = input[i] == '#'
	}
	return alg
}

func readInput() Input {
	inputBytes, err := ioutil.ReadFile("input.txt")
	if err != nil {
		panic("couldn't read input")
	}

	return ParseInput(string(inputBytes))
}

func main() {
	input := readInput()

	part := os.Getenv("part")

	if part == "part2" {
		fmt.Println(GetSolutionPart2(input))
	} else {
		fmt.Println(GetSolutionPart1(input))
	}
}
