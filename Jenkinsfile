pipeline {
    agent any
    options {
    timeout(time: 1, unit: 'HOURS') // set timeout 1 hour
    }

    environment {

        TIME_ZONE = 'Asia/Seoul'

        //github
        TARGET_BRANCH = 'develop'
        REPOSITORY_URL= 'https://github.com/rework-kr/REWORK-SERVER'

        //docker-hub
        registryCredential = 'docker-hub'

        CONTAINER_NAME = 'rework-backend'
        IMAGE_NAME = 'kimminwoo1234/rework-backend'
    }



    stages {


        stage('init') {
            steps {
                echo 'init stage'
                deleteDir()
            }
            // post {
            //     success {
            //         echo 'success init in pipeline'
            //     }
            //     // failure {
            //     //     slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
            //     //     error 'fail init in pipeline'
            //     // }
            // }
        }

        stage('Prepare') {
            steps {
                echo 'Cloning Repository'
                git branch: 'develop',
                    url: 'https://github.com/rework-kr/REWORK-SERVER.git',
            }
            // post {
            //     success {
            //         echo 'Successfully Cloned Repository'
            //     }
            //     // failure {
            //     //     slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
            //     //     error 'This pipeline stops here...'
            //     // }
            // }
        }
        stage('Build Gradle') {
            steps {
                echo 'Build Gradle'

                dir('.'){
                    sh '''
                        pwd
                        cd /var/jenkins_home/workspace/rework-backend
                        chmod +x ./gradlew
                        ./gradlew build

                    '''
                }
            }
            // post {
            //     // failure {
            //     //     slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
            //     //     error 'This pipeline stops here...'
            //     // }
            // }
        }

        // 도커 이미지를 만든다. build number로  latest 태그 부여한다.
        stage('Build Docker') {
            steps {
                echo 'Build Docker'
                sh """
                    cd /var/jenkins_home/workspace/rework-backend
                    docker build -t $IMAGE_NAME:$BUILD_NUMBER .
                    docker tag $IMAGE_NAME:$BUILD_NUMBER $IMAGE_NAME:latest
                """
            }
            // post {
            //     // failure {
            //     //     slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
            //     //     error 'This pipeline stops here...'
            //     // }
            // }
        }


     // 빌드넘버 latest
        stage('Push Docker') {
            steps {
                echo 'Push Docker'
                script {
                    docker.withRegistry('', registryCredential) {
                        docker.image("${IMAGE_NAME}:${BUILD_NUMBER}").push()
                        docker.image("${IMAGE_NAME}:latest").push()
                    }
                }
            }
            // post {
            //     // failure {
            //     //     slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
            //     //     error 'This pipeline stops here...'
            //     // }
            // }
        }

    stage('rm container and rm images') {
            steps {
                echo 'rm container stage'
                sh '''
                docker rm -f $CONTAINER_NAME
                docker image prune -f --filter "label=${IMAGE_NAME}"
                '''
            }
            // post {
            //     success {
            //         echo 'success rm container in pipeline'
            //     }
            //     // failure {
            //     //     slackSend (channel: '#backend', color: '#FF0000', message: "${env.CONTAINER_NAME} CI / CD 파이프라인 구동 실패, 젠킨스 확인 해주세요")
            //     //     error 'fail rm container in pipeline'
            //     // }
            // }
    }

    stage('Docker run') {
            steps {
                echo 'Pull Docker Image & Docker Image Run'

                script {
                    docker.withRegistry('', registryCredential) {
                        sshagent (credentials: ['ssh']) {
                            sh "ssh -o StrictHostKeyChecking=no ubuntu@43.200.143.218 'cd rework && sed -i \"s/^BUILD_NUMBER=.*/BUILD_NUMBER=${BUILD_NUMBER}/\" .env'"
                            sh "ssh -o StrictHostKeyChecking=no ubuntu@43.200.143.218 'cd rework && ./deploy.sh'"
                            sh "ssh -o StrictHostKeyChecking=no ubuntu@43.200.143.218 'docker image prune -f --all'"
                        }
                    }
                }

            }
            post {
                    failure {
                      echo 'Docker Run failure !'
                    }
                    success {
                      echo 'Docker Run Success !'
                    }
            }
        }



    stage('Clean Up Docker Images on Jenkins Server') {
        steps {
            echo 'Cleaning up unused Docker images on Jenkins server'

            // Clean up unused Docker images, including those created within the last hour
            // sh "docker image prune -f --all --filter \"until=1m\""
            sh "docker image prune -f --all"
        }
    }




}

    // post {
    //     success {
    //         slackSend (channel: '#backend', color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
    //     }
    //     failure {
    //         slackSend (channel: '#backend', color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
    //     }
    // }
}
