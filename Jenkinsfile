pipeline {
    agent any

    tools {
        go '1.23.1'
    }

    triggers {
        githubPush()
    }

    environment {
        GITHUB_TOKEN = credentials('playjenkins')
        GITHUB_REPO = 'DucTran999/play-jenkins'

        COMMIT_MESSAGE = sh(script: 'git log --format=%B -n 1', returnStdout: true).trim()
        COMMIT_HASH = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
        BRANCH_NAME = "${GIT_BRANCH.split('/').size() > 1 ? GIT_BRANCH.split('/')[1..-1].join('/') : GIT_BRANCH}"

        GOPATH = "${env.WORKSPACE}/go"
        CGO_ENABLED = 1
        PATH = "${GOPATH}/bin:${env.PATH}"
        GO114MODULE = 'on'
    }

    parameters {
        string(name: 'PENDING', defaultValue: 'pending', description: 'pending status')
        string(name: 'SUCCESS', defaultValue: 'success', description: 'success status')
        string(name: 'FAILURE', defaultValue: 'failure', description: 'failure status')
    }

    stages {
        stage('Install dependecies') {
            when {
                expression { env.BRANCH_NAME ==~ /feature\/.*/ }
            }
            steps{
                script{
                    sh 'go clean -modcache'
                    sh 'curl -sSfL https://raw.githubusercontent.com/golangci/golangci-lint/master/install.sh | sh -s -- -b $(go env GOPATH)/bin v1.61.0'
                    sh 'go mod tidy'
                }
            }
        }
        // stage('CI') {
        //     parallel {
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
                stage('Test') {
                    when {
                        expression { env.BRANCH_NAME ==~ /feature\/.*/ }
                    }
                    steps {
                        script {
                            try {
                                echo "Running tests on branch: ${env.BRANCH_NAME}"
                                updateGitHubStatus(params.PENDING, 'CI/Test')
                                sh 'go test ./...'
                                updateGitHubStatus(params.SUCCESS, 'CI/Test')
                            } catch (err) {
                                updateGitHubStatus(params.FAILURE, 'CI/Test')
                                error "Test command failed: ${err.message}"
                            }
                        }
                    }
                }
                stage('Coverage') {
                    when {
                        expression { env.BRANCH_NAME ==~ /feature\/.*/ }
                    }
                    steps {
                        script {
                            try {
                                echo "Checking coverage on branch: ${env.BRANCH_NAME}"
                                updateGitHubStatus(params.PENDING, 'CI/Coverage')
                                sh 'go clean -modcache'
                                sh 'go mod tidy'
                                sh 'make coverage'
                                updateGitHubStatus(params.SUCCESS, 'CI/Coverage')
                            } catch (err) {
                                updateGitHubStatus(params.FAILURE, 'CI/Coverage')
                                error "Coverage command failed: ${err.message}"
                            }
                        }
                    }
                }
            // }
        // }
    }
}


void updateGitHubStatus(String status, String context) {
    String curlCommand = '''
        curl --location "https://api.github.com/repos/DucTran999/play-jenkins/statuses/${COMMIT_HASH}" \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${GITHUB_TOKEN}" \
        -H "X-GitHub-Api-Version: 2022-11-28" \
        -H "Content-Type: application/json" \
        -d \'{"state": "'''+status+'''","context": "'''+context+'''","description":"build sucesstully"}\'\
        --silent --output /dev/null --write-out "%{http_code}"
    '''

    String responseCode = sh(script: curlCommand, returnStdout: true).trim()

    if (responseCode == '201') {
        echo "Successfully updated GitHub status for commit ${env.COMMIT_HASH}"
    } else {
        error "Failed to update GitHub status: HTTP ${responseCode}"
    }
}
