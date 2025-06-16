def call(Map config = [:]) {
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

            stage('Generate Report and Archive') {
                steps {
                    script {
                        // 確保 org.example.ci.ReportGenerator 類存在於你的共享庫的 'src/' 目錄中
                        def reportGen = new org.example.ci.ReportGenerator(this)
                        reportGen.generateTxtReport(params) // 傳遞 params 給報告生成器

                        // archiveArtifacts 是一個標準 Jenkins 步驟，可以在 steps 區塊內使用
                        archiveArtifacts artifacts: 'ci_report.txt', fingerprint: true
                    }
                }
            }
        }
}
