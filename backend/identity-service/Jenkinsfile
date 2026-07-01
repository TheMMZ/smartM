pipeline {
    agent any


        tools {
            maven "maven3"
        }

    environment {

        DOCKER_HUB_USER = "bouazalmat"
        IMAGE_NAME = "smartmaintain-identity-service"
        DOCKER_HUB_CREDS = "docker-hub"
    }

    stages {
        stage('Checkout') {
            steps {

                checkout scm
            }
        }

        stage('Maven Build') {
            steps {

                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                script {

                    docker.withRegistry('', "${DOCKER_HUB_CREDS}") {
                        def identityImage = docker.build("${DOCKER_HUB_USER}/${IMAGE_NAME}")
                        identityImage.push('latest')
                        identityImage.push("${env.BUILD_ID}")
                    }
                }
            }
        }

        stage('Ansible Deploy') {
            steps {

                ansiblePlaybook(
                    playbook: 'ansible/deploy.yml',
                    inventory: 'ansible/inventory.ini'
                )
            }
        }
    }

    post {
        success {
            echo "Pipeline  reussiiii !!!"
        }
        failure {
            echo "Kayn chi mouchkil f l build, checki  l logs."
        }
    }
}