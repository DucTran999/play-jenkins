def ciWorkflows = load 'pipeline.groovy'

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
            steps {
                script {
                    ciWorkflows.installDependencies()
                }
            }
        }
        stage('CI') {
            parallel {
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
                                sh 'go test ./calc/...'
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
        }
    }
}
