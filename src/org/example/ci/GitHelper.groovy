package org.example.ci
import java.io.Serializable

class GitHelper implements Serializable {
    private script

    GitHelper(script) {
        this.script = script
    }


    def checkoutRepos(List<Map> reposConfig) {
        script.echo "Starting multi-repository checkout..."

        if (!reposConfig) {
            script.echo "No repositories configured for checkout. Skipping."
            return
        }

        reposConfig.eachWithIndex { repo, index ->
            script.echo "--- Processing Repository ${index + 1} ---"
            def repoUrl = repo.url
            def repoBranch = repo.branch ?: 'master' 
            def repoTargetDir = repo.targetDir
            def credentialsId = repo.credentialsId
            def needPoll = repo.need_poll ?: false

            if (!repoUrl) {
                script.echo "Skipping repository ${index + 1}: URL is missing."
                return
            }

            script.echo "Repository URL: ${repoUrl}"
            script.echo "Branch: ${repoBranch}"
            script.echo "Target Directory: ${repoTargetDir ?: 'default (repo name)'}"
            script.echo "Credentials ID: ${credentialsId ?: 'None'}"

            def gitCommandParams = [
                $class: 'GitSCM',
                branches: [[name: repoBranch]],
                userRemoteConfigs: [[url: repoUrl]]
            ]

            // 如果指定了憑證，則加入憑證 ID
            if (credentialsId) {
                gitCommandParams.userRemoteConfigs[0].credentialsId = credentialsId
            }

            gitCommandParams.extensions = [
                [$class: 'GitLFSPull'],
                [$class: 'DisableRemotePoll'],
                [$class: 'CheckoutOption', timeout: 30], 
                [$class: 'CloneOption', noTags: false, reference: '', shallow: false, timeout: 30],
                [$class: 'CleanCheckout', deleteUntrackedNestedRepositories: true],
                [$class: 'CleanBeforeCheckout', deleteUntrackedNestedRepositories: true],  
                [$class: 'RelativeTargetDirectory', relativeTargetDir: repoTargetDir]
            ]

            gitCommandParams.gitTool = 'jgit'
            gitCommandParams.changelog = needPoll
            gitCommandParams.poll = needPoll

            script.checkout(gitCommandParams)

            script.echo "Repository ${repoUrl} checked out successfully."
        }
        script.echo "All specified repositories processed."
    }
}