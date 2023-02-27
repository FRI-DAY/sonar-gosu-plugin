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
package de.friday.test.support.checks.dsl.gosu;

import de.friday.test.support.TestResourcesDirectories;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

final class GosuCheckTestResources {

    static final Path BASE_DIR = TestResourcesDirectories.CHECK_RESOURCES_DIR.getPath();

    static Path getBaseDir() {
        return BASE_DIR;
    }

    static Path getPathOf(String gosuSourceFileName) {
        return getPathOf(gosuSourceFileName, BASE_DIR.toFile().getPath());
    }

    static Path getPathOf(String gosuSourceFileName, String baseDir) {
        return Paths.get(baseDir + File.separator + gosuSourceFileName);
    }

    static String getBaseDirPathAsString() {
        return BASE_DIR.toFile().getPath();
    }

}
