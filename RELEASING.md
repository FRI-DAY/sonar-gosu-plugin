## Releasing

These are the general steps to release a new version of the plugin:

1. Remove the `SNAPSHOT` suffix from plugin version number on the `gradle.properties`.
2. Update the `CHANGELOG.md` by running the `./gradlew patchChangelog` task. The task will automatically move all
the unreleased changes notes to the soon-to-be released version and create a new change note section for new unreleased changes.
3. Open a pull request with the changes.
4. Once the pull request is merged the release workflow will build the release version, create the tag and create a draft [Github release](https://docs.github.com/en/repositories/releasing-projects-on-github/about-releases) with the plugin JAR and change notes.
5. Review the draft GitHub release and, if everything is ok, release it.

### Sonarqube Marketplace

After a new version is released, we need to [announce](https://community.sonarsource.com/t/deploying-to-the-marketplace/35236#announcing-new-releases-2) it on Sonarqube Marketplace.

Follow the steps bellow to publish the new release on Sonarqube Marketplace:

1. Fork [sonar-update-center.properties](https://github.com/SonarSource/sonar-update-center-properties) repository;
2. Add the new version of the plugin on the [communitygosu.properties](https://github.com/SonarSource/sonar-update-center-properties/blob/master/communitygosu.properties);
3. Create a pull request with your changes. [Example](https://github.com/SonarSource/sonar-update-center-properties/pull/490);
4. Once a SonarSource representative review and approve your pull request, it will be automatically published to the marketplace;
5. All done :-).
