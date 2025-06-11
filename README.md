
# Jenkins Shared Library Template

## Usage

1. Add this repo as a global pipeline library in Jenkins configuration with the name `jenkins-shared-lib`.

2. In your Jenkinsfile, use:

```groovy
@Library('jenkins-shared-lib') _

ciPipeline(
    repos: [
        'https://github.com/your-org/repo-a.git',
        'https://github.com/your-org/repo-b.git'
    ],
    branch: 'main'
)
```

3. Customize the repos and branch as needed.

## Structure

- `vars/ciPipeline.groovy`: Pipeline template entry point.
- `src/org/example/ci/`: Helper classes for git checkout, file moving, and report generation.
