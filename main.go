package main

import (
	"fmt"

	"github.com/DucTran999/play-jenkins/calc"
)

func main() {
	sum := calc.Sum(1, 2, 3, 4, 5)
	fmt.Println("Sum", sum)
}
