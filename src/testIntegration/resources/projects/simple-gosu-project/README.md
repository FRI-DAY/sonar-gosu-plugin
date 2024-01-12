# Simple Gosu Project

Simple project, using Gosu, for integration tests purposes.

## Requirements

- JDK 11

## Building

On the project root folder run:
```shell
  ./gradlew build
```

## Issues

The project contains the following issues:

| Type       | Rule Key                     | Quantity |
|------------|------------------------------|----------|
| Code Smell | `gosu:TODOsRule`             | *1*      |
| Code Smell | `gosu:MagicNumbersRule`      | *3*      |
| Code Smell | `gosu:UnnecessaryImportRule` | *6*      |

## Sonar

### Running Scanner with Gradle

On the project root folder run:
```shell
  ./gradlew sonar -Dsonar.host.url="[SONAR HOST URL]" -Dsonar.login="[AUTHORIZATION TOKEN]"
```

### Running Scanner with Docker

On the project root folder run:
```shell
$ docker run \
    --network=host \
    --rm \
    -e SONAR_HOST_URL=[SONAR HOST URL] \
    -e SONAR_LOGIN=[AUTHORIZATION TOKEN] \
    -v "$(pwd):/usr/src" \
    sonarsource/sonar-scanner-cli -X
```


