package calc

func Mulnum ...int) int {
	if len(num) == 0 {
		return 0
	}

	if len(num) == 1 {
		return num[0]
	}

	result := num[0]

	for i := 1; i < len(num); i++ {
		result *= num[i]
	}

	return result
}
