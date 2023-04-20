/*
 * Copyright (C) 2023 FRIDAY Insurance S.A.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.friday.test.config;

import com.sonar.orchestrator.build.GradleBuild;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum Projects {
    SIMPLE_GOSU_PROJECT("simple-gosu-project", "projects/simple-gosu-project");

    private static final String TEST_INTEGRATION_RESOURCES = "src/testIntegration/resources/";
    private final String projectKey;
    private final String projectDirectory;

    Projects(String projectKey, String projectDirectory) {
        this.projectKey = projectKey;
        this.projectDirectory = projectDirectory;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public File getRootDirectory() {
        return getPath().toFile();
    }

    public Path getPath() {
        return Paths.get(TEST_INTEGRATION_RESOURCES + projectDirectory);
    }

    public GradleBuild asGradleBuild() {
        return GradleBuild
                .create(getRootDirectory())
                .setProperty("sonar.projectKey", this.projectKey)
                .setTasks("sonar");
    }

    public String getMainComponentKeyOf(String component) {
        return getComponentKeyOf(component, "main");
    }

    private String getComponentKeyOf(String component, String scope) {
        return this.projectKey + ":" + "src/" + scope.toLowerCase() + "/" + component;
    }
}
