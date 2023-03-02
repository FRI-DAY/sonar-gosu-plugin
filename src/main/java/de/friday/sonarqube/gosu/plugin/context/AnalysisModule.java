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

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import de.friday.sonarqube.gosu.plugin.GosuFileProperties;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.issues.IssueCollector;
import de.friday.sonarqube.gosu.plugin.measures.metrics.CognitiveComplexityMetric;
import de.friday.sonarqube.gosu.plugin.measures.metrics.CyclomaticComplexityMetric;
import de.friday.sonarqube.gosu.plugin.measures.metrics.LinesOfCodeMetric;
import de.friday.sonarqube.gosu.plugin.measures.metrics.TestsMetric;
import de.friday.sonarqube.gosu.plugin.tools.listeners.SuppressWarningsListener;
import de.friday.sonarqube.gosu.plugin.tools.listeners.SyntaxErrorListener;
import de.friday.sonarqube.gosu.plugin.tools.reflections.ClassExtractor;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.surefire.data.UnitTestIndex;

public class AnalysisModule extends AbstractModule {
    private final SensorContext context;
    private final GosuFileProperties gosuFileProperties;
    private final IssueCollector issueCollector;
    private final UnitTestIndex unitTestIndex;

    public AnalysisModule(SensorContext context, GosuFileProperties gosuFileProperties, IssueCollector issueCollector, UnitTestIndex unitTestIndex) {
        this.context = context;
        this.gosuFileProperties = gosuFileProperties;
        this.issueCollector = issueCollector;
        this.unitTestIndex = unitTestIndex;
    }

    @Override
    protected void configure() {
        bindBasicModuleFields();
        bindMetrics();
        bindChecks();
        bindErrorListeners();
        bindAdditionalListeners();
    }

    private void bindBasicModuleFields() {
        bind(SensorContext.class).toInstance(context);
        bind(GosuFileProperties.class).toInstance(gosuFileProperties);
        bind(IssueCollector.class).toInstance(issueCollector);
        bind(UnitTestIndex.class).toInstance(unitTestIndex);
    }

    private void bindMetrics() {
        bind(CognitiveComplexityMetric.class).in(Singleton.class);
        bind(CyclomaticComplexityMetric.class).in(Singleton.class);
        bind(LinesOfCodeMetric.class).in(Singleton.class);
        bind(TestsMetric.class).in(Singleton.class);
    }

    private void bindChecks() {
        final Multibinder<BaseGosuRule> checksMultibinder = Multibinder.newSetBinder(binder(), BaseGosuRule.class);

        for (Class<? extends BaseGosuRule> check : ClassExtractor.getChecks().values()) {
            checksMultibinder.addBinding().to(check);
        }
    }

    private void bindErrorListeners() {
        bind(ANTLRErrorListener.class).to(SyntaxErrorListener.class);
    }

    private void bindAdditionalListeners() {
        bind(SuppressWarningsListener.class).in(Singleton.class);
    }
}
