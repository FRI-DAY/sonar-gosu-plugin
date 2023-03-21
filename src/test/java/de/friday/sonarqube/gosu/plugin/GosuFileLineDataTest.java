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

import de.friday.test.support.GosuTestFileParser;
import de.friday.test.support.TestResourcesDirectories;
import de.friday.test.support.sonar.scanner.FileLinesContextFactorySpy;
import java.io.IOException;
import java.util.stream.Stream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import static de.friday.test.support.TestResourcesDirectories.SAMPLES_RESOURCES_DIR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GosuFileLineDataTest {

    private SensorContextTester sonarContext;

    @BeforeEach
    void setup() {
        sonarContext = SensorContextTester.create(SAMPLES_RESOURCES_DIR.getPath());
    }

    @ParameterizedTest
    @MethodSource("getGosuSourceFiles")
    void shouldReturnFileLinesData(String gosuSourceFilePath, int expectedNumberOfLinesOfCode, int expectedNumberOfCommentedLines) {
        final GosuFileProperties fileProperties = aFilePropertiesOf(gosuSourceFilePath);

        final GosuFileLineData fileLineData = new GosuFileLineData(fileProperties);

        assertThat(fileLineData.getNumberOfLinesOfCode()).isEqualTo(expectedNumberOfLinesOfCode);
        assertThat(fileLineData.getNumberOfCommentedLines()).isEqualTo(expectedNumberOfCommentedLines);
    }

    private Stream<Arguments> getGosuSourceFiles() {
        return Stream.of(
                Arguments.of("/samples/Foo.gs", 6, 1),
                Arguments.of("/samples/FooEnhancement.gsx", 6, 4),
                Arguments.of("/samples/SpaceMarine.gs", 7, 4),
                Arguments.of("/samples/SpaceMarineTest.gs", 11, 0)
        );
    }

    @Test
    void throwsGosuPluginExceptionWhenFileLineDataCanNotBeCreated() {
        final GosuTestFileParser.GosuFileParsed fileParsed = aParsedGosuFileOf("/samples/Foo.gs");
        final InputFile fakeInputFile = TestInputFileBuilder.create("fakeModule", "FakeFile.java").build();
        final CommonTokenStream tokenStream = fileParsed.getSourceFileProperties().getTokenStream();
        final GosuFileProperties fileProperties = new GosuFileProperties(fakeInputFile, tokenStream, aFileLinesContextOf(fakeInputFile));

        assertThatThrownBy(() -> new GosuFileLineData(fileProperties))
                .isInstanceOf(GosuPluginException.class)
                .hasCauseInstanceOf(IOException.class)
                .hasMessage("Unable to compute lines of code for source file: FakeFile.java");
    }

    @ParameterizedTest
    @MethodSource("getGosuSourceFilesWithLineData")
    void shouldSaveLineMetricsOnSensorContext(String gosuSourceFileName, String expectedLinesData, String expectedExecutableLinesData) {
        final GosuFileProperties fileProperties = aFilePropertiesOf(gosuSourceFileName);
        final String key = componentKeyOf(gosuSourceFileName);
        final GosuFileLineData fileLineData = new GosuFileLineData(fileProperties);

        fileLineData.saveOnContext();

        assertThat(sonarContext.measure(key, CoreMetrics.NCLOC_DATA_KEY).value()).isEqualTo(expectedLinesData);
        assertThat(sonarContext.measure(key, CoreMetrics.EXECUTABLE_LINES_DATA_KEY).value()).isEqualTo(expectedExecutableLinesData);
    }

    private String componentKeyOf(String gosuSourceFilePath) {
        return TestResourcesDirectories.RESOURCES_DIR.getPathAsString() + ":" + gosuSourceFilePath.replaceFirst("/", "");
    }

    private Stream<Arguments> getGosuSourceFilesWithLineData() {
        return Stream.of(
                Arguments.of("/samples/Foo.gs", "1=1;3=1;5=1;6=1;7=1;9=1", "5=1"),
                Arguments.of("/samples/FooEnhancement.gsx", "1=1;3=1;8=1;10=1;11=1;13=1", ""),
                Arguments.of("/samples/SpaceMarine.gs", "1=1;3=1;5=1;7=1;8=1;9=1;15=1", "7=1"),
                Arguments.of("/samples/SpaceMarineTest.gs", "1=1;3=1;4=1;6=1;8=1;9=1;10=1;11=1;12=1;13=1;14=1", "9=1;11=1")
        );
    }

    private GosuFileProperties aFilePropertiesOf(String gosuSourceFilePath) {
        final GosuTestFileParser.GosuFileParsed fileParsed = aParsedGosuFileOf(gosuSourceFilePath);
        final InputFile inputFile = fileParsed.getInputFile();
        final FileLinesContext fileLinesContext = aFileLinesContextOf(inputFile);
        final CommonTokenStream tokenStream = fileParsed.getSourceFileProperties().getTokenStream();
        return new GosuFileProperties(inputFile, tokenStream, fileLinesContext);
    }

    private GosuTestFileParser.GosuFileParsed aParsedGosuFileOf(String testFilePath) {
        return new GosuTestFileParser(testFilePath, InputFile.Type.MAIN).parse();
    }

    private FileLinesContext aFileLinesContextOf(InputFile inputFile) {
        return new FileLinesContextFactorySpy(sonarContext).createFor(inputFile);
    }
}
