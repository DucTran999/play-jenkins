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
        GITHUB_TOKEN_CREDENTIALS = credentials('playjenkins')
        COMMIT_MESSAGE = sh(script: "git log --format=%B -n 1", returnStdout: true).trim()
        COMMIT_HASH = sh(script: "git rev-parse HEAD", returnStdout: true).trim()
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
                    def response = httpRequest(
                        url: "https://api.github.com/repos/${GITHUB_REPO}/statuses/${COMMIT_HASH}",
                        httpMode: 'POST',
                        contentType: 'APPLICATION_JSON',
                        requestBody: """{
                            "state": "success",
                            "description": "Build passed",
                            "context": "ci/jenkins-pipeline",
                        }""",
                        authentication: '${GITHUB_TOKEN_CREDENTIALS}'
                    )

                    if (response.status != 200) {
                        error "Failed to update GitHub status: ${response.status} - ${response.content}"
                    } else {
                        echo "Successfully updated GitHub status for commit ${commitSha}"
                    }
                }
            }
        }
    }
}
