pipeline {
    // means it can run on any Jenkins node (usually the default built-in one).
    agent any

    environment {
        DOCKER_IMAGE = "yuehming/user_server"
        DOCKER_TAG   = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                // Equivalent to running git clone and checking out the main branch.
                git branch: 'main', url: 'https://github.com/JohnsonJapow/skills_exchange_platform/skills_server.git'
            }
        }

        stage('Test') {
            steps {
                // Gradle do the unit tests
                sh './gradlew test'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Jenkins builds your Docker image from the Dockerfile in your repo.
                sh "docker build -t $DOCKER_IMAGE:$DOCKER_TAG ."
            }
        }

        stage('Push Docker Image') {
            steps {
                // Uses Jenkins stored credentials (ID: dockerhub-credentials) to log in to DockerHub securely.
                // Pushes the built image (johnsonjapow/user_server:latest) to your DockerHub account.
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    sh "docker push $DOCKER_IMAGE:$DOCKER_TAG"
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'db-credentials', usernameVariable: 'DB_USER', passwordVariable: 'DB_PASS')]) {
                    sh """
                    export SKILL_EXCHANGE_USERNAME=$DB_USER
                    export SKILL_EXCHANGE_PASSWORD=$DB_PASS
                    export SKILL_EXCHANGE_DATABASE=skill_exchange
                    docker compose down
                    docker compose up -d
                    """
                }
            }
        }
    }
}
