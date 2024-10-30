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
        COMMIT_MESSAGE = sh(script: "git log --format=%B -n 1", returnStdout: true).trim()
        COMMIT_HASH = sh(script: "git rev-parse HEAD", returnStdout: true).trim()
        GITHUB_TOKEN = credentials('playjenkins')
    }

    parameters {
        string(name: 'PENDING', defaultValue: 'pending', description: 'pending status')
        string(name: 'SUCCESS', defaultValue: 'success', description: 'success status')
        string(name: 'FAILURE', defaultValue: 'failure', description: 'failure status')
    }

    stages {
        stage('Pull') {
            stages {
                steps {
                git 'https://github.com/DucTran999/play-jenkins.git'
                script {
                    echo 'Running Golangci-lint...'
                    updateGitHubStatus(params.PENDING, 'linting...')
                    sh 'go version'
                    updateGitHubStatus(params.SUCCESS, 'linting completed...')
                }
            }
        }
    }
}

def updateGitHubStatus(String status, String context) {
    def curlCommand = '''
        curl --location "https://api.github.com/repos/DucTran999/play-jenkins/statuses/${COMMIT_HASH}" \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${GITHUB_TOKEN}" \
        -H "X-GitHub-Api-Version: 2022-11-28" \
        -H "Content-Type: application/json" \
        -d \'{"state": "''' + status + '''","context": "'''+ context +'''"}\'\
        --silent --output /dev/null --write-out "%{http_code}"
    '''

    def responseCode = sh(script: curlCommand, returnStdout: true).trim()
    
    if (responseCode == '201') {
        echo "Successfully updated GitHub status for commit ${env.COMMIT_HASH}"
    } else {
        error "Failed to update GitHub status: HTTP ${responseCode}"
    }
}
