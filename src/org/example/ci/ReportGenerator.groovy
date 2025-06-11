package org.example.ci

class ReportGenerator implements Serializable {
    def script

    ReportGenerator(script) {
        this.script = script
    }

    def generateTxtReport(params) {
        def commit = script.sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
        def content = """
        CI Report
        --------------------------
        Build #: ${script.env.BUILD_NUMBER}
        Branch : ${script.env.BRANCH_NAME ?: 'N/A'}
        Commit : ${commit}
        Target : ${params.TARGET}
        Env    : ${params.ENVIRONMENT}
        Result : ${script.currentBuild.currentResult}
        Time   : ${new Date().format("yyyy-MM-dd HH:mm:ss")}
        --------------------------
        """.stripIndent()

        script.writeFile file: 'ci_report.txt', text: content
    }
}
