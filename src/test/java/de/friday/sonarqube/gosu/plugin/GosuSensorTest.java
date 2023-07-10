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
import de.friday.test.support.rules.dsl.gosu.GosuSourceCodeFile;
import de.friday.test.support.rules.dsl.specification.SourceCodeFile;
import de.friday.test.support.sonar.scanner.FileLinesContextFactorySpy;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultTextPointer;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.scan.filesystem.PathResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collections;

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

    @Test
    void shouldSaveAnalysisErrorOnContextWhenInputFileCanNotBeRead() {
        // given
        final InputFile throwingInputFile = new ThrowingInputFile();
        final SensorContextTester sensorContextTester = createSensorContextTesterFor(new ThrowingInputFile(), false);
        final GosuSensor sensor = newGosuSensorFor(sensorContextTester);

        // when
        sensor.execute(sensorContextTester);

        // then
        assertThat(sensorContextTester.allAnalysisErrors()).hasSize(1).allSatisfy(
                analysisError -> {
                    assertThat(analysisError.inputFile().filename()).isEqualTo(throwingInputFile.filename());
                    assertThat(analysisError.location().line()).isZero();
                    assertThat(analysisError.location().lineOffset()).isZero();
                    assertThat(analysisError.message()).isEqualTo("Something exploded!!!");
                }
        );
    }

    private GosuSensor newGosuSensorFor(SensorContextTester sensorContextTester) {
        return new GosuSensor(
                sensorContextTester.fileSystem(),
                sensorContextTester.config(),
                new PathResolver(),
                new FileLinesContextFactorySpy(sensorContextTester)
        );
    }

    private SensorContextTester createSensorContextTesterFor(String fileName) {
        return createSensorContextTesterFor(fileName, false);
    }

    private SensorContextTester createSensorContextTesterFor(String fileName, boolean isCancelled) {
        final SourceCodeFile sourceCodeFile = new GosuSourceCodeFile(fileName, TestResourcesDirectories.SENSOR_RESOURCES_DIR.getPathAsString());
        return createSensorContextTesterFor(sourceCodeFile.asInputFile(), isCancelled);
    }

    private SensorContextTester createSensorContextTesterFor(InputFile inputFile, boolean isCancelled) {
        final GosuSensorContextTester sensorContextTester =
                new GosuSensorContextTester(TestResourcesDirectories.SENSOR_RESOURCES_DIR.getPath(),
                        "MagicNumbersRule", Collections.emptyMap());
        final SensorContextTester sensorContext = sensorContextTester.get();
        sensorContext.setCancelled(isCancelled);
        sensorContext.fileSystem().add(inputFile);
        return sensorContext;
    }

    private class ThrowingInputFile implements InputFile {

        @Override
        public String filename() {
            return "throwing-input-file.gs";
        }

        @Override
        public String relativePath() {
            return null;
        }

        @Override
        public String absolutePath() {
            return null;
        }

        @Override
        public File file() {
            return null;
        }

        @Override
        public Path path() {
            return null;
        }

        @Override
        public URI uri() {
            return null;
        }

        @Nullable
        @Override
        public String language() {
            return GosuLanguage.KEY;
        }

        @Override
        public Type type() {
            return null;
        }

        @Override
        public InputStream inputStream() throws IOException {
            throw new IOException("Something exploded!!!");
        }

        @Override
        public String contents() throws IOException {
            return null;
        }

        @Override
        public Status status() {
            return null;
        }

        @Override
        public int lines() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public TextPointer newPointer(int line, int lineOffset) {
            return new DefaultTextPointer(line, lineOffset);
        }

        @Override
        public TextRange newRange(TextPointer start, TextPointer end) {
            return null;
        }

        @Override
        public TextRange newRange(int startLine, int startLineOffset, int endLine, int endLineOffset) {
            return null;
        }

        @Override
        public TextRange selectLine(int line) {
            return null;
        }

        @Override
        public Charset charset() {
            return null;
        }

        @Override
        public String key() {
            return null;
        }

        @Override
        public boolean isFile() {
            return false;
        }
    }
}
