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
        GITHUB_REPO_URL = 'https://github.com/DucTran999/play-jenkins.git'
        GITHUB_REPO = 'DucTran999/play-jenkins'
        
        COMMIT_MESSAGE = sh(script: 'git log --format=%B -n 1', returnStdout: true).trim()
        COMMIT_HASH = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
        
        GOPATH = "${env.WORKSPACE}/go"
        PATH = "${GOPATH}/bin:${env.PATH}" 
    }

    parameters {
        string(name: 'PENDING', defaultValue: 'pending', description: 'pending status')
        string(name: 'SUCCESS', defaultValue: 'success', description: 'success status')
        string(name: 'FAILURE', defaultValue: 'failure', description: 'failure status')
    }

    stages {
        stage('Check lint') {
            steps {
                script{
                    try {
                        if (env.CHANGE_ID && env.CHANGE_TARGET == 'dev') {
                        echo "Triggered by a Pull Request to 'dev' branch"
                        echo "Pull Request ID: ${env.CHANGE_ID}"
                        echo "Source Branch: ${env.BRANCH_NAME}"
                        } else if (env.CHANGE_ID) {
                            echo "Triggered by a Pull Request to a different branch (${env.CHANGE_TARGET})"
                        } else {
                            echo "Triggered by a Push to branch: ${env.BRANCH_NAME}"
                        }
                        updateGitHubStatus(params.PENDING, 'CI/Lint')
                        sh 'curl -sSfL https://raw.githubusercontent.com/golangci/golangci-lint/master/install.sh | sh -s -- -b $(go env GOPATH)/bin v1.61.0'
                        sh 'golangci-lint run'
                        updateGitHubStatus(params.SUCCESS, 'CI/Lint')
                    } catch (err) {
                        updateGitHubStatus(params.FAILURE, 'CI/Lint')
                        error "Shell command failed: ${err.message}"
                    }
                }
            }
        }
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
