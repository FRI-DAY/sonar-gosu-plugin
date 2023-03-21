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
import de.friday.test.support.sonar.scanner.FileLinesContextFactorySpy;
import java.io.IOException;
import java.util.stream.Stream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.measures.FileLinesContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GosuFilePropertiesTest {

    @ParameterizedTest
    @MethodSource("getGosuSourceFiles")
    void shouldReturnFileLineDataOfGosuFile(String gosuSourceFilePath, int expectedNumberOfLinesOfCode) {
        final GosuTestFileParser.GosuFileParsed fileParsed = aParsedGosuFileOf(gosuSourceFilePath);
        final InputFile inputFile = fileParsed.getInputFile();
        final FileLinesContext fileLinesContext = aFileLinesContextOf(inputFile);
        final CommonTokenStream tokenStream = fileParsed.getSourceFileProperties().getTokenStream();

        final GosuFileProperties fileProperties = new GosuFileProperties(inputFile, tokenStream, fileLinesContext);

        assertThat(fileProperties.getFileLineData().getNumberOfLinesOfCode()).isEqualTo(expectedNumberOfLinesOfCode);
    }

    private Stream<Arguments> getGosuSourceFiles() {
        return Stream.of(
                Arguments.of("/samples/Foo.gs", 6),
                Arguments.of("/samples/FooEnhancement.gsx", 6)
        );
    }

    @Test
    void throwsGosuPluginExceptionWhenFileLineDataCanNotBeCreated() {
        final GosuTestFileParser.GosuFileParsed fileParsed = aParsedGosuFileOf("/samples/Foo.gs");
        final InputFile fakeInputFile = TestInputFileBuilder.create("fakeModule", "FakeFile.java").build();
        final CommonTokenStream tokenStream = fileParsed.getSourceFileProperties().getTokenStream();

        final GosuFileProperties fileProperties = new GosuFileProperties(fakeInputFile, tokenStream, aFileLinesContextOf(fakeInputFile));

        assertThatThrownBy(fileProperties::getFileLineData)
                .isInstanceOf(GosuPluginException.class)
                .hasCauseInstanceOf(IOException.class)
                .hasMessage("Unable to compute lines of code for source file: FakeFile.java");
    }

    @Test
    void isMainFileShouldReturnTrueWhenFileHasMainScope() {
        final GosuTestFileParser.GosuFileParsed fileParsed = aParsedGosuFileOf("/samples/Foo.gs", InputFile.Type.MAIN);
        final InputFile inputFile = fileParsed.getInputFile();
        final CommonTokenStream tokenStream = fileParsed.getSourceFileProperties().getTokenStream();

        final GosuFileProperties fileProperties = new GosuFileProperties(inputFile, tokenStream, aFileLinesContextOf(inputFile));

        assertThat(fileProperties.isMainFile()).isTrue();
        assertThat(fileProperties.isTestFile()).isFalse();
    }

    @Test
    void isTestFileShouldReturnTrueWhenFileHasTestScope() {
        final GosuTestFileParser.GosuFileParsed fileParsed = aParsedGosuFileOf("/samples/FooTest.gs", InputFile.Type.TEST);
        final InputFile inputFile = fileParsed.getInputFile();
        final CommonTokenStream tokenStream = fileParsed.getSourceFileProperties().getTokenStream();

        final GosuFileProperties fileProperties = new GosuFileProperties(inputFile, tokenStream, aFileLinesContextOf(inputFile));

        assertThat(fileProperties.isTestFile()).isTrue();
        assertThat(fileProperties.isMainFile()).isFalse();
    }

    private GosuTestFileParser.GosuFileParsed aParsedGosuFileOf(String testFilePath) {
        return aParsedGosuFileOf(testFilePath, InputFile.Type.MAIN);
    }

    private GosuTestFileParser.GosuFileParsed aParsedGosuFileOf(String testFilePath, InputFile.Type type) {
        return new GosuTestFileParser(testFilePath, type).parse();
    }

    private FileLinesContext aFileLinesContextOf(InputFile inputFile) {
        return new FileLinesContextFactorySpy().createFor(inputFile);
    }
}
