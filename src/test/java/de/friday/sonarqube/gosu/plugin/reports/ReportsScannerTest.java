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
import org.sonar.api.config.Configuration;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.plugins.surefire.data.UnitTestIndex;
import static org.assertj.core.api.Assertions.assertThat;

class ReportsScannerTest {

    @Test
    void shouldCreateUnitTestIndexWithTestReports() {
        // given
        final Configuration settings = new MapSettings().asConfig();
        final DefaultFileSystem fileSystem = new DefaultFileSystem(new File("src/test/resources/reports/scanner/"));
        final PathResolver pathResolver = new PathResolver();
        final List<File> reportsDirectories = new ReportsDirectories(settings, fileSystem, pathResolver).get();

        // when
        final UnitTestIndex index = new ReportsScanner(settings).createIndex(reportsDirectories);

        // then
        assertThat(index.size()).isEqualTo(5);
        assertThat(index.getClassnames()).containsExactlyInAnyOrder(
                "de.friday.suite.util.StringUtilTest",
                "de.friday.ab.vendorservices.exportimport.import.excel.enhancement.ExcelRowEnhancementTest",
                "de.friday.suite.util.validation.iban.IbanValidatorTest",
                "de.friday.suite.util.XMLUtilTest",
                "de.friday.suite.util.DateEnhancementTest"
        );
    }

}
