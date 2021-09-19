Jenkinsfile (Declarative Pipeline)
pipeline {
    agent { docker { image 'projectdashboard' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}