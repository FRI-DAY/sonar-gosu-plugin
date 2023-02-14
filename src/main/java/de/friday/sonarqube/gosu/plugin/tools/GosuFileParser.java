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
package de.friday.sonarqube.gosu.plugin.tools;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.friday.sonarqube.gosu.antlr.GosuLexer;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.antlr.GosuParserBaseListener;
import de.friday.sonarqube.gosu.language.GosuLanguage;
import de.friday.sonarqube.gosu.plugin.Properties;
import de.friday.sonarqube.gosu.plugin.checks.AbstractCheckBase;
import de.friday.sonarqube.gosu.plugin.issues.Issue;
import de.friday.sonarqube.gosu.plugin.issues.IssueCollector;
import de.friday.sonarqube.gosu.plugin.measures.metrics.AbstractMetricBase;
import de.friday.sonarqube.gosu.plugin.tools.listeners.SuppressWarningsListener;
import de.friday.sonarqube.gosu.plugin.tools.reflections.ClassExtractor;
import de.friday.sonarqube.gosu.plugin.utils.annotations.UnitTestMissing;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.surefire.data.UnitTestIndex;

@UnitTestMissing
public class GosuFileParser {
    private final GosuParser gosuParser = new GosuParser(null);
    private final GosuLexer gosuLexer = new GosuLexer(null);
    private Properties properties;
    private final SensorContext context;
    private final UnitTestIndex unitTestIndex;
    private IssueCollector collector = new IssueCollector();
    private final InputFile inputFile;

    public GosuFileParser(InputFile inputFile, SensorContext context, UnitTestIndex index) throws IOException {
        this.inputFile = inputFile;
        this.context = context;
        this.unitTestIndex = index;
        initializeTokenStream();
    }

    public List<Issue> parse() {
        initializeListeners();
        parseFile();
        removeListeners();
        return getIssues();
    }

    private void initializeTokenStream() throws IOException {
        try (InputStream stream = inputFile.inputStream()) {
            gosuLexer.setInputStream(CharStreams.fromStream(stream));
            CommonTokenStream tokenStream = new CommonTokenStream(gosuLexer);
            gosuParser.setTokenStream(tokenStream);
            properties = new Properties(inputFile, tokenStream);
        }
    }

    private void initializeListeners() {
        Injector injector = Guice.createInjector(new AnalysisModule(context, properties, collector, unitTestIndex));
        List<AbstractCheckBase> activeChecks = instantiateChecks(injector);

        for (AbstractCheckBase check : activeChecks) {
            registerListener(check);
        }

        for (Class<? extends AbstractMetricBase> metric : ClassExtractor.getMetrics()) {
            registerListener(injector.getInstance(metric));
        }
        registerErrorListener(injector.getInstance(ANTLRErrorListener.class));
        registerListener(injector.getInstance(SuppressWarningsListener.class));
    }

    private List<AbstractCheckBase> instantiateChecks(Injector injector) {
        Collection<ActiveRule> activeRules = context.activeRules().findByRepository(GosuLanguage.REPOSITORY_KEY);
        List<AbstractCheckBase> activeChecks = new ArrayList<>();

        for (ActiveRule activeRule : activeRules) {
            getCheckClass(activeRule).ifPresent(check -> activeChecks.add(injector.getInstance(check)));
        }

        return bindChecksWithSonarAnnotations(activeChecks);
    }

    private List<AbstractCheckBase> bindChecksWithSonarAnnotations(List<AbstractCheckBase> activeChecks) {
        CheckFactory checkFactory = new CheckFactory(context.activeRules());
        Checks<AbstractCheckBase> checks = checkFactory.create(GosuLanguage.REPOSITORY_KEY);

        return new ArrayList<>(checks.addAnnotatedChecks((Iterable) activeChecks).all());
    }

    private Optional<Class<? extends AbstractCheckBase>> getCheckClass(ActiveRule activeRule) {
        String activeRuleKey = activeRule.ruleKey().rule();

        return ClassExtractor.getCheckForScope(activeRuleKey, inputFile.type());
    }

    private void registerListener(GosuParserBaseListener check) {
        gosuParser.addParseListener(check);
    }

    private void registerErrorListener(ANTLRErrorListener errorListener) {
        gosuParser.addErrorListener(errorListener);
    }

    private void parseFile() {
        gosuParser.start();
    }

    private void removeListeners() {
        gosuParser.removeParseListeners();
        gosuParser.removeErrorListeners();
    }

    private List<Issue> getIssues() {
        return collector.getIssues();
    }

    public Properties getProperties() {
        return new Properties(properties.getFile(), properties.getTokenStream());
    }
}
