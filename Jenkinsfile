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

    parameters {
        string(name: 'commit_sha', defaultValue: '', description: 'Commit SHA of the PR')
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'master', url: 'https://github.com/DucTran999/play-jenkins'
            }
        }

        stage('Build') {
            steps {
                echo 'Building...'
                // Add your build commands here
            }
        }
    }

    post {
        success {
            script {
                echo "Sending 'success' status to GitHub"
                def response = httpRequest(
                    url: "https://api.github.com/repos/DucTran999/play-jenkins/project/statuses/${params.commit_sha}",
                    httpMode: 'POST',
                    contentType: 'APPLICATION_JSON',
                    requestBody: """{
                        "state": "success",
                        "description": "Build passed",
                        "context": "ci/jenkins-pipeline",
                        "target_url": "${env.BUILD_URL}"
                    }""",
                    authentication: 'playjenkins'
                )
                echo "GitHub Response: ${response.status}"
            }
        }

        always {
            echo "Pipeline finished. Commit SHA: ${params.commit_sha}"
        }
    }
}