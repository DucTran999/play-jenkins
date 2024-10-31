PKG_SCRIPTS=./scripts

default: help

help: ## Show help for each of the Makefile commands
	@@awk 'BEGIN \
		{FS = ":.*##"; printf "Usage: make ${cyan}<command>\n${white}Commands:\n"} \
		/^[a-zA-Z_-]+:.*?##/ \
		{ printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2 } \
		/^##@/ { printf "\n\033[1m%s\033[0m\n", substr($$0, 5) } ' \
		$(MAKEFILE_LIST)

.PHONY: all test clean
test: ## run unittest
	${PKG_SCRIPTS}/unit_test.sh

.PHONY: all coverage clean
coverage: ## code coverage
	${PKG_SCRIPTS}/coverage.sh

tidy: ## Tidy up the go.mod
	go mod tidy
