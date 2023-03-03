# SonarQube Gosu Plugin
[![.github/workflows/build.yml](https://github.com/FRI-DAY/sonar-gosu-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/FRI-DAY/sonar-gosu-plugin/actions/workflows/build.yml)

[Gosu Programming Language](https://gosu-lang.github.io/) Plugin for SonarQube.

For plugin configurations and a list of all coding rules included in the plugin, see: [plugin documentation](docs/README.md).

## Why ANTLR4?

The Sonarqube Gosu Plugin uses [ANTLR (Another Tool For Language Recognition)](https://www.antlr.org/) to execute static
analysis of Gosu code. 
We decided to write our own grammar in ANTLR4 due the [official Gosu grammar](https://gosu-lang.github.io/grammar.html)
been written in an old and non-supported version of ANTLR.
In the long run, it was easier to write our own simple grammar rather than working with an undocumented, unmodifiable and not fully working parser.

## Compatibility
Please find below the compatibility matrix of the plugin:

| SonarQube Version | Plugin Version |
|-------------------|----------------|
| v8.9 or earlier   | v1.0.0         |

## Installation

### Sonarcloud

Not yet supported.

### Self-hosted Sonarqube

Download the [latest](https://github.com/FRI-DAY/sonar-gosu-plugin/releases) JAR file and put it into Sonarqube's plugin directory (`./extensions/plugins`). 
After restarting the server the plugin should be available under `Quality Profiles`.

Alternatively, you can clone the project and build the JAR file from the sources. E.g.: `./gradlew shadowJar`

## Want to contribute?

Check the [contributing](CONTRIBUTING.md) guidelines.

## Changelog

All releases are available in the [Releases](https://github.com/FRI-DAY/sonar-gosu-plugin/releases) section.

## License
Copyright (C) 2023 FRIDAY Insurance S.A.

The SonarQube Plugin for Gosu Programming Language is released under the [GNU AGPL License, Version 3.0](https://www.gnu.org/licenses/agpl-3.0.en.html).
