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
    stages {
        stage('Build') {
            steps {
                // Build commands go here
                echo 'Building...'
            }
        }
    }
    post {
        success {
            publishChecks('SUCCESS', 'Build completed successfully')
        }
        failure {
            publishChecks('FAILURE', 'Build failed')
        }
    }
}

def publishChecks(status, message) {
    step([
        $class: 'GitHubChecksPublisher',
        name: 'ci-cd',
        status: status,
        detailsURL: env.BUILD_URL,
        description: message
    ])
}
