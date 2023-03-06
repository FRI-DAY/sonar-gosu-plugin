#!/bin/sh

BASEDIR="$(dirname "$0")"
PROJECT_DIR="$(dirname `pwd`)"

echo "------ BUILDING SONAR GOSU PLUGIN ------"
(cd "$PROJECT_DIR" || exit; ./gradlew shadowJar)
echo "----------------------------------------"

echo "------ BUILDING CUSTOM SONAR SERVER DOCKER IMAGE ------"
(cd "$PROJECT_DIR" || exit; docker build -f docker/Dockerfile -t "friday:sonar-server" .)
echo "-------------------------------------------------------"

echo "------ STARTING ------"
docker-compose up
echo "----------------------"
