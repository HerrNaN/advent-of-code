package main

import (
	"bytes"
	"fmt"
	"io/ioutil"
	"math/big"
	"os"
	"strconv"
	"strings"
)

const (
	PacketTypeIDLiteral uint = 4
)

type Transmission string

type PacketHeader struct {
	version uint
	id      uint
}

type Packet struct {
	PacketHeader
	value      uint64
	subPackets []Packet
}

func (p Packet) sumVersions() int {
	var sum int
	for _, sp := range p.subPackets {
		sum += sp.sumVersions()
	}
	return int(p.version) + sum
}

func (p Packet) eval() *big.Int {
	switch p.id {
	case 0:
		return p.sum()
	case 1:
		return p.product()
	case 2:
		return p.min()
	case 3:
		return p.max()
	case 4:
		return big.NewInt(int64(p.value))
	case 5:
		return p.gt()
	case 6:
		return p.lt()
	case 7:
		return p.eq()
	default:
		panic("invalid op")
	}
}

func (p Packet) sum() *big.Int {
	sum := big.NewInt(0)
	for _, sp := range p.subPackets {
		sum.Add(sum, sp.eval())
	}
	return sum
}

func (p Packet) product() *big.Int {
	product := big.NewInt(1)
	for _, sp := range p.subPackets {
		product.Mul(product, sp.eval())
	}
	return product
}

func (p Packet) min() *big.Int {
	min, set := big.NewInt(0), false
	for _, sp := range p.subPackets {
		spv := sp.eval()
		if !set || spv.Cmp(min) == -1 {
			min = spv
			set = true
		}
	}
	return min
}

func (p Packet) max() *big.Int {
	max, set := big.NewInt(0), false
	for _, sp := range p.subPackets {
		spv := sp.eval()
		if !set || spv.Cmp(max) == 1 {
			max = spv
			set = true
		}
	}
	return max
}

func (p Packet) gt() *big.Int {
	if p.subPackets[0].eval().Cmp(p.subPackets[1].eval()) == 1 {
		return big.NewInt(1)
	}
	return big.NewInt(0)
}

func (p Packet) lt() *big.Int {
	if p.subPackets[0].eval().Cmp(p.subPackets[1].eval()) == -1 {
		return big.NewInt(1)
	}
	return big.NewInt(0)
}

func (p Packet) eq() *big.Int {
	if p.subPackets[0].eval().Cmp(p.subPackets[1].eval()) == 0 {
		return big.NewInt(1)
	}
	return big.NewInt(0)
}

func (h PacketHeader) isLiteral() bool {
	return h.id == PacketTypeIDLiteral
}

func getSolutionPart1(t Transmission) int {
	p, _ := t.parsePacketAt(0)
	return p.sumVersions()
}

func getSolutionPart2(t Transmission) big.Int {
	p, _ := t.parsePacketAt(0)
	return *p.eval()
}

func (t *Transmission) parsePacketAt(idx int) (Packet, int) {
	header, idx := t.parseHeaderAt(idx)
	if header.isLiteral() {
		value, idx := t.parseLiteralAt(idx)
		return Packet{
			PacketHeader: header,
			value:        value,
		}, idx
	}

	subPackets, idx := t.parseSubPacketsAt(idx)
	return Packet{
		PacketHeader: header,
		subPackets:   subPackets,
	}, idx
}

func (t *Transmission) parseSubPacketsAt(idx int) ([]Packet, int) {
	switch string(*t)[idx] {
	case '0':
		nBits := bitsToInt(string(*t)[idx+1 : idx+1+15])
		return t.parseNBitsPacketsAt(idx+1+15, int(nBits))
	case '1':
		nPackets := bitsToInt(string(*t)[idx+1 : idx+1+11])
		return t.parseNPacketsAt(idx+1+11, int(nPackets))
	default:
		panic("length type ID not 1 or 0")
	}
}

func (t *Transmission) parseNBitsPacketsAt(idx, nBits int) ([]Packet, int) {
	end := idx + nBits
	var packets []Packet
	var p Packet
	for {
		p, idx = t.parsePacketAt(idx)
		packets = append(packets, p)

		if idx == end {
			break
		} else if idx > end {
			panic("assumption wrong")
		}
	}
	return packets, idx
}

func (t *Transmission) parseNPacketsAt(idx, nPackets int) ([]Packet, int) {
	var packets []Packet
	var p Packet
	for i := 0; i < nPackets; i++ {
		p, idx = t.parsePacketAt(idx)
		packets = append(packets, p)
	}
	return packets, idx
}

func (t *Transmission) parseLiteralAt(idx int) (uint64, int) {
	bs := &bytes.Buffer{}
	hasMore := true
	var group string

	for hasMore {
		group, hasMore, idx = t.parseGroupAt(idx)
		bs.WriteString(group)
	}

	literal, _ := strconv.ParseUint(bs.String(), 2, bs.Len())
	return literal, idx
}

func (t *Transmission) parseGroupAt(idx int) (string, bool, int) {
	hasMore := string(*t)[idx] == '1'
	group := string(*t)[idx+1 : idx+5]
	return group, hasMore, idx + 5
}

func (t *Transmission) parseHeaderAt(idx int) (PacketHeader, int) {
	return PacketHeader{
		version: bitsToInt(string(*t)[idx : idx+3]),
		id:      bitsToInt(string(*t)[idx+3 : idx+6]),
	}, idx + 6
}

func bitsToInt(bits string) uint {
	ui, _ := strconv.ParseUint(bits, 2, 64)
	return uint(ui)
}

func hexToBin(hex string) string {
	i, _ := strconv.ParseUint(hex, 16, 64)
	return fmt.Sprintf("%04b", i)
}

func parseInput(input string) Transmission {
	input = strings.TrimSpace(input)
	bs := &bytes.Buffer{}
	for _, r := range input {
		bs.WriteString(hexToBin(string(r)))
	}
	return Transmission(bs.String())
}

func readInput() Transmission {
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
