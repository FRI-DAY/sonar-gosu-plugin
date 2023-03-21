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
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import static de.friday.test.support.TestFileComponentKeyBuilder.samplesComponentKeyOf;
import static de.friday.test.support.TestResourcesDirectories.SAMPLES_RESOURCES_DIR;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CodeSizeMetricsTest {

    @ParameterizedTest
    @MethodSource("getGosuSourceFiles")
    void shouldSaveLinesOfCodeMetricOnSensorContextWhenFileHasMainScope(
            String gosuFileName,
            int expectedNumberOfClasses,
            int expectedNumberOfFunctions,
            int expectedNumberOfStatements
    ) {
        // given
        final GosuTestFileParser parser = new GosuTestFileParser(gosuFileName);
        final String componentKey = samplesComponentKeyOf(gosuFileName);

        // when
        final GosuTestFileParser.GosuFileParsed fileParsed = parser.parseWithSensorContext(SAMPLES_RESOURCES_DIR, "CodeSizeMetricsTest");
        final SensorContextTester sensor = fileParsed.getSensorContext();

        // then
        assertThat(sensor.measure(componentKey, CoreMetrics.CLASSES).value()).as("Number of classes")
                .isEqualTo(expectedNumberOfClasses);
        assertThat(sensor.measure(componentKey, CoreMetrics.FUNCTIONS).value()).as("Number of functions")
                .isEqualTo(expectedNumberOfFunctions);
        assertThat(sensor.measure(componentKey, CoreMetrics.STATEMENTS).value()).as("Number of Statements")
                .isEqualTo(expectedNumberOfStatements);
    }

    private Stream<Arguments> getGosuSourceFiles() {
        return Stream.of(
                Arguments.of("Foo.gs", 1, 1, 1),
                Arguments.of("FooEnhancement.gsx", 0, 1, 1),
                Arguments.of("SpaceMarine.gs", 1, 1, 1),
                Arguments.of("ComplexClass.gs", 2, 3, 13)
        );
    }

    @Test
    void shouldNotSaveLinesOfCodeMetricOnSensorContextWhenFileHasTestScope() {
        // given
        final String gosuFileName = "SpaceMarineTest.gs";
        final GosuTestFileParser parser = new GosuTestFileParser(gosuFileName, InputFile.Type.TEST);
        final String componentKey = samplesComponentKeyOf(gosuFileName);

        // when
        final GosuTestFileParser.GosuFileParsed fileParsed = parser.parseWithSensorContext(SAMPLES_RESOURCES_DIR, "CodeSizeMetricsTest");
        final SensorContextTester sensor = fileParsed.getSensorContext();

        // then
        assertThat(sensor.measure(componentKey, CoreMetrics.CLASSES)).isNull();
        assertThat(sensor.measure(componentKey, CoreMetrics.FUNCTIONS)).isNull();
        assertThat(sensor.measure(componentKey, CoreMetrics.STATEMENTS)).isNull();
    }

}
