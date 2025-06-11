package org.example.ci

class GitHelper implements Serializable {
    def script

    GitHelper(script) {
        this.script = script
    }

    def checkoutRepos(List repos, String branch) {
        repos.each { repo ->
            def dirName = repo.tokenize('/').last().replaceAll('.git$', '')
            script.dir(dirName) {
                script.checkout([$class: 'GitSCM',
                    branches: [[name: branch]],
                    userRemoteConfigs: [[url: repo]]
                ])
            }
        }
    }
}
