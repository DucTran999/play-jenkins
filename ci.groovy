def installDependencies() {
    stage('Install dependencies') {
        when {
            expression { env.BRANCH_NAME ==~ /feature\/.*/ }
        }
        steps {
            script {
                sh 'go clean -modcache'
                sh 'curl -sSfL https://raw.githubusercontent.com/golangci/golangci-lint/master/install.sh | sh -s -- -b $(go env GOPATH)/bin v1.61.0'
                sh 'go mod tidy'
            }
        }
    }
}

def runLint() {
    stage('Lint') {
        when {
            expression { env.BRANCH_NAME ==~ /feature\/.*/ }
        }
        steps {
            script {
                try {
                    echo "Triggered by a Push to branch: ${env.BRANCH_NAME}"
                    updateGitHubStatus(params.PENDING, 'CI/Lint')
                    sh 'golangci-lint run .'
                    updateGitHubStatus(params.SUCCESS, 'CI/Lint')
                } catch (err) {
                    updateGitHubStatus(params.FAILURE, 'CI/Lint')
                    error "Lint command failed: ${err.message}"
                }
            }
        }
    }
}

def runTests() {
    stage('Test') {
        when {
            expression { env.BRANCH_NAME ==~ /feature\/.*/ }
        }
        steps {
            script {
                try {
                    echo "Running tests on branch: ${env.BRANCH_NAME}"
                    updateGitHubStatus(params.PENDING, 'CI/Test')
                    sh 'go test ./calc/...'
                    updateGitHubStatus(params.SUCCESS, 'CI/Test')
                } catch (err) {
                    updateGitHubStatus(params.FAILURE, 'CI/Test')
                    error "Test command failed: ${err.message}"
                }
            }
        }
    }
}

def checkCoverage() {
    stage('Coverage') {
        when {
            expression { env.BRANCH_NAME ==~ /feature\/.*/ }
        }
        steps {
            script {
                try {
                    echo "Checking coverage on branch: ${env.BRANCH_NAME}"
                    updateGitHubStatus(params.PENDING, 'CI/Coverage')
                    sh 'make coverage'
                    updateGitHubStatus(params.SUCCESS, 'CI/Coverage')
                } catch (err) {
                    updateGitHubStatus(params.FAILURE, 'CI/Coverage')
                    error "Coverage command failed: ${err.message}"
                }
            }
        }
    }
}

return this
