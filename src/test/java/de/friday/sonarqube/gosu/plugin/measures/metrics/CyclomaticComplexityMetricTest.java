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
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import static de.friday.test.support.TestFileComponentKeyBuilder.samplesComponentKeyOf;
import static de.friday.test.support.TestResourcesDirectories.SAMPLES_RESOURCES_DIR;
import static org.assertj.core.api.Assertions.assertThat;

class CyclomaticComplexityMetricTest {

    @Test
    void shouldSaveCyclomaticComplexityOnSensorContextWhenFileHasMainScope() {
        // given
        final String gosuFileName = "SpaceMarine.gs";
        final GosuTestFileParser parser = new GosuTestFileParser(gosuFileName);
        final String componentKey = samplesComponentKeyOf(gosuFileName);

        // when
        final GosuTestFileParser.GosuFileParsed fileParsed = parser.parseWithSensorContext(SAMPLES_RESOURCES_DIR, "CyclomaticComplexityMetricTest");
        final SensorContextTester sensor = fileParsed.getSensorContext();

        // then
        assertThat(sensor.measure(componentKey, CoreMetrics.COMPLEXITY).value()).isEqualTo(1);
    }

    @Test
    void shouldNotCyclomaticComplexityOnSensorContextWhenFileHasTestScope() {
        // given
        final String gosuFileName = "SpaceMarineTest.gs";
        final GosuTestFileParser parser = new GosuTestFileParser(gosuFileName, InputFile.Type.TEST);
        final String componentKey = samplesComponentKeyOf(gosuFileName);

        // when
        final GosuTestFileParser.GosuFileParsed fileParsed = parser.parseWithSensorContext(SAMPLES_RESOURCES_DIR, "CyclomaticComplexityMetricTest");
        final SensorContextTester sensor = fileParsed.getSensorContext();

        // then
        assertThat(sensor.measure(componentKey, CoreMetrics.COMPLEXITY)).isNull();
    }

}
