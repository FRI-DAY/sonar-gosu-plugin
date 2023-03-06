# SonarQube Gosu Plugin

The SonarQube Gosu Plugin supports the following features:

- Code Metrics (Cognitive and Cyclomatic Complexity, Lines of Code, Code Duplications);
- Code Coverage;
- Test Reports;
- Syntax highlighting;
- Suppress Warnings.

## Plugin Properties

The plugin provides that following properties that can configured through project properties:

| Property                   | Description                                  | Default Value                                    |
|----------------------------|----------------------------------------------|--------------------------------------------------|
| `sonar.gosu.file.suffixes` | Comma separated list of Gosu file suffixes.  | `.gs,.gsx`                                       |
| `sonar.gosu.reportPaths`   | Comma separated list of JUnit reports paths. | `modules/configuration/build/test-results/test/` |

The properties can be used as follows: `-Dsonar.jacoco.reportPath=build/jacoco/coverage.exec`.

## Suppress Warnings

The plugin supports warning suppressing for the following Gosu types:
- `class`;
- `enhancement`;
- `enum`;
- `interface`;
- `annotation`;
- `constructor`;
- `function`;
- `field`;
- `property`;

To suppress warnings from Sonarqube Gosu Plugin Rules, use the following syntax `@SuppressWarnings("gosu:RULE_KEY")`. 
This will suppress any issue found by the specified rule during the code analysis. 

Check the table bellow for other warning suppressing configurations:

| Suppress Key       | Description                                 | Example                                     |
|--------------------|---------------------------------------------|---------------------------------------------|
| Rule Key           | Suppress warnings for given rule            | `@SuppressWarnings("gosu:TODOsRule")`       |
| `all`              | Suppress warnings for all rules             | `@SuppressWarnings("gosu:all")`             |
| `metrics`          | Suppress warnings for metrics rules         | `@SuppressWarnings("gosu:metrics")`         |
| `code_smells`      | Suppress warnings for code smells rules     | `@SuppressWarnings("gosu:code_smells")`     |
| `vulnerabilities`  | Suppress warnings for vulnerabilities rules | `@SuppressWarnings("gosu:vulnerabilities")` |
| `bugs`             | Suppress warnings for bugs rules            | `@SuppressWarnings("gosu:bugs")`            |

## Rules

A list of all the rules included on the plugin is available [here](RULES.md).
