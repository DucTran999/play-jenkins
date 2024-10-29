package calc

func Sub(first int, num ...int) int {
	result := first
	for _, n := range num {
		result -= n
	}

	return result
}
