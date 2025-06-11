package org.example.ci

class FileMover implements Serializable {
    def script

    FileMover(script) {
        this.script = script
    }

    def moveStuff() {
        script.sh '''
        mkdir -p workspace/merged
        cp */build/output/*.jar workspace/merged/ || true
        '''
    }
}
