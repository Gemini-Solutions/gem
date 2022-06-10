node('maven_runner') {
 stage('backend_checkout') {
         dir ('GembookSvc') {
         checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg:  [], \
userRemoteConfigs: [[credentialsId: 'admingithub', url: 'https://github.com/Gemini-Solutions/GembookSvc.git']]])
         }
    }
  stage('Maven_Build') {
        container('mavenbuild') {
         dir ('GembookSvc') {
            sh 'rm -rf target'
            sh 'mvn package'
            dir ('target'){
            sh 'chmod +x gembook-service.jar'
            }
       }
        }
     }
  }


node('image_builder') {
  try {
   stage('Build_image') {
            dir ('GembookSvc') {
              container('dockerbuild') {
                //checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg:  [], \
//userRemoteConfigs: [[credentialsId: 'gitea', url: 'https://gemgitea.geminisolutions.com/gembook/GembookSvc.git']]])
              withCredentials([usernamePassword(credentialsId: 'docker_registry', passwordVariable: 'docker_pass', usernameVariable: 'docker_user')]) {
              sh 'docker image build -f DockerFile -t registry.geminisolutions.com/gembook/gembook:1.0-$BUILD_NUMBER .'
              sh '''docker login -u $docker_user -p $docker_pass https://registry.geminisolutions.com'''
              sh 'docker push registry.geminisolutions.com/gembook/gembook:1.0-$BUILD_NUMBER'
              sh 'rm -rf dist/'
           }
         }
            }
      }
  } finally {
     sh 'echo current_image="registry.geminisolutions.com/gembook/gembook:1.0-$BUILD_NUMBER" > build.properties'
     archiveArtifacts artifacts: 'build.properties', onlyIfSuccessful: true
  }
    }


