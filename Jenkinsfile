
pipeline {

    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    environment {
        APP_NAME = 'invoice-service'
        DOCKER_IMAGE = 'navzzzz/invoice-service'
    }

    options {
        timeout(time: 20, unit: 'MINUTES')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
                sh 'mvn -f invoice-service/pom.xml clean package'
            }
            post {
                always {
                    junit 'invoice-service/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build \
                      -t ${DOCKER_IMAGE}:${BUILD_NUMBER} \
                      -f invoice-service/Dockerfile \
                      invoice-service
                '''
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-badcreds',
                        usernameVariable: 'DOCKERHUB_USER',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    sh '''
                        set +x
                        echo "$DOCKERHUB_TOKEN" | docker login \
                          -u "$DOCKERHUB_USER" \
                          --password-stdin

                        docker push "${DOCKER_IMAGE}:${BUILD_NUMBER}"
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKERHUB_USER',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    sh '''
                        set +x
                        echo "$DOCKERHUB_TOKEN" | docker login \
                          -u "$DOCKERHUB_USER" \
                          --password-stdin

                        docker service update \
                          --with-registry-auth \
                          --image "${DOCKER_IMAGE}:${BUILD_NUMBER}" \
                          invoice-stack_app

                        docker logout
                    '''
                }
            }
        }

        stage('Verify') {
            when {
                branch 'main'
            }
            steps {
                sh '''
                    docker service ls
                    docker service ps invoice-stack_app
                    curl --fail http://localhost:8092/
                '''
            }
        }
    }
}

