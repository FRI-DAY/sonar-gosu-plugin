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

import de.friday.sonarqube.gosu.antlr.GosuParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;

public class GosuFileLineData {

    private static final String COMMENT_TOKENS_REGEX = "^\\s?[/,*].*$";

    private final GosuFileProperties fileProperties;
    private final FileLinesContext fileLinesContext;
    private int numberOfLinesOfCode = 0;
    private int numberOfCommentedLines = 0;
    private final Set<Integer> linesOfCodeIndexes = new HashSet<>();
    private final Set<Integer> executableLinesIndexes = new HashSet<>();

    public GosuFileLineData(GosuFileProperties gosuFileProperties) {
        this.fileProperties = gosuFileProperties;
        this.fileLinesContext = fileProperties.getFileLinesContext();
        computeLinesOfCodeOf(gosuFileProperties.getFile());
        computeLineIndexesOf(gosuFileProperties);
    }

    public void saveOnContext() {
        final InputFile currentFile = fileProperties.getFile();

        for (int line = 1; line <= currentFile.lines(); line++) {
            fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, linesOfCodeIndexes.contains(line) ? 1 : 0);
            fileLinesContext.setIntValue(CoreMetrics.EXECUTABLE_LINES_DATA_KEY, line, executableLinesIndexes.contains(line) ? 1 : 0);
        }

        fileLinesContext.save();
    }

    public int getNumberOfLinesOfCode() {
        return numberOfLinesOfCode;
    }

    public boolean isNumberOfLinesOfCodeGreaterThan(int threshold) {
        return this.numberOfLinesOfCode > threshold;
    }

    public int getNumberOfCommentedLines() {
        return numberOfCommentedLines;
    }

    private void computeLineIndexesOf(GosuFileProperties gosuFileProperties) {
        gosuFileProperties.getTokenStream().getTokens().forEach(token -> {
            if (isCodeToken(token)) linesOfCodeIndexes.add(token.getLine());
            if (isExecutableToken(token)) executableLinesIndexes.add(token.getLine());
        });
    }

    private boolean isCodeToken(Token token) {
        return !isCommentToken(token) && token.getType() != Recognizer.EOF;
    }

    private boolean isCommentToken(Token token) {
        switch (token.getType()) {
            case GosuParser.LINE_COMMENT:
            case GosuParser.COMMENT:
                return true;
            default:
                return false;
        }
    }

    private boolean isExecutableToken(Token token) {
        switch (token.getType()) {
            case GosuParser.BLOCK:
            case GosuParser.FUNCTION:
            case GosuParser.FOR:
            case GosuParser.FOREACH:
            case GosuParser.WHILE:
            case GosuParser.DO:
            case GosuParser.NEW:
            case GosuParser.TRY:
            case GosuParser.CATCH:
                return true;
            default:
                return false;
        }
    }

    private void computeLinesOfCodeOf(InputFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.inputStream()))) {
            while (reader.ready()) {
                final String line = reader.readLine();
                if (isLineOfCode(line)) {
                    numberOfLinesOfCode++;
                    continue;
                }

                if (isCommentLine(line)) {
                    numberOfCommentedLines++;
                }
            }
        } catch (IOException ioException) {
            throw new GosuPluginException("Unable to compute lines of code for source file: " + file.filename(), ioException);
        }
    }

    private boolean isCommentLine(String line) {
        return hasValidCharacters(line) && isComment(line);
    }

    private boolean isLineOfCode(final String line) {
        return hasValidCharacters(line) && !isComment(line);
    }

    private boolean isComment(final String line) {
        return line.trim().matches(COMMENT_TOKENS_REGEX);
    }

    private boolean hasValidCharacters(final String line) {
        return StringUtils.isNotBlank(line) && StringUtils.isNotEmpty(line);
    }
}
