package calc_test

import (
	"testing"

	"github.com/DucTran999/play-jenkins/calc"
	"github.com/stretchr/testify/assert"
)

func TestMul(t *testing.T) {
	tests := []struct {
		input    []int
		expected int
	}{
		{input: []int{}, expected: 0},             // No arguments
		{input: []int{5}, expected: 5},            // Single argument
		{input: []int{2, 3}, expected: 6},         // Two arguments
		{input: []int{2, 3, 4}, expected: 24},     // Multiple arguments
		{input: []int{2, -3, 4}, expected: -24},   // Mix of positive and negative
		{input: []int{-2, -3, -4}, expected: -24}, // All negative
		{input: []int{2, 0, 4}, expected: 0},      // Multiplication with zero
	}

	for _, tt := range tests {
		result := calc.Mul(tt.input...)

		assert.Equal(t, result, tt.expected)
	}
}
