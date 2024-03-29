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
package de.friday.test.support;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum TestResourcesDirectories {

    RESOURCES_DIR("src/test/resources"),
    RULES_RESOURCES_DIR("src/test/resources/rules"),
    METRICS_RESOURCES_DIR("src/test/resources/measures/metrics"),
    SAMPLES_RESOURCES_DIR("src/test/resources/samples"),
    SENSOR_RESOURCES_DIR("src/test/resources/sensor");

    private final String path;

    TestResourcesDirectories(String path) {
        this.path = path;
    }

    public String getPathAsString() {
        return path;
    }

    public Path getPath() {
        return Paths.get(this.getPathAsString());
    }
}
