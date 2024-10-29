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

        stage('Install deps') {
            steps {
                sh 'curl -sSfL https://raw.githubusercontent.com/golangci/golangci-lint/master/install.sh | sh -s -- -b $(go env GOPATH)/bin v1.61.0'
                sh 'export "PATH=$PATH:/var/jenkins_home/workspace/play-jenkins/go/bin/golangci-lint"'
                sh 'golangci-lint run'
            }
        }

        stage('Build') {
            steps{
                sh 'go run .'
            }
        }
    }
}
