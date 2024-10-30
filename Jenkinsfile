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
                script {
                    // Start build status
                    githubCheckStatus(status: 'IN_PROGRESS', message: 'Build in progress')
                    echo 'Building...'
                    // Mark build status as successful
                    githubCheckStatus(status: 'SUCCESS', message: 'Build successful')
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    // Start test status
                    githubCheckStatus(status: 'IN_PROGRESS', message: 'Testing in progress')
                    echo 'Testing...'
                    // Mark test status as successful
                    githubCheckStatus(status: 'SUCCESS', message: 'Tests passed')
                }
            }
        }
    }
    post {
        success {
            githubCheckStatus(status: 'SUCCESS', message: 'Pipeline completed successfully')
        }
        failure {
            githubCheckStatus(status: 'FAILURE', message: 'Pipeline failed')
        }
    }
}

def githubCheckStatus(status, message) {
    step([
        $class: 'GitHubChecksPublisher',
        name: 'ci-pipeline',  // Context name displayed in GitHub checks
        status: status,
        detailsURL: env.BUILD_URL,
        description: message
    ])
}
