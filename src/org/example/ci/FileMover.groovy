package org.example.ci

class FileMover implements Serializable {
    def script

    FileMover(script) {
        this.script = script
    }

    def moveStuff() {
        script.echo "Starting file moving process...
    }
}
