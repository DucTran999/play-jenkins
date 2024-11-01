package calc_test

import (
	"testing"

	"github.com/DucTran999/play-jenkins/calc"
	"github.com/stretchr/testify/assert"
)

func TestSub(t *testing.T) {
	tests := []struct {
		input    []int
		expected int
	}{
		{input: []int{}, expected: 0},
		{input: []int{1, 2}, expected: -1},
	}

	for _, tt := range tests {
		result := calc.Sub(tt.input...)
		assert.Equal(t, tt.expected, result)
	}
}
