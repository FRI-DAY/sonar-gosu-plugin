## Releasing

These are the general steps to release a new version of the plugin:

1. Remove the `SNAPSHOT` suffix from plugin version number on the `gradle.properties`.
2. Update the `CHANGELOG.md` by running the `./gradlew patchChangelog` task. The task will automatically move all
the unreleased changes notes to the soon-to-be released version and create a new change note section for new unreleased changes.
3. Open a pull request with the changes.
4. Once the pull request is merged the release workflow will build the release version, create the tag and create a draft [Github release](https://docs.github.com/en/repositories/releasing-projects-on-github/about-releases) with the plugin JAR and change notes.
5. Review the draft Github release and, if everything is ok, release it.
6. All done :-). 
