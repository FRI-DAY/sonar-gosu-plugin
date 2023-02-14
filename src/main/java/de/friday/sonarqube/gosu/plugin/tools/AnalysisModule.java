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

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import de.friday.sonarqube.gosu.plugin.checks.AbstractCheckBase;
import de.friday.sonarqube.gosu.plugin.issues.IssueCollector;
import de.friday.sonarqube.gosu.plugin.measures.metrics.CognitiveComplexityMetric;
import de.friday.sonarqube.gosu.plugin.measures.metrics.CyclomaticComplexityMetric;
import de.friday.sonarqube.gosu.plugin.measures.metrics.LinesOfCodeMetric;
import de.friday.sonarqube.gosu.plugin.measures.metrics.ReportMetric;
import de.friday.sonarqube.gosu.plugin.Properties;
import de.friday.sonarqube.gosu.plugin.tools.listeners.SuppressWarningsListener;
import de.friday.sonarqube.gosu.plugin.tools.listeners.SyntaxErrorListener;
import de.friday.sonarqube.gosu.plugin.tools.reflections.ClassExtractor;
import de.friday.sonarqube.gosu.plugin.utils.annotations.UnitTestMissing;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.surefire.data.UnitTestIndex;

@UnitTestMissing
public class AnalysisModule extends AbstractModule {
    private final SensorContext context;
    private final Properties properties;
    private final IssueCollector issueCollector;
    private final UnitTestIndex unitTestIndex;

    AnalysisModule(SensorContext context, Properties properties, IssueCollector issueCollector, UnitTestIndex unitTestIndex) {
        this.context = context;
        this.properties = properties;
        this.issueCollector = issueCollector;
        this.unitTestIndex = unitTestIndex;
    }

    @Override
    protected void configure() {
        //Fields
        bind(SensorContext.class).toInstance(context);
        bind(Properties.class).toInstance(properties);
        bind(IssueCollector.class).toInstance(issueCollector);
        bind(UnitTestIndex.class).toInstance(unitTestIndex);

        //Metrics
        bind(CognitiveComplexityMetric.class).in(Singleton.class);
        bind(CyclomaticComplexityMetric.class).in(Singleton.class);
        bind(LinesOfCodeMetric.class).in(Singleton.class);
        bind(ReportMetric.class).in(Singleton.class);

        //Checks
        Multibinder<AbstractCheckBase> checksMultibinder = Multibinder.newSetBinder(binder(), AbstractCheckBase.class);

        for (Class<? extends AbstractCheckBase> check : ClassExtractor.getChecks().values()) {
            checksMultibinder.addBinding().to(check);
        }

        //ErrorListeners
        bind(ANTLRErrorListener.class).to(SyntaxErrorListener.class);

        //AdditionalListeners
        bind(SuppressWarningsListener.class).in(Singleton.class);
    }
}
