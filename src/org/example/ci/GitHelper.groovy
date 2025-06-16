package org.example.ci

class GitHelper implements Serializable {
    def script

    GitHelper(script) {
        this.script = script
    }

    def checkoutRepos(List repos, String branch) {
        echo "Checking out repositories on branch ${branch}: ${repos.join(', ')}"
        if (repos.isEmpty()) {
            echo "No repositories provided to checkout."
            return
        }
        repos.each { repo ->
            def dirName = repo.tokenize('/').last().replaceAll('.git$', '')
            script.dir(dirName) {
                echo "Checking out ${repo} on branch ${branch}"
                script.checkout([$class: 'GitSCM',
                    branches: [[name: branch]],
                    userRemoteConfigs: [[url: repo]]
                ])
            }
        }
    }
}
