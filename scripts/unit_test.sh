#!/bin/bash
go test -race $(go list ./... | grep -E "calc")
