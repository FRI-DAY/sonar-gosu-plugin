# SonarQube Gosu Plugin
[![.github/workflows/build.yml](https://github.com/FRI-DAY/sonar-gosu-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/FRI-DAY/sonar-gosu-plugin/actions/workflows/build.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=FRI-DAY_sonar-gosu-plugin&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=FRI-DAY_sonar-gosu-plugin) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=FRI-DAY_sonar-gosu-plugin&metric=coverage)](https://sonarcloud.io/summary/new_code?id=FRI-DAY_sonar-gosu-plugin) 

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
| v8.9              | v1.0.0, v1.1.0 |

## Installation

### Sonarqube Marketplace

Not yet available.

### Sonarqube On-premise

Download the [latest](https://github.com/FRI-DAY/sonar-gosu-plugin/releases) JAR file and put it into Sonarqube's plugin directory (`./extensions/plugins`). 
After restarting the server, a new Quality Profile for Gosu with all the plugin rules should be available on `Quality Profiles`.

Alternatively, you can clone the project and build the JAR file from the sources. E.g.: 
```shell
$ ./gradlew shadowJar
```

The installation procedure is the same as mentioned previously. 

## Want to contribute?

Check the [contributing](CONTRIBUTING.md) guidelines.

## Changelog

All releases are available in the [Releases](https://github.com/FRI-DAY/sonar-gosu-plugin/releases) section.

## License
Copyright (C) 2023 FRIDAY Insurance S.A.

The SonarQube Plugin for Gosu Programming Language is released under the [GNU AGPL License, Version 3.0](https://www.gnu.org/licenses/agpl-3.0.en.html).
