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
package de.friday.test.support.checks.dsl.gosu;

import de.friday.sonarqube.gosu.plugin.checks.AbstractCheckBase;
import de.friday.sonarqube.gosu.plugin.tools.GosuFileParser;
import de.friday.test.support.GosuSensorContextTester;
import de.friday.test.support.checks.dsl.specification.CheckRunner;
import de.friday.test.support.checks.dsl.specification.SourceCodeFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.check.Rule;
import org.sonar.plugins.surefire.data.UnitTestIndex;

/**
 * Check runner for Gosu checks that implements the AbstractCheckBase class.
 */
final class GosuCheckTestRunner implements CheckRunner<List<Issue>> {
    private final Class<? extends AbstractCheckBase> check;
    private final SourceCodeFile gosuSourceCodeFile;

    public GosuCheckTestRunner(Class<? extends AbstractCheckBase> check, SourceCodeFile gosuSourceCodeFile) {
        this.check = check;
        this.gosuSourceCodeFile = gosuSourceCodeFile;
    }

    @Override
    public List<Issue> executeCheck() {
        final InputFile gosuInputFile = gosuSourceCodeFile.asInputFile();
        final SensorContextTester context = new GosuSensorContextTester(GosuCheckTestResources.getBaseDir(), getRuleKey()).get();
        final List<de.friday.sonarqube.gosu.plugin.issues.Issue> issues = analyse(gosuInputFile, context);

        issues.forEach(issue -> issue.createIssue(context, gosuInputFile));

        return new ArrayList<>(context.allIssues());
    }

    private String getRuleKey() {
        return this.check.getAnnotation(Rule.class).key();
    }

    private List<de.friday.sonarqube.gosu.plugin.issues.Issue> analyse(InputFile inputFile, SensorContextTester context) {
        try {
            return new GosuFileParser(inputFile, context, new UnitTestIndex()).parse();
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to parse input file.");
        }
    }
}
