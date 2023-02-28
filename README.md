
# SonarQube Gosu Plugin
[![.github/workflows/build.yml](https://github.com/FRI-DAY/sonar-gosu-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/FRI-DAY/sonar-gosu-plugin/actions/workflows/build.yml)

[Gosu Programming Language](https://gosu-lang.github.io/) Plugin for SonarQube.

## Why ANTLR4?

Gosu official grammar can be found [here](https://gosu-lang.github.io/grammar.html). It was written in version 3 of [ANTLR (Another Tool For Language Recognition)](https://www.antlr.org/). Despite this, we decided to write our own grammar in ANTLR4.

The official Gosu grammar has some pitfalls, it is old and written in a not supported version of ANTLR. In the long run, it was easier to write own simplev grammar rather than working with an undocumented, unmodifiable and not fully working parser.

### Working with ANTLR4

**The Gosu Grammar should not be modified**. However, it can happen that there are bugs in the grammar or a new release of the Gosu language contains new features.

In such cases, there is a very useful tool called [ANTLR v4](https://github.com/antlr/intellij-plugin-v4/blob/master/README.md) plugin for IntelliJ IDEA. With it, you can verify the parse tree built by ANTLR.

## Getting Started (TODO)

### Writing custom checks (TODO)

## Compatibility
Please find below the compatibility matrix of the plugin:

| SonarQube Version | Plugin Version | Gosu Version  |
|-------------------|----------------|---------------|
| 8.9+              | 1.0.0          | 1+            |  


## How to contribute? (TODO)

## Release Notes (TODO)

## Continuous Integration Builds (TODO)

## License
Copyright (C) 2023 FRIDAY Insurance S.A.

The SonarQube Plugin for Gosu Programming Language is released under the [GNU AGPL License, Version 3.0](https://www.gnu.org/licenses/agpl-3.0.en.html).
