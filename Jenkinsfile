#!/usr/bin/env groovy

def docsOutDir = 'build/docsOut'
def docsRepositoryUrl = 'git@github.com:CraftTweaker/CraftTweaker-Documentation.git'
def gitSshCredentialsId = 'crt_git_ssh_key'
def botUsername = 'crafttweakerbot'
def botEmail = 'crafttweakerbot@gmail.com'

def documentationDir = 'CrafttweakerDocumentation'
def exportDirInRepo = 'docs_exported/1.18/botania'

pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    agent any
    tools {
        jdk "jdk-17.0.1"
    }
    stages {
        stage('Clean') {
            steps {
                echo 'Cleaning Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean --no-daemon'
            }
        }
        stage('Build and Deploy Release') {
            when {
                tag 'release-*'
            }
            environment {
                RELEASE_MODE = '1'
            }
            steps {
                sh './gradlew build publish --no-daemon'
            }
        }
        stage('Build and Deploy Snapshot') {
            when {
                not {
                    tag 'release-*'
                }
            }
            steps {
                sh './gradlew build publish --no-daemon'
            }
        }
	/*
        stage('Update CraftTweaker Docs') {
            when {
                tag 'release-*'
            }
            steps {
                echo "Cloning Repository at Branch main"

                dir(documentationDir) {
                    git credentialsId: gitSshCredentialsId, url: docsRepositoryUrl, branch: "main", changelog: false
                }


                echo "Clearing existing Documentation export"
                dir(documentationDir) {
                    sh "rm --recursive --force ./$exportDirInRepo"
                }


                echo "Moving Generated Documentation to Local Clone"
                sh "mkdir --parents ./$documentationDir/$exportDirInRepo"
                sh "mv ./$docsOutDir/* ./$documentationDir/$exportDirInRepo/"

                echo "Committing and Pushing to the repository"
                dir(documentationDir) {
                    sshagent([gitSshCredentialsId]) {
                        sh "git config user.name $botUsername"
                        sh "git config user.email $botEmail"
                        sh 'git add -A'
                        //Either nothing to commit, or we create a commit
                        sh "git diff-index --quiet HEAD || git commit -m 'CI Doc export for Botania build ${env.BRANCH_NAME}-${env.BUILD_NUMBER}\n\nMatches git commit ${env.GIT_COMMIT}'"
                        sh "git push origin main"
                    }
                }
            }
        }
	*/
    }
    post {
        always {
            archiveArtifacts artifacts: 'Forge/build/libs/*.jar, Fabric/build/libs/*.jar'
        }
    }
}
