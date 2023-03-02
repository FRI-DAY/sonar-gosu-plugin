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
package de.friday.sonarqube.gosu.plugin.context;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.antlr.GosuParserBaseListener;
import de.friday.sonarqube.gosu.language.GosuLanguage;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.measures.metrics.BaseMetric;
import de.friday.sonarqube.gosu.plugin.tools.listeners.SuppressWarningsListener;
import de.friday.sonarqube.gosu.plugin.tools.reflections.ClassExtractor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.ActiveRule;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.SensorContext;

public class GosuParserContext {

    private final GosuParser gosuParser;

    private final SensorContext context;

    private final AnalysisModule analysisModule;
    private final InputFile inputFile;

    public GosuParserContext(SensorContext context, AnalysisModule analysisModule, InputFile inputFile, GosuParser gosuParser) {
        this.context = context;
        this.analysisModule = analysisModule;
        this.inputFile = inputFile;
        this.gosuParser = gosuParser;
    }

    public void start() {
        final Injector injector = Guice.createInjector(analysisModule);
        registerChecksOn(injector);
        registerMetricsOn(injector);
        registerListenersOn(injector);
    }

    public void stop() {
        removeParserListeners();
    }

    private void registerChecksOn(Injector injector) {
        final List<BaseGosuRule> activeChecks = instantiateChecks(injector);

        for (BaseGosuRule check : activeChecks) {
            registerListener(check);
        }
    }

    private void registerMetricsOn(Injector injector) {
        for (Class<? extends BaseMetric> metric : ClassExtractor.getMetrics()) {
            registerListener(injector.getInstance(metric));
        }
    }

    private void registerListenersOn(Injector injector) {
        registerErrorListener(injector.getInstance(ANTLRErrorListener.class));
        registerListener(injector.getInstance(SuppressWarningsListener.class));
    }

    private List<BaseGosuRule> instantiateChecks(Injector injector) {
        final Collection<ActiveRule> activeRules = context.activeRules().findByRepository(GosuLanguage.REPOSITORY_KEY);
        final List<BaseGosuRule> activeChecks = new ArrayList<>();

        for (ActiveRule activeRule : activeRules) {
            getCheckClass(activeRule).ifPresent(check -> activeChecks.add(injector.getInstance(check)));
        }

        return bindChecksWithSonarAnnotations(activeChecks);
    }

    private void registerListener(GosuParserBaseListener check) {
        gosuParser.addParseListener(check);
    }

    private void registerErrorListener(ANTLRErrorListener errorListener) {
        gosuParser.addErrorListener(errorListener);
    }

    private Optional<Class<? extends BaseGosuRule>> getCheckClass(ActiveRule activeRule) {
        final String activeRuleKey = activeRule.ruleKey().rule();

        return ClassExtractor.getCheckForScope(activeRuleKey, inputFile.type());
    }

    private List<BaseGosuRule> bindChecksWithSonarAnnotations(List<BaseGosuRule> activeChecks) {
        final CheckFactory checkFactory = new CheckFactory(context.activeRules());
        final Checks<BaseGosuRule> checks = checkFactory.create(GosuLanguage.REPOSITORY_KEY);

        return new ArrayList<>(checks.addAnnotatedChecks((Iterable) activeChecks).all());
    }

    private void removeParserListeners() {
        gosuParser.removeParseListeners();
        gosuParser.removeErrorListeners();
    }
}
