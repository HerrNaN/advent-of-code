package main

import (
	"container/heap"
	"fmt"
	"io/ioutil"
	"os"
	"strings"
)

var neighbourDeltas = []Point{
	{row: 0, col: 1},
	{row: -1, col: 0},
	{row: 0, col: -1},
	{row: 1, col: 0},
}

type Point struct{ row, col int }

func (p Point) add(other Point) Point {
	p.row += other.row
	p.col += other.col
	return p
}

type Cavern [][]int

func (c Cavern) pointOutOfBounds(p Point) bool {
	return p.row < 0 || p.col < 0 || p.row >= len(c) || p.col >= len(c)
}

func getSolutionPart1(c Cavern) int {
	dist, _ := c.Dijkstra(Point{0, 0})
	return dist[Point{len(c) - 1, len(c) - 1}]
}

func getSolutionPart2(c Cavern) int {
	c = c.Scaled(5)
	dist, _ := c.Dijkstra(Point{0, 0})
	return dist[Point{len(c) - 1, len(c) - 1}]
}

func (c Cavern) neighbourPoints(p Point) []Point {
	neighbours := make([]Point, 0, 8)

	for i := range neighbourDeltas {
		if !c.pointOutOfBounds(p.add(neighbourDeltas[i])) {
			neighbours = append(neighbours, p.add(neighbourDeltas[i]))
		}
	}

	return neighbours
}

func (c Cavern) Points() []Point {
	ps := make([]Point, len(c)*len(c))
	for row := range c {
		for col := range c {
			ps = append(ps, Point{row, col})
		}
	}
	return ps
}

func (c Cavern) Scaled(n int) Cavern {
	size := len(c)*n
	newC := make([][]int, size)
	for row := range newC {
		newC[row] = make([]int, size)
		oldRow := row % len(c)
		offsetRow := row / len(c)

		for col := range newC {
			oldCol := col % len(c)
			offsetCol := col / len(c)

			oldVal := c[oldRow][oldCol]
			newVal := (oldVal + offsetRow + offsetCol - 1) % 9 + 1

			newC[row][col] = newVal
		}
	}

	return newC
}

const (
	Infinity = int(^uint(0) >> 1)
)

var (
	Uninitialized = Point{-1, -1}
)

func parseInput(input string) Cavern {
	input = strings.TrimSpace(input)
	lines := strings.Split(input, "\n")
	cavern := make([][]int, len(lines))
	for r, line := range lines {
		cavern[r] = parseLine(line)
	}
	return cavern
}

func parseLine(line string) []int {
	row := make([]int, len(line))
	for c, r := range line {
		row[c] = int(r - '0')
	}
	return row
}

func readInput() Cavern {
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

//
// Below is shamelessly stolen from Rosetta Code
//

func (c Cavern) Dijkstra(source Point) (dist map[Point]int, prev map[Point]Point) {
	vs := c.Points()
	dist = make(map[Point]int, len(vs))
	prev = make(map[Point]Point, len(vs))
	sid := source
	dist[sid] = 0
	q := &PriorityQueue{
		items: make([]Point, 0, len(vs)),
		m:     make(map[Point]int, len(vs)),
		pr:    make(map[Point]int, len(vs)),
	}
	for _, v := range vs {
		if v != sid {
			dist[v] = Infinity
		}
		prev[v] = Uninitialized
		q.addWithPriority(v, dist[v])
	}
	for len(q.items) != 0 {
		u := heap.Pop(q).(Point)
		for _, v := range c.neighbourPoints(u) {
			alt := dist[u] + c[v.row][v.col]
			if alt < dist[v] {
				dist[v] = alt
				prev[v] = u
				q.update(v, alt)
			}
		}
	}
	return dist, prev
}

// A PriorityQueue implements heap.Interface and holds Items.
type PriorityQueue struct {
	items []Point
	m     map[Point]int // value to index
	pr    map[Point]int // value to priority
}

func (pq *PriorityQueue) Len() int           { return len(pq.items) }
func (pq *PriorityQueue) Less(i, j int) bool { return pq.pr[pq.items[i]] < pq.pr[pq.items[j]] }
func (pq *PriorityQueue) Swap(i, j int) {
	pq.items[i], pq.items[j] = pq.items[j], pq.items[i]
	pq.m[pq.items[i]] = i
	pq.m[pq.items[j]] = j
}

func (pq *PriorityQueue) Push(x interface{}) {
	n := len(pq.items)
	item := x.(Point)
	pq.m[item] = n
	pq.items = append(pq.items, item)
}

func (pq *PriorityQueue) Pop() interface{} {
	old := pq.items
	n := len(old)
	item := old[n-1]
	pq.m[item] = -1
	pq.items = old[0 : n-1]
	return item
}

// update modifies the priority of an item in the queue.
func (pq *PriorityQueue) update(item Point, priority int) {
	pq.pr[item] = priority
	heap.Fix(pq, pq.m[item])
}

func (pq *PriorityQueue) addWithPriority(item Point, priority int) {
	heap.Push(pq, item)
	pq.update(item, priority)
}

