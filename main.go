package main

import (
	"fmt"

	"github.com/DucTran999/play-jenkins/calc"
)

func main() {
	sum := calc.Sum(1, 2, 3, 5, 5)

	fmt.Printf("Sum %s", sum)
}
