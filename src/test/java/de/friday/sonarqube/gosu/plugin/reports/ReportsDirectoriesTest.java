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
import java.util.List;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.scan.filesystem.PathResolver;
import static org.assertj.core.api.Assertions.assertThat;

class ReportsDirectoriesTest {

    @Test
    void shouldReturnReportDirectoriesConfiguredOnSettings() {
        final MapSettings settings = new MapSettings();
        settings.setProperty("sonar.gosu.reportPaths", "targ/surefire,submodule/targ/surefire");

        final DefaultFileSystem fs = aFileSystemFor("src/test/resources/reports/directories/shouldGetReportsPathFromProperty");
        final PathResolver pathResolver = new PathResolver();

        final List<File> directories = new ReportsDirectories(settings.asConfig(), fs, pathResolver).get();

        assertThat(directories).hasSize(2).allSatisfy(
                file -> assertThat(file).exists().isDirectory()
        );
    }

    @Test
    void shouldReturnDefaultReportDirectories() throws Exception {
        // given
        final MapSettings emptySettings = new MapSettings();
        final DefaultFileSystem fs = aFileSystemFor("src/test/resources/reports/directories");
        final PathResolver pathResolver = new PathResolver();

        // when
        final List<File> directories = new ReportsDirectories(emptySettings.asConfig(), fs, pathResolver).get();

        // then
        assertThat(directories).hasSize(1).first().satisfies(
                file -> {
                    assertThat(file).exists().isDirectory();
                    assertThat(file.getCanonicalPath()).endsWith("test-results" + File.separator + "test");
                }
        );
    }

    private DefaultFileSystem aFileSystemFor(String directoryPath) {
        return new DefaultFileSystem(new File(directoryPath));
    }
}
