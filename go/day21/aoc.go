package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"strconv"
	"strings"
)

type Input = []Player
type GameState struct {
	Players   []Player
	Turn      int
	Dice      Dice
	BoardSize int
}

type Dice struct {
	Sides       int
	LastRoll    int
	TimesRolled int
}

type Player struct {
	Pos, Score int
}

func (d Dice) RollNTimes(n int) (Dice, int) {
	sum := 0
	for i := 0; i < n; i++ {
		d = d.Roll()
		sum += d.LastRoll
	}
	return d, sum
}

func (d Dice) Roll() Dice {
	d.LastRoll = (d.LastRoll % d.Sides) + 1
	d.TimesRolled++
	return d
}

func (g GameState) PlayRound() GameState {
	var sum int

	g.Dice, sum = g.Dice.RollNTimes(3)
	g = g.MoveCurrentPlayer(sum)
	g = g.ScoreCurrentPlayer()
	g.Turn = (g.Turn + 1) % len(g.Players)

	return g
}

func (g GameState) MoveCurrentPlayer(steps int) GameState {
	p := g.Players[g.Turn]
	p.Pos = ((p.Pos + steps - 1) % g.BoardSize) + 1
	g.Players[g.Turn] = p
	return g
}

func (g GameState) ScoreCurrentPlayer() GameState {
	p := g.Players[g.Turn]
	p.Score += p.Pos
	g.Players[g.Turn] = p
	return g
}

func (g GameState) PlayUntil(isFinished func(GameState) bool) GameState {
	for !isFinished(g) {
		g = g.PlayRound()
	}

	return g
}

func (g GameState) FindPlayerWith(choose func(Player, Player) Player) Player {
	var player *Player
	for _, p := range g.Players {
		var tmp Player
		if player == nil {
			tmp = p
		} else {
			tmp = choose(*player, p)
		}
		player = &tmp
	}

	return *player
}

func LeastPoints(p, o Player) Player {
	if p.Score < o.Score {
		return p
	}

	return o
}

func (g GameState) APlayerHasScoredAtLeast(score int) bool {
	for _, p := range g.Players {
		if p.Score >= score {
			return true
		}
	}
	return false
}

func GetSolutionPart1(inp Input) int {
	state := GameState{
		Players:   inp,
		Dice:      Dice{
			Sides:       100,
		},
		BoardSize: 10,
	}

	state = state.PlayUntil(func(g GameState) bool { return g.APlayerHasScoredAtLeast(1000) })


	return state.FindPlayerWith(LeastPoints).Score * state.Dice.TimesRolled
}

func GetSolutionPart2(inp Input) int {
	panic("unimplemented")
}

func ParseInput(input string) Input {
	input = strings.TrimSpace(input)
	var players []Player
	for _, playerString := range strings.Split(input, "\n") {
		players = append(players, ParsePlayer(playerString))
	}
	return players
}

func ParsePlayer(playerString string) Player {
	startPosString := strings.Split(playerString, ": ")[1]
	startPos, err := strconv.Atoi(startPosString)
	if err != nil {
		panic("cannot parse start pos")
	}
	return Player{
		Pos:   startPos,
	}
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
