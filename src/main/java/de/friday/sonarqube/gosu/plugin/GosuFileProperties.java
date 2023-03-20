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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.fs.InputFile;

public class GosuFileProperties {

    private static final String COMMENT_TOKENS_REGEX = "^\\s?[/,*].*$";
    private final InputFile file;
    private final CommonTokenStream tokenStream;

    private int linesOfCode = 0;

    public GosuFileProperties(InputFile file, CommonTokenStream tokenStream) {
        this.file = file;
        this.tokenStream = tokenStream;
    }

    public CommonTokenStream getTokenStream() {
        return tokenStream;
    }

    public InputFile getFile() {
        return file;
    }

    public Token getToken(int index) {
        return tokenStream.get(index);
    }

    public int getLinesOfCode() {
        if (linesOfCode > 0) return linesOfCode;

        this.linesOfCode = computeLinesOfCodeOf(file);

        return linesOfCode;
    }

    private int computeLinesOfCodeOf(InputFile file) {
        int linesOfCode = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.inputStream()))) {
            while (reader.ready()) {
                final String line = reader.readLine();
                if (hasValidCharacters(line) && isNotComment(line)) {
                    linesOfCode++;
                }
            }
        } catch (IOException ioException) {
            throw new GosuPluginException("Unable to compute lines of code for source file: " + file.filename(), ioException);
        }

        return linesOfCode;
    }

    private boolean isNotComment(final String line) {
        return !line.trim().matches(COMMENT_TOKENS_REGEX);
    }

    private boolean hasValidCharacters(final String line) {
        return StringUtils.isNotBlank(line) && StringUtils.isNotEmpty(line);
    }
}
