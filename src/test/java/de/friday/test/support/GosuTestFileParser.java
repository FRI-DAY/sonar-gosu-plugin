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
package de.friday.test.support;

import de.friday.sonarqube.gosu.antlr.GosuLexer;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.Properties;
import de.friday.test.support.checks.dsl.gosu.GosuSourceCodeFile;
import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.sonar.api.batch.fs.InputFile;

public class GosuTestFileParser {
    private final GosuLexer gosuLexer = new GosuLexer(null);
    private final GosuParser gosuParser = new GosuParser(null);

    private final String gosuSourceFilename;

    public GosuTestFileParser(String gosuSourceFilename) {
        this.gosuSourceFilename = gosuSourceFilename;
    }

    public GosuFileParsed parse() {
        final CommonTokenStream commonTokenStream = createTokenStreamOf(gosuSourceFilename);

        gosuParser.start();

        if (gosuParser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("Gosu file " + gosuSourceFilename + " has syntax errors.");
        }

        final InputFile inputFile = new GosuSourceCodeFile(
                gosuSourceFilename,
                TestResourcesDirectories.RESOURCES_DIR.getPath()
        ).asInputFile();
        final Properties properties = new Properties(inputFile, commonTokenStream);

        return new GosuFileParsed(inputFile, properties);
    }

    private CommonTokenStream createTokenStreamOf(String gosuSourceFilename) {
        try (final InputStream inputStream = this.getClass().getResourceAsStream(gosuSourceFilename)) {
            assert inputStream != null;

            gosuLexer.setInputStream(CharStreams.fromStream(inputStream));
            final CommonTokenStream commonTokenStream = new CommonTokenStream(gosuLexer);
            gosuParser.setTokenStream(commonTokenStream);

            return commonTokenStream;
        } catch (IOException e) {
            throw new RuntimeException("Error trying to parse " + gosuSourceFilename, e);
        }
    }

    public static class GosuFileParsed {
        private final InputFile inputFile;
        private final Properties properties;

        public GosuFileParsed(InputFile inputFile, Properties properties) {
            this.inputFile = inputFile;
            this.properties = properties;
        }

        public Properties getSourceFileProperties() {
            return properties;
        }

        public InputFile getInputFile() {
            return inputFile;
        }
    }

}
