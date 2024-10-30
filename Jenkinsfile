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
                    sh """
                        curl --locaEXAMPLE_CREDS_USRtion 'https://api.github.com/repos/DucTran999/play-jenkins/statuses/$COMMIT_HASH' \
                        --header 'Accept: application/vnd.github+json' \
                        --header 'Authorization: Bearer $GITHUB_TOKEN' \
                        --header 'X-GitHub-Api-Version: 2022-11-28' \
                        --header 'Content-Type: application/json' \
                        --data '{
                            "state": "success",
                            "context": "continuous-integration/jenkins"
                        }'
                        """

                    // if (response.status != 200) {
                    //     error "Failed to update GitHub status: ${response.status} - ${response.content}"
                    // } else {
                    //     echo "Successfully updated GitHub status for commit ${commitSha}"
                    // }
                }
            }
        }
    }
}
