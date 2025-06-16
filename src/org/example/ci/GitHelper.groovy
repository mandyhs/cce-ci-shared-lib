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
                script.echo "Checking out ${repo} on branch ${branch}"
                /*script.checkout([$class: 'GitSCM',
                    branches: [[name: branch]],
                    userRemoteConfigs: [[url: repo]]
                ])*/

                script.checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], 
                  extensions: [[$class: 'LocalBranch', localBranch: "${branch}"],[$class: 'GitLFSPull'],
                  [$class: 'DisableRemotePoll'],
                  [$class: 'CheckoutOption', timeout: 30], 
                  [$class: 'CloneOption', noTags: false, reference: '', shallow: false, timeout: 30],
                  [$class: 'CleanCheckout', deleteUntrackedNestedRepositories: true], [$class: 'CleanBeforeCheckout', deleteUntrackedNestedRepositories: true],  
                  [$class: 'RelativeTargetDirectory', relativeTargetDir: 'ice_source']], 
                  gitTool: 'jgit', userRemoteConfigs: [[credentialsId: '746ebc8c-18cd-43aa-a425-a14134b6beee', url: repo]],
                  changelog: true, poll: true
                ])
            }
        }
    }
}
