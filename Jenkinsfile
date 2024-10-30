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
                    def commitSha = params.commit_sha
                    def response = httpRequest(
                        url: "https://api.github.com/repos/${GITHUB_REPO}/statuses/${commitSha}",
                        httpMode: 'POST',
                        contentType: 'APPLICATION_JSON',
                        requestBody: """{
                            "state": "success",
                            "description": "Build passed",
                            "context": "ci/jenkins-pipeline",
                        }""",
                        authentication: 'Bearer $GITHUB_TOKEN_CREDENTIALS'
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

    parameters {
        string(name: 'commit_sha', defaultValue: '', description: 'Commit SHA to update status')
    }
}
