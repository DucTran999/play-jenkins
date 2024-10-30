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
                    githubCheckStatus(status: 'IN_PROGRESS', message: 'Build started')
                    // Simulate build step (replace with actual build commands)
                    echo 'Building...'
                    // Mark as success upon completion
                    githubCheckStatus(status: 'SUCCESS', message: 'Build successful')
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    githubCheckStatus(status: 'IN_PROGRESS', message: 'Testing started')
                    // Simulate test step (replace with actual test commands)
                    echo 'Testing...'
                    // Mark as success upon completion
                    githubCheckStatus(status: 'SUCCESS', message: 'Tests passed')
                }
            }
        }
    }
    post {
        always {
            // Final status update depending on build result
            if (currentBuild.result == 'SUCCESS') {
                githubCheckStatus(status: 'SUCCESS', message: 'Pipeline completed successfully')
            } else {
                githubCheckStatus(status: 'FAILURE', message: 'Pipeline failed')
            }
        }
    }
}

def githubCheckStatus(status, message) {
    step([
        $class: 'GitHubChecksPublisher',
        name: 'ci-pipeline',
        status: status,
        detailsURL: env.BUILD_URL,
        description: message
    ])
}
