package main

import (
	"math/big"
	"testing"

	"github.com/stretchr/testify/assert"
)

var input = Transmission("110100101111111000101000")

func TestAOC_parseInput(t *testing.T) {
	inputString := `D2FE28
`
	actualParsedInput := parseInput(inputString)
	assert.Equal(t, input, actualParsedInput)
}

func TestAOC_parsePacket(t *testing.T) {
	tests := []struct {
		name   string
		input  string
		packet Packet
		idx    int
	}{
		{
			name:  "Packet Literal",
			input: "D2FE28",
			packet: Packet{
				PacketHeader: PacketHeader{
					version: 6,
					id:      4,
				},
				value: 2021,
			},
			idx: 21,
		},
		{
			name:  "Packet Operator NBits",
			input: "38006F45291200",
			packet: Packet{
				PacketHeader: PacketHeader{
					version: 1,
					id:      6,
				},
				subPackets: []Packet{
					{
						PacketHeader: PacketHeader{
							version: 6,
							id:      4,
						},
						value: 10,
					},
					{
						PacketHeader: PacketHeader{
							version: 2,
							id:      4,
						},
						value: 20,
					},
				},
			},
			idx: 49,
		},
		{
			name:  "Packet Operator NPackets",
			input: "EE00D40C823060",
			packet: Packet{
				PacketHeader: PacketHeader{
					version: 7,
					id:      3,
				},
				subPackets: []Packet{
					{
						PacketHeader: PacketHeader{
							version: 2,
							id:      4,
						},
						value: 1,
					},
					{
						PacketHeader: PacketHeader{
							version: 4,
							id:      4,
						},
						value: 2,
					},
					{
						PacketHeader: PacketHeader{
							version: 1,
							id:      4,
						},
						value: 3,
					},
				},
			},
			idx: 51,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			tr := parseInput(tt.input)
			p, idx := tr.parsePacketAt(0)
			assert.Equal(t, tt.idx, idx)
			assert.Equal(t, tt.packet, p)
		})
	}
}

func TestAOC_getSolutionPart1(t *testing.T) {
	assert.Equal(t, 16, getSolutionPart1(parseInput("8A004A801A8002F478")))
	assert.Equal(t, 12, getSolutionPart1(parseInput("620080001611562C8802118E34")))
	assert.Equal(t, 23, getSolutionPart1(parseInput("C0015000016115A2E0802F182340")))
	assert.Equal(t, 31, getSolutionPart1(parseInput("A0016C880162017C3686B18A3D4780")))
}

func TestAOC_getSolutionPart2(t *testing.T) {
	assert.Equal(t, *big.NewInt(3), getSolutionPart2(parseInput("C200B40A82")))
	assert.Equal(t, *big.NewInt(54), getSolutionPart2(parseInput("04005AC33890")))
	assert.Equal(t, *big.NewInt(7), getSolutionPart2(parseInput("880086C3E88112")))
	assert.Equal(t, *big.NewInt(9), getSolutionPart2(parseInput("CE00C43D881120")))
	assert.Equal(t, *big.NewInt(1), getSolutionPart2(parseInput("D8005AC2A8F0")))
	assert.Equal(t, *big.NewInt(0), getSolutionPart2(parseInput("F600BC2D8F")))
	assert.Equal(t, *big.NewInt(0), getSolutionPart2(parseInput("9C005AC2F8F0")))
	assert.Equal(t, *big.NewInt(1), getSolutionPart2(parseInput("9C0141080250320F1802104A08")))
}

func Benchmark_Parse(b *testing.B) {
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		readInput()
	}
}

func Benchmark_Part1(b *testing.B) {
	b.ReportAllocs()

	input := readInput()
	for i := 0; i < b.N; i++ {
		getSolutionPart1(input)
	}
}

func Benchmark_Part2(b *testing.B) {
	b.ReportAllocs()

	input := readInput()
	for i := 0; i < b.N; i++ {
		getSolutionPart2(input)
	}
}
