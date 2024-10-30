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

    // Environment variables for GitHub integration
    environment {
        GITHUB_REPO = 'owner/repository'
        GITHUB_TOKEN = credentials('github-token')
    }

    // Configure GitHub status checks
    options {
        githubSetConfig('GitHub')
    }

    stages {
        stage('Build') {
            steps {
                // Set GitHub status to pending
                githubNotify context: 'Build', description: 'Build in progress...', status: 'PENDING'
                
                try {
                    sh 'mvn clean install'
                    // If build succeeds, set status to success
                    githubNotify context: 'Build', description: 'Build succeeded!', status: 'SUCCESS'
                } catch (Exception e) {
                    // If build fails, set status to failure
                    githubNotify context: 'Build', description: 'Build failed!', status: 'FAILURE'
                    throw e
                }
            }
        }

        stage('Test') {
            steps {
                githubNotify context: 'Test', description: 'Tests running...', status: 'PENDING'
                
                try {
                    sh 'mvn test'
                    githubNotify context: 'Test', description: 'Tests passed!', status: 'SUCCESS'
                } catch (Exception e) {
                    githubNotify context: 'Test', description: 'Tests failed!', status: 'FAILURE'
                    throw e
                }
            }
        }

        stage('Deploy') {
            steps {
                githubNotify context: 'Deploy', description: 'Deployment in progress...', status: 'PENDING'
                
                try {
                    sh 'your-deployment-script.sh'
                    githubNotify context: 'Deploy', description: 'Deployment successful!', status: 'SUCCESS'
                } catch (Exception e) {
                    githubNotify context: 'Deploy', description: 'Deployment failed!', status: 'FAILURE'
                    throw e
                }
            }
        }
    }

    // Post-build actions
    post {
        success {
            githubNotify context: 'Pipeline', description: 'All stages completed successfully!', status: 'SUCCESS'
        }
        failure {
            githubNotify context: 'Pipeline', description: 'Pipeline failed!', status: 'FAILURE'
        }
    }
}