package org.example.ci

class ReportGenerator implements Serializable {
    def script

    ReportGenerator(script) {
        this.script = script
    }

    def generateTxtReport(params, List<Map> reposConfig) {
        script.echo "Generating CI report..."
        if (!params || !reposConfig) {
            script.error "Parameters or repository configuration is missing. Cannot generate report."
            return
        }
        def commits = []
        reposConfig.eachWithIndex { repo, index ->
            script.echo "--- Processing Repository ${index + 1} ---"
            def repoTargetDir = repo.targetDir
            script.dir(repoTargetDir) {
                def commit = script.powershell(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                commits.add("Repository ${index + 1} (${repo.url}): ${commit}")
                script.echo "Current commit for ${repoTargetDir}: ${commit}"
            }
        }

        
        def content = """
        CI Report
        --------------------------
        Build #: ${script.env.BUILD_NUMBER}
        Branch : ${script.env.BRANCH_NAME ?: 'N/A'}
        Commit : ${commits.join('\n')}
        Target : ${params.TARGET}
        Env    : ${params.ENVIRONMENT}
        Result : ${script.currentBuild.currentResult}
        Time   : ${new Date().format("yyyy-MM-dd HH:mm:ss")}
        --------------------------
        """.stripIndent()

        script.writeFile file: 'ci_report.txt', text: content
    }
}
