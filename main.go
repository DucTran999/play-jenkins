package main

import (
	"fmt"

	"github.com/DucTran999/play-jenkins/calc"
)

func main() {
	sum := calc.Sum(1, 2, 4)

	fmt.Printf("Sum %s", sum)
}
