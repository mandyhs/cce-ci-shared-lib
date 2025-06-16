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
            def repoBranch = repo.branch ?: 'main' // 默認分支為 'main'
            def repoTargetDir = repo.targetDir // 目標資料夾可能為 null
            def credentialsId = repo.credentialsId // 憑證 ID 可能為 null

            if (!repoUrl) {
                script.echo "Skipping repository ${index + 1}: URL is missing."
                return // 跳過當前循環
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

            // 如果指定了目標資料夾，則使用 dir 步驟
            if (repoTargetDir) {
                script.echo "Creating/entering directory: ${repoTargetDir}"
                script.dir(repoTargetDir) { // 進入指定資料夾
                    script.checkout(gitCommandParams)
                }
            } else {
                // 如果沒有指定目標資料夾，直接在當前目錄 (通常是工作空間根目錄) 下克隆
                // GitSCM 預設會以倉庫名稱創建資料夾
                script.checkout(gitCommandParams)
            }
            script.echo "Repository ${repoUrl} checked out successfully."
        }
        script.echo "All specified repositories processed."
    }