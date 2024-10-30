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
                    echo 'Building...' // Replace with actual build commands
                }
            }
            publishChecks name: 'example', title: 'Pipeline Check', summary: 'check through pipeline',
                text: 'you can publish checks in pipeline script',
                detailsURL: 'https://github.com/jenkinsci/checks-api-plugin#pipeline-usage',
                actions: [[label:'an-user-request-action', description:'actions allow users to request pre-defined behaviours', identifier:'an unique identifier']]
        }
    }
}
