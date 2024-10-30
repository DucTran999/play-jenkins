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

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                // Your build steps here
            }
        }

        stage('Update GitHub Status') {
            steps {
                script {
                    def statusCode = updateGitHubStatus(env.COMMIT_HASH, env.GITHUB_TOKEN)
                    if (statusCode != '201') {
                        error "Failed to update GitHub status: HTTP ${statusCode}"
                    } else {
                        echo "Successfully updated GitHub status for commit ${env.COMMIT_HASH}"
                    }
                }
            }
        }
    }
}

def updateGitHubStatus(commitHash, githubToken) {
    return sh(script: """
        curl --location "https://api.github.com/repos/DucTran999/play-jenkins/statuses/${commitHash}" \
            -H "Accept: application/vnd.github+json" \
            -H "Authorization: Bearer ${githubToken}" \
            -H "X-GitHub-Api-Version: 2022-11-28" \
            -H "Content-Type: application/json" \
            --data '{
                "state": "success",
                "context": "continuous-integration/jenkins"
            }' \
            --silent --output /dev/null --write-out "%{http_code}"
    """, returnStdout: true).trim()
}