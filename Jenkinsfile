pipeline {
    agent any
    stages {
        stage('build') {
            steps {
                sh 'pwd'
                sh 'ls -la'
                sh 'java -version'
                sh './gradlew build'
            }
        }

    }
}