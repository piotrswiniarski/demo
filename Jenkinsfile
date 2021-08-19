pipeline {
    agent any
    stages {
       stage('test') {
           steps {
               sh './gradlew test'
           }
       }

        stage('build') {
            steps {
                sh './gradlew build'
            }
        }
    }
}