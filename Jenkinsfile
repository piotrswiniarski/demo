pipeline {
    agent any
    stages {
       stage('test') {
           steps {
               sh './gradlew test --debug'
           }
       }

        stage('build') {
            steps {
                sh './gradlew build'
            }
        }
    }
}