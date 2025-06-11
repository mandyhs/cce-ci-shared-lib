def call(Map config = [:]) {
    pipeline {
        agent any

        parameters {
            string(name: 'TARGET', defaultValue: 'World', description: 'Who to greet')
            string(name: 'ENVIRONMENT', defaultValue: 'staging', description: 'Deployment environment')
        }

        stages {
            stage('Checkout Repos') {
                steps {
                    script {
                        def repos = config.get('repos', [])
                        def branch = config.get('branch', 'main')
                        def gitHelper = new org.example.ci.GitHelper(this)
                        gitHelper.checkoutRepos(repos, branch)
                    }
                }
            }

            stage('Move Files') {
                steps {
                    script {
                        def mover = new org.example.ci.FileMover(this)
                        mover.moveStuff()
                    }
                }
            }

            stage('Build') {
                steps {
                    echo "Doing your custom build for ${params.TARGET}"
                }
            }
        }

        post {
            always {
                script {
                    def reportGen = new org.example.ci.ReportGenerator(this)
                    reportGen.generateTxtReport(params)
                    archiveArtifacts artifacts: 'ci_report.txt', fingerprint: true
                }
            }
        }
    }
}
