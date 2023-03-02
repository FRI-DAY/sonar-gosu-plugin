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
import de.friday.sonarqube.gosu.plugin.GosuFileParser;
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import de.friday.sonarqube.gosu.plugin.reports.ReportsDirectories;
import de.friday.sonarqube.gosu.plugin.reports.ReportsScanner;
import de.friday.test.support.rules.dsl.gosu.GosuSourceCodeFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.plugins.surefire.data.UnitTestIndex;

public class GosuTestFileParser {
    private final GosuLexer gosuLexer = new GosuLexer(null);
    private final GosuParser gosuParser = new GosuParser(null);

    private final String gosuSourceFilename;

    public GosuTestFileParser(String gosuSourceFilename) {
        this.gosuSourceFilename = gosuSourceFilename;
    }

    public GosuFileParsed parse() {
        return parse(Optional.empty());
    }

    public GosuFileParsed parse(Optional<ParseTreeListener> listener) {
        final CommonTokenStream commonTokenStream = createTokenStreamOf(gosuSourceFilename, listener);

        gosuParser.start();

        if (gosuParser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("Gosu file " + gosuSourceFilename + " has syntax errors.");
        }

        final InputFile inputFile = new GosuSourceCodeFile(
                gosuSourceFilename,
                TestResourcesDirectories.RESOURCES_DIR.getPathAsString()
        ).asInputFile();
        final GosuFileProperties gosuFileProperties = new GosuFileProperties(inputFile, commonTokenStream);

        return new GosuFileParsed(inputFile, gosuFileProperties, null);
    }

    private CommonTokenStream createTokenStreamOf(String gosuSourceFilename, Optional<ParseTreeListener> parseListener) {
        try (final InputStream inputStream = this.getClass().getResourceAsStream(gosuSourceFilename)) {
            assert inputStream != null;

            gosuLexer.setInputStream(CharStreams.fromStream(inputStream));
            final CommonTokenStream commonTokenStream = new CommonTokenStream(gosuLexer);
            parseListener.ifPresent(gosuParser::addParseListener);
            gosuParser.setTokenStream(commonTokenStream);

            return commonTokenStream;
        } catch (IOException e) {
            throw new RuntimeException("Error trying to parse " + gosuSourceFilename, e);
        }
    }

    public GosuFileParsed parseWithSensorContext(TestResourcesDirectories baseDir, String packageName) {
        final InputFile inputFile = new GosuSourceCodeFile(gosuSourceFilename, baseDir.getPathAsString()).asInputFile();
        final SensorContextTester context = new GosuSensorContextTester(baseDir.getPath()).get();
        final GosuFileProperties gosuFileProperties = parse(baseDir, packageName, inputFile, context);

        return new GosuFileParsed(inputFile, gosuFileProperties, context);
    }

    private GosuFileProperties parse(TestResourcesDirectories baseDir, String packageName, InputFile inputFile, SensorContextTester context) {
        try {
            GosuFileParser gosuFileParser = new GosuFileParser(inputFile, context, getUnitTestIndex(baseDir, packageName));
            gosuFileParser.parse();
            return gosuFileParser.getProperties();
        } catch (IOException e) {
            throw new RuntimeException("Error trying to parse " + gosuSourceFilename, e);
        }
    }

    private UnitTestIndex getUnitTestIndex(TestResourcesDirectories baseDir, String packageName) {
        Configuration settings = new MapSettings().asConfig();
        ReportsScanner scanner = new ReportsScanner(settings);
        DefaultFileSystem fs = new DefaultFileSystem(new File(baseDir.getPathAsString() + File.separator + packageName));
        PathResolver pathResolver = new PathResolver();

        List<File> dirs = new ReportsDirectories(settings, fs, pathResolver).get();

        return scanner.createIndex(dirs);
    }

    public static class GosuFileParsed {
        private final InputFile inputFile;
        private final GosuFileProperties gosuFileProperties;

        private final SensorContextTester sensorContext;

        public GosuFileParsed(InputFile inputFile, GosuFileProperties gosuFileProperties, SensorContextTester context) {
            this.inputFile = inputFile;
            this.gosuFileProperties = gosuFileProperties;
            this.sensorContext = context;
        }

        public GosuFileProperties getSourceFileProperties() {
            return gosuFileProperties;
        }

        public InputFile getInputFile() {
            return inputFile;
        }

        public SensorContextTester getSensorContext() {
            return sensorContext;
        }
    }

}
