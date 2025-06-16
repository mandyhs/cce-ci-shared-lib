def call(Map config = [:]) {
        stage('Checkout Repos') {
                script {
                    //def repos = config.get('repos', [])
                    //def branch = config.get('branch', 'main')
                    def gitHelper = new org.example.ci.GitHelper(this)
                    gitHelper.checkoutRepos(config.get('repos', []))
                    //echo "Checked out repositories: ${repos.join(', ')} on branch: ${branch}"
                }
        }

        stage('Move Files') {
                script {
                    def mover = new org.example.ci.FileMover(this)
                    mover.moveStuff()
                }
        }

        stage('Build') {
                echo "Doing your custom build for ${params.TARGET}"
        }  

        stage('Generate Report and Archive') {
                script {
                    def reportGen = new org.example.ci.ReportGenerator(this)
                    reportGen.generateTxtReport(params, config.get('repos', [])) // 傳遞 params 給報告生成器
                    archiveArtifacts artifacts: 'ci_report.txt', fingerprint: true
                }
        }
}
