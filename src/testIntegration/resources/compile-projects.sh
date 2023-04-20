#!/bin/bash -e
#
# Copyright (C) 2023 FRIDAY Insurance S.A.
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published
# by the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program. If not, see <https://www.gnu.org/licenses/>.
#


for project in projects/*
do
    if [ -e "$project/build.gradle" ]; then
      echo "Building $project Using Gradle"
      (cd "$project" || exit; ./gradlew build --build-cache --no-daemon)
    elif [ -e "$project/pom.xml" ]; then
      echo "Building $project using Maven"
      (cd "$project" || exit; ./mvmw clean install)
    else
      echo "Project not supported"
    fi
done
