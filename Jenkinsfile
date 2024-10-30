// pipeline {
//     agent any

//     environment {
//         HOME = "${env.WORKSPACE}"
//     }

//     stages {

//         stage('Build') {
//             steps{
//                 sh 'go mod tidy'
//                 sh 'go run .'
//             }
//         }
//     }
// }

pipeline {
    agent any

    tools {
        go '1.23.1'
    }

    environment {
        GITHUB_REPO = 'DucTran999/play-jenkins'
        REPO_LINK = 'https://github.com/DucTran999/play-jenkins.git'
        COMMIT_MESSAGE = sh(script: 'git log --format=%B -n 1', returnStdout: true).trim()
        COMMIT_HASH = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
        GITHUB_TOKEN = credentials('playjenkins')
    }

    parameters {
        string(name: 'PENDING', defaultValue: 'pending', description: 'pending status')
        string(name: 'SUCCESS', defaultValue: 'success', description: 'success status')
        string(name: 'FAILURE', defaultValue: 'failure', description: 'failure status')
    }

    stages {
        // stage('Checkout') {
        //     steps {
        //         // Checkout the source code from the repository
        //         git url: 'https://github.com/your/repo.git', branch: 'main'
        //     }
        // }
        stage('Install golangci-lint') {
            steps {
                // Install golangci-lint if not already installed
                sh 'curl -sSfL https://raw.githubusercontent.com/golangci/golangci-lint/master/install.sh | sh -s -- -b $(go env GOPATH)/bin v1.50.0'
            }
        }
        stage('Check lint') {
            steps {
                script{
                    try {
                        updateGitHubStatus(params.PENDING, 'CI/Lint')
                        sh 'golangci-lint version'
                        updateGitHubStatus(params.SUCCESS, 'CI/Lint')
                    } catch (err) {
                        updateGitHubStatus(params.FAILURE, 'CI/Lint')
                        error "Shell command failed: ${e.message}"
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
