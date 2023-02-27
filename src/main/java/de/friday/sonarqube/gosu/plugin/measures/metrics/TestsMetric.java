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
package de.friday.sonarqube.gosu.plugin.measures.metrics;

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.Properties;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.surefire.data.UnitTestClassReport;
import org.sonar.plugins.surefire.data.UnitTestIndex;

public class TestsMetric extends AbstractMetricBase {

    private static final Logger LOGGER = Loggers.get(TestsMetric.class);
    private final SensorContext context;
    private final Properties properties;
    private final UnitTestIndex index;

    private String packageName = "";

    @Inject
    public TestsMetric(SensorContext context, Properties properties, UnitTestIndex index) {
        this.context = context;
        this.properties = properties;
        this.index = index;
    }

    @Override
    public void exitPackageDeclaration(GosuParser.PackageDeclarationContext packageDeclarationContext) {
        if (packageDeclarationContext.namespace() == null) {
            return;
        }
        packageName = packageDeclarationContext.namespace().getText();
    }

    @Override
    public void exitClassSignature(GosuParser.ClassSignatureContext classSignatureContext) {
        processReportForClass(classSignatureContext.identifier().getText());
    }

    private void processReportForClass(String className) {
        final UnitTestClassReport unitTestClassReport = index.get(packageName + '.' + className);
        if (unitTestClassReport == null) {
            return;
        }
        if (unitTestClassReport.getTests() > 0) {
            saveMetrics(unitTestClassReport);
        }
        if (unitTestClassReport.getNegativeTimeTestNumber() > 0) {
            LOGGER.warn("There is {} test(s) reported with negative time by surefire, total duration may not be accurate.", unitTestClassReport.getNegativeTimeTestNumber());
        }
    }

    private void saveMetrics(UnitTestClassReport report) {
        final InputFile inputFile = properties.getFile();
        final int testsCount = report.getTests() - report.getSkipped();

        saveMetric(context, inputFile, CoreMetrics.SKIPPED_TESTS, report.getSkipped());
        saveMetric(context, inputFile, CoreMetrics.TESTS, testsCount);
        saveMetric(context, inputFile, CoreMetrics.TEST_ERRORS, report.getErrors());
        saveMetric(context, inputFile, CoreMetrics.TEST_FAILURES, report.getFailures());
        saveMetric(context, inputFile, CoreMetrics.TEST_EXECUTION_TIME, report.getDurationMilliseconds());
    }
}