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

import de.friday.sonarqube.gosu.antlr.GosuLexer;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.context.AnalysisModule;
import de.friday.sonarqube.gosu.plugin.context.GosuParserContext;
import de.friday.sonarqube.gosu.plugin.issues.Issue;
import de.friday.sonarqube.gosu.plugin.issues.IssueCollector;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.surefire.data.UnitTestIndex;

public class GosuFileParser {
    private final GosuParser gosuParser = new GosuParser(null);
    private final GosuLexer gosuLexer = new GosuLexer(null);
    private final GosuFileProperties gosuFileProperties;
    private final SensorContext context;
    private final UnitTestIndex unitTestIndex;
    private final IssueCollector collector = new IssueCollector();
    private final InputFile inputFile;

    public GosuFileParser(InputFile inputFile, SensorContext context, UnitTestIndex index) throws IOException {
        this.inputFile = inputFile;
        this.context = context;
        this.unitTestIndex = index;
        this.gosuFileProperties = initializeTokenStream();
    }

    public List<Issue> parse() {
        final GosuParserContext parserContext = initializeParserContext();
        parserContext.start();
        parseFile();
        parserContext.stop();
        return getIssues();
    }

    private GosuParserContext initializeParserContext() {
        final AnalysisModule analysisModule = new AnalysisModule(context, gosuFileProperties, collector, unitTestIndex);
        return new GosuParserContext(context, analysisModule, inputFile, gosuParser);
    }

    private GosuFileProperties initializeTokenStream() throws IOException {
        try (InputStream stream = inputFile.inputStream()) {
            gosuLexer.setInputStream(CharStreams.fromStream(stream));
            CommonTokenStream tokenStream = new CommonTokenStream(gosuLexer);
            gosuParser.setTokenStream(tokenStream);
            return new GosuFileProperties(inputFile, tokenStream);
        }
    }

    private void parseFile() {
        gosuParser.start();
    }

    private List<Issue> getIssues() {
        return collector.getIssues();
    }

    public GosuFileProperties getProperties() {
        return new GosuFileProperties(gosuFileProperties.getFile(), gosuFileProperties.getTokenStream());
    }
}
