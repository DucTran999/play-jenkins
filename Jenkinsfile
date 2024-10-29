pipeline {
    agent any

    tools {
        go '1.23.1'
    }
    
    environment {
        HOME = "${env.WORKSPACE}"
    }

    stages {

        stage('Pull') {
            steps {
                git 'https://github.com/DucTran999/play-jenkins.git'
                sh 'go version'
            }
        }

        stage('Build') {
            steps{
                sh 'go mod tidy'
                sh 'go run .'
            }
        }
    }
}
