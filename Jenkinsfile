pipeline {
    // means it can run on any Jenkins node (usually the default built-in one).
    agent any

    environment {
        DOCKER_IMAGE = "johnsontengg/user_server"
        DOCKER_TAG   = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                // Equivalent to running git clone and checking out the main branch.
                git branch: 'main', url: 'https://github.com/JohnsonJapow/skills_exchange_platform.git'
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
                    sh '''
                    echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                    docker push $DOCKER_IMAGE:$DOCKER_TAG
                    docker logout
                    '''
                }
            }
        }

        stage('Deploy with Docker') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'db-credentials', usernameVariable: 'DB_USER', passwordVariable: 'DB_PASS')]) {
                    sh '''
                    export SKILL_EXCHANGE_USERNAME=$DB_USER
                    export SKILL_EXCHANGE_PASSWORD=$DB_PASS
                    export SKILL_EXCHANGE_DATABASE=skill_exchange
                    docker pull $DOCKER_IMAGE:$DOCKER_TAG
                    docker ps -a -q -f name=user_server | grep -q . && docker stop user_server && docker rm user_server || echo "No existing user_server container"
                    docker run -d --name user_server -p 8080:8080 \
                        -e SKILL_EXCHANGE_USERNAME=$DB_USER \
                        -e SKILL_EXCHANGE_PASSWORD=$DB_PASS \
                        -e SKILL_EXCHANGE_DATABASE=skill_exchange \
                        $DOCKER_IMAGE:$DOCKER_TAG

                    '''
                }
            }
        }
    }
}
