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
package de.friday.sonarqube.gosu.plugin;

import de.friday.sonarqube.gosu.language.GosuLanguage;
import de.friday.test.support.GosuSensorContextTester;
import de.friday.test.support.TestResourcesDirectories;
import de.friday.test.support.checks.dsl.gosu.GosuSourceCodeFile;
import de.friday.test.support.checks.dsl.specification.SourceCodeFile;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.scan.filesystem.PathResolver;
import static org.assertj.core.api.Assertions.assertThat;

class GosuSensorTest {

    @Test
    void shouldSaveIssuesOnContextWhenGosuFileHasIssues() {
        // given
        final String fileName = "SensorTest.gs";
        final SensorContextTester sensorContextTester = createSensorContextTesterFor(fileName);
        final GosuSensor sensor = newGosuSensorFor(sensorContextTester);

        // when
        sensor.execute(sensorContextTester);

        // then
        assertThat(sensorContextTester.allIssues()).hasSize(2);
    }

    @Test
    void shouldSaveNoIssuesOnContextWhenGosuFileHasNoIssues() {
        // given
        final String fileName = "SensorTest2.gs";
        final SensorContextTester sensorContextTester = createSensorContextTesterFor(fileName);
        final GosuSensor sensor = newGosuSensorFor(sensorContextTester);

        // when
        sensor.execute(sensorContextTester);

        // then
        assertThat(sensorContextTester.allIssues()).isEmpty();
    }

    @Test
    void shouldSaveNoIssuesOnContextWhenSensorExecutionWasCancelled() {
        // given
        final String fileName = "SensorTest.gs";
        final SensorContextTester sensorContextTester = createSensorContextTesterFor(fileName, true);
        final GosuSensor sensor = newGosuSensorFor(sensorContextTester);

        // when
        sensor.execute(sensorContextTester);

        // then
        assertThat(sensorContextTester.allIssues()).isEmpty();
    }

    @Test
    void shouldAddGosuSensorDescriptionToSensorDescriptor() {
        // given
        final String fileName = "SensorTest2.gs";
        final DefaultSensorDescriptor sensorDescriptor = new DefaultSensorDescriptor();
        final SensorContextTester sensorContextTester = createSensorContextTesterFor(fileName);
        final GosuSensor sensor = newGosuSensorFor(sensorContextTester);

        // when
        sensor.describe(sensorDescriptor);

        // then
        assertThat(sensorDescriptor.name()).isEqualTo("Gosu Sensor");
        assertThat(sensorDescriptor.languages()).containsExactly(GosuLanguage.KEY);
    }

    private GosuSensor newGosuSensorFor(SensorContextTester sensorContextTester) {
        return new GosuSensor(sensorContextTester.fileSystem(), sensorContextTester.config(), new PathResolver());
    }

    private SensorContextTester createSensorContextTesterFor(String fileName) {
        return createSensorContextTesterFor(fileName, false);
    }

    private SensorContextTester createSensorContextTesterFor(String fileName, boolean isCancelled) {
        final SourceCodeFile sourceCodeFile = new GosuSourceCodeFile(fileName, TestResourcesDirectories.SENSOR_RESOURCES_DIR.getPathAsString());
        final GosuSensorContextTester sensorContextTester = new GosuSensorContextTester(TestResourcesDirectories.SENSOR_RESOURCES_DIR.getPath(), "MagicNumbersCheck");
        final SensorContextTester sensorContext = sensorContextTester.get();
        sensorContext.setCancelled(isCancelled);
        sensorContext.fileSystem().add(sourceCodeFile.asInputFile());
        return sensorContext;
    }
}
