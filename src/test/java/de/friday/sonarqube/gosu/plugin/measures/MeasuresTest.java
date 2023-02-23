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
package de.friday.sonarqube.gosu.plugin.measures;

import de.friday.sonarqube.gosu.plugin.utils.TextRangeUtil;
import de.friday.test.support.GosuSensorContextTester;
import de.friday.test.support.GosuTestFileParser;
import de.friday.test.support.GosuTestFileParser.GosuFileParsed;
import de.friday.test.support.TestResourcesDirectories;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MeasuresTest {
    private static final Path BASE_DIR = Paths.get(TestResourcesDirectories.RESOURCES_DIR.getPath());

    private SensorContextTester context;

    @BeforeEach
    void setUp() {
        context = new GosuSensorContextTester(BASE_DIR).get();
    }

    private Stream<Arguments> getTokensToHighlight() {
        return Stream.of(
                of("/measures/highlighting/GosuFile.gs", "package", 0, TypeOfText.KEYWORD),
                of("/measures/highlighting/GosuFile.gs", "class", 2, TypeOfText.KEYWORD),
                of("/measures/highlighting/GosuFile.gs", "at (@)", 7, TypeOfText.ANNOTATION),
                of("/measures/highlighting/GosuFile.gs", "Override", 8, TypeOfText.ANNOTATION),
                of("/measures/highlighting/GosuFile.gs", "multi line comment", 5, TypeOfText.STRUCTURED_COMMENT),
                of("/measures/highlighting/GosuFile.gs", "in line comment", 6, TypeOfText.COMMENT),
                of("/measures/highlighting/GosuFile.gs", "function", 9, TypeOfText.KEYWORD),
                of("/measures/highlighting/GosuFile.gs", "String literal", 22, TypeOfText.STRING),
                of("/measures/highlighting/GosuFile.gs", "var", 25, TypeOfText.KEYWORD),
                of("/measures/highlighting/GosuFile.gs", "Number literal", 28, TypeOfText.CONSTANT),
                of("/measures/highlighting/GosuFile.gs", "conjunction", 33, TypeOfText.KEYWORD),
                of("/measures/highlighting/GosuFile.gs", "true", 38, TypeOfText.KEYWORD),
                of("/measures/highlighting/GosuFile.gs", "disjunction", 39, TypeOfText.KEYWORD),
                of("/measures/highlighting/GosuFile.gs", "false", 40, TypeOfText.KEYWORD),
                of("/measures/highlighting/GosuFile.gs", "open multi line String", 55, TypeOfText.STRING),
                of("/measures/highlighting/GosuFile.gs", "multi line String", 56, TypeOfText.STRING),
                of("/measures/highlighting/GosuFile.gs", "close multi line String", 57, TypeOfText.STRING),
                of("/measures/highlighting/GosuFile.gs", "keyword in String interpolation", 64, TypeOfText.KEYWORD),
                of("/measures/highlighting/GosuFile.gs", "single quote String", 74, TypeOfText.STRING),
                of("/measures/highlighting/GosuFile.gs", "in line comment", 80, TypeOfText.COMMENT)
        );
    }

    @ParameterizedTest(name = "should highlight {1} token of type {3} at {2} on {0}")
    @MethodSource("getTokensToHighlight")
    void shouldHighlightGosuTokens(
            String gosuSourceCodeFileName,
            String tokenName,
            int highlightedTokenIndex,
            TypeOfText expectedTypeOfHighlightedText
    ) {
        // given
        final GosuFileParsed fileParsed = parse(gosuSourceCodeFileName);

        // when
        Measures.of(fileParsed.getSourceFileProperties()).addProcessedTokensTo(context);
        final CommonTokenStream commonTokenStream = fileParsed.getSourceFileProperties().getTokenStream();
        final TextRange textRange = TextRangeUtil.fromToken(commonTokenStream.get(highlightedTokenIndex));

        // then
        assertThat(startOfTextHighlightedOn(fileParsed, textRange))
                .as(
                        "%s token at %s should have been highlighted on %s Gosu source file",
                        tokenName, highlightedTokenIndex, gosuSourceCodeFileName
                ).contains(expectedTypeOfHighlightedText);

        assertThat(endOfTextHighlightedOn(fileParsed, textRange))
                .as(
                        "%s token at %s should have been highlighted on %s Gosu source file",
                        tokenName, highlightedTokenIndex, gosuSourceCodeFileName
                ).contains(expectedTypeOfHighlightedText);
    }

    private List<TypeOfText> startOfTextHighlightedOn(GosuFileParsed fileParsed, TextRange textRange) {
        int startOfLineIndex = textRange.start().line();
        int startOfLineOffset = textRange.start().lineOffset();
        return context.highlightingTypeAt(fileParsed.getInputFile().key(), startOfLineIndex, startOfLineOffset);
    }

    private List<TypeOfText> endOfTextHighlightedOn(GosuFileParsed fileParsed, TextRange textRange) {
        final int endOfLineIndex = textRange.end().line();
        final int endOfLineOffset = textRange.end().lineOffset() - 1;
        return context.highlightingTypeAt(fileParsed.getInputFile().key(), endOfLineIndex, endOfLineOffset);
    }

    private GosuFileParsed parse(String fileName) {
        final GosuTestFileParser parser = new GosuTestFileParser(fileName);
        return parser.parse();
    }
}
