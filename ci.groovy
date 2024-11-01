def installDependencies() {
    sh 'go clean -modcache'
    sh 'curl -sSfL https://raw.githubusercontent.com/golangci/golangci-lint/master/install.sh | sh -s -- -b $(go env GOPATH)/bin v1.61.0'
    sh 'go mod tidy'
}

def runLint() {
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

def runTests() {
    try {
        echo "Running tests on branch: ${env.BRANCH_NAME}"
        updateGitHubStatus(params.PENDING, 'CI/Test')
        sh 'make test'
        updateGitHubStatus(params.SUCCESS, 'CI/Test')
    } catch (err) {
        updateGitHubStatus(params.FAILURE, 'CI/Test')
        error "Test command failed: ${err.message}"
    }
}

def checkCoverage() {
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

void updateGitHubStatus(String status, String context) {
    String curlCommand = '''
        curl --location "https://api.github.com/repos/DucTran999/play-jenkins/statuses/${COMMIT_HASH}" \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${GITHUB_TOKEN}" \
        -H "X-GitHub-Api-Version: 2022-11-28" \
        -H "Content-Type: application/json" \
        -d \'{"state": "'''+status+'''","context": "'''+context+'''", "description":"run '''+status+'''"}\'\
        --silent --output /dev/null --write-out "%{http_code}"
    '''

    String responseCode = sh(script: curlCommand, returnStdout: true).trim()

    if (responseCode == '201') {
        echo "Successfully updated GitHub status for commit ${env.COMMIT_HASH}"
    } else {
        error "Failed to update GitHub status: HTTP ${responseCode}"
    }
}


return this
