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
package de.friday.sonarqube.gosu.plugin.reports;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Configuration;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public final class ReportsDirectories {

    private static final Logger LOGGER = Loggers.get(ReportsDirectories.class);
    private static final String DEFAULT_REPORT_PATH = "modules/configuration/build/test-results/test/";
    public static final String REPORT_PATHS_PROPERTY = "sonar.gosu.reportPaths";
    private final Configuration settings;
    private final FileSystem fileSystem;
    private final PathResolver pathResolver;

    public ReportsDirectories(Configuration settings, FileSystem fileSystem, PathResolver pathResolver) {
        this.settings = settings;
        this.fileSystem = fileSystem;
        this.pathResolver = pathResolver;
    }

    public List<File> get() {
        final List<File> customReportDirectories = getReportsDirectoriesFromProperty();

        if (customReportDirectories.isEmpty()) {
            return getDefaultReportDirectory();
        }

        return customReportDirectories;
    }

    private List<File> getDefaultReportDirectory() {
        return Collections.singletonList(new File(fileSystem.baseDir(), DEFAULT_REPORT_PATH));
    }

    private List<File> getReportsDirectoriesFromProperty() {
        if (settings.hasKey(REPORT_PATHS_PROPERTY)) {
            return Arrays.stream(settings.getStringArray(REPORT_PATHS_PROPERTY))
                    .map(String::trim)
                    .map(path -> getFileFromPath(fileSystem, pathResolver, path))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @CheckForNull
    private static File getFileFromPath(FileSystem fs, PathResolver pathResolver, String path) {
        try {
            return pathResolver.relativeFile(fs.baseDir(), path);
        } catch (Exception e) {
            /*
             * from SonarSource:
             * exceptions on file not found was only occurring with SQ 5.6 LTS, not with SQ 6.4
             */
            LOGGER.info("Test report path: {}/{} not found.", fs.baseDir(), path);
        }
        return null;
    }
}
