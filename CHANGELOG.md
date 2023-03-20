# Sonarqube Gosu Plugin
All notable changes to this project will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed
- Refactored TooManyParamsRule to avoid unnecessary `if` condition ([#45](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/45)).
- Only compute core metrics (Lines of code, cognitive/cyclomatic complexity) for **main** source code files ([#54](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/54)).

### Fixed
- Fixed Lines of Code metric ([#54](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/54)).
- Fixed all minor SonarCloud issues:
  * [#46](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/46)
  * [#49](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/49)
  * [#50](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/50)

## [1.0.0]

### Added
- Added Sonar Gosu Plugin documentation ([#27](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/27)).
- Added Gosu Rules for code smells ([#15](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/15)).
- Added Gosu Rules for bugs ([#13](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/13)).
- Added Gosu Rules for vulnerabilities ([#13](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/13)).
- Added Gosu Rules for code metrics ([#11](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/11)).
- Added DSL for Gosu Rule tests [(#10)](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/10).
- Added Sonar Gosu Plugin structure;

### Changed
- Renamed `checks` to `rules` [(#34)](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/34).
- Changed plugin key to `communityGosuPlugin` [(#36)](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/36).

### Fixed
- Fixed false positives on UnnecessaryImportRule [(#33)](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/33).
- Fixed NPE on UnnecessaryImportRule [(#31)](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/31).
- Fixed NPE on InternalImportsRule [(#29)](https://github.com/FRI-DAY/sonar-gosu-plugin/pull/29);

[Unreleased]: https://github.com/FRI-DAY/sonar-gosu-plugin/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/FRI-DAY/sonar-gosu-plugin/commits/v1.0.0
