// pipeline {
//     agent any

//     tools {
//         go '1.23.1'
//     }
    
//     environment {
//         HOME = "${env.WORKSPACE}"
//     }

//     stages {

//         stage('Pull') {
//             steps {
//                 git 'https://github.com/DucTran999/play-jenkins.git'
//                 sh 'go version'
//             }
//         }

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

    environment {
        GITHUB_REPO = 'DucTran999/play-jenkins'
        COMMIT_MESSAGE = sh(script: "git log --format=%B -n 1", returnStdout: true).trim()
        COMMIT_HASH = sh(script: "git rev-parse HEAD", returnStdout: true).trim()
        GITHUB_TOKEN = credentials('playjenkins')
    }

    parameters {
        string(name: 'PENDING', defaultValue: 'pending', description: 'pending status')
    }

    stages {
        stage('Lint') {
            steps {
                echo 'Golangci-lint running...'
                script{
                    updateGitHubStatus(parameters.PENDING)
                }
            }
        }
    }
}

def updateGitHubStatus(status) {
    def curlCommand = '''
        curl --location "https://api.github.com/repos/DucTran999/play-jenkins/statuses/${COMMIT_HASH}" \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${GITHUB_TOKEN}" \
        -H "X-GitHub-Api-Version: 2022-11-28" \
        -H "Content-Type: application/json" \
        --data '{
            "state": "success",
            "context": "ok"
        }'\
        --silent --output /dev/null --write-out "%{http_code}"
    '''

    def responseCode = sh(script: curlCommand, returnStdout: true).trim()
    
    if (responseCode == '201') {
        echo "Successfully updated GitHub status for commit ${env.COMMIT_HASH}"
    } else {
        error "Failed to update GitHub status: HTTP ${responseCode}"
    }

    echo $status
}
