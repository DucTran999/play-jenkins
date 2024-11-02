pipeline {
    agent any

    tools {
        go '1.23.1'
    }

    triggers {
        githubPush()
    }

    environment {
        GITHUB_TOKEN = credentials('playjenkins')
        GITHUB_REPO = 'DucTran999/play-jenkins'

        COMMIT_MESSAGE = sh(script: 'git log --format=%B -n 1', returnStdout: true).trim()
        COMMIT_HASH = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
        BRANCH_NAME = "${GIT_BRANCH.split('/').size() > 1 ? GIT_BRANCH.split('/')[1..-1].join('/') : GIT_BRANCH}"

        GOPATH = "${env.WORKSPACE}/go"
        CGO_ENABLED = 1
        PATH = "${GOPATH}/bin:${env.PATH}"
        GO114MODULE = 'on'
    }

    parameters {
        string(name: 'PENDING', defaultValue: 'pending', description: 'pending status')
        string(name: 'SUCCESS', defaultValue: 'success', description: 'success status')
        string(name: 'FAILURE', defaultValue: 'failure', description: 'failure status')
    }

    stages {
        stage('Load script') {
            ci = load './devop/ci.groovy'
        }
        ci.startCI()
    }
}
