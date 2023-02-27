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
package de.friday.sonarqube.gosu.plugin.measures.metrics;

import de.friday.test.support.GosuTestFileParser;
import de.friday.test.support.GosuTestFileParser.GosuFileParsed;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import static de.friday.test.support.TestResourcesDirectories.METRICS_RESOURCES_DIR;
import static org.assertj.core.api.Assertions.assertThat;

public class TestsMetricTest {

    @Test
    void shouldSaveCoreTestMetricsOnSensorContext() {
        // given
        final String unitTestSourceFilename = "TestsMetricTest/StringUtilTest.gs";
        final GosuTestFileParser parser = new GosuTestFileParser(unitTestSourceFilename);
        final String key = unitTextKeyOf(unitTestSourceFilename);

        // when
        final GosuFileParsed fileParsed = parser.parseWithSensorContext(METRICS_RESOURCES_DIR, "TestsMetricTest");
        final SensorContextTester sensor = fileParsed.getSensorContext();

        // then
        assertThat(sensor.measure(key, CoreMetrics.SKIPPED_TESTS).value()).isZero();
        assertThat(sensor.measure(key, CoreMetrics.TESTS).value()).isEqualTo(6);
        assertThat(sensor.measure(key, CoreMetrics.TEST_ERRORS).value()).isZero();
        assertThat(sensor.measure(key, CoreMetrics.TEST_FAILURES).value()).isZero();
        assertThat(sensor.measure(key, CoreMetrics.TEST_EXECUTION_TIME).value()).isEqualTo(6);
    }

    @Test
    void shouldNotSaveTestMetricsOnSensorContextWhenTestReportIsNotAvailable() {
        // given
        final String unitTestSourceFilename = "TestsMetricTest/TestWithoutReport.gs";
        final GosuTestFileParser parser = new GosuTestFileParser(unitTestSourceFilename);
        final String key = unitTextKeyOf(unitTestSourceFilename);

        // when
        final GosuFileParsed fileParsed = parser.parseWithSensorContext(METRICS_RESOURCES_DIR, "TestWithoutReport");
        final SensorContextTester sensor = fileParsed.getSensorContext();

        // then
        assertThat(sensor.measure(key, CoreMetrics.SKIPPED_TESTS)).isNull();
        assertThat(sensor.measure(key, CoreMetrics.TESTS)).isNull();
        assertThat(sensor.measure(key, CoreMetrics.TEST_ERRORS)).isNull();
        assertThat(sensor.measure(key, CoreMetrics.TEST_FAILURES)).isNull();
        assertThat(sensor.measure(key, CoreMetrics.TEST_EXECUTION_TIME)).isNull();
    }

    private String unitTextKeyOf(String testSourceFileName) {
        return METRICS_RESOURCES_DIR.getPath() + ":" + testSourceFileName;
    }
}
