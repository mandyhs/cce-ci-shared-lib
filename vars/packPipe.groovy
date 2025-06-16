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
        
        stage('Build') {
                echo "Doing your custom build for ${params.TARGET}"
        }  


}
