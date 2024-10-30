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
                publishChecks name: 'example', title: 'Pipeline Check', summary: 'check through pipeline',
                    text: 'you can publish checks in pipeline script',
                    detailsURL: 'https://github.com/DucTran999/play-jenkins',
                    actions: [[label:'an-user-request-action', description:'actions allow users to request pre-defined behaviours', identifier:'an unique identifier']]
                echo "GitHub Response: ${response.status}"
            }
        }

        always {
            echo "Pipeline finished. Commit SHA: ${params.commit_sha}"
        }
    }
}
