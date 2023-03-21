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
package de.friday.test.support.rules.dsl.gosu;

import de.friday.sonarqube.gosu.plugin.GosuFileParser;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.test.support.GosuSensorContextTester;
import de.friday.test.support.rules.dsl.specification.RuleRunner;
import de.friday.test.support.rules.dsl.specification.SourceCodeFile;
import de.friday.test.support.sonar.scanner.FileLinesContextFactorySpy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.check.Rule;
import org.sonar.plugins.surefire.data.UnitTestIndex;

/**
 * Runner for Gosu rules that implements the BaseGosuRule class.
 */
final class GosuRuleTestRunner implements RuleRunner<List<Issue>> {
    private final Class<? extends BaseGosuRule> rule;
    private final SourceCodeFile gosuSourceCodeFile;

    public GosuRuleTestRunner(Class<? extends BaseGosuRule> rule, SourceCodeFile gosuSourceCodeFile) {
        this.rule = rule;
        this.gosuSourceCodeFile = gosuSourceCodeFile;
    }

    @Override
    public List<Issue> executeRule() {
        final InputFile gosuInputFile = gosuSourceCodeFile.asInputFile();
        final SensorContextTester context = new GosuSensorContextTester(GosuRulesTestResources.getBaseDir(), getRuleKey()).get();
        final List<de.friday.sonarqube.gosu.plugin.issues.Issue> issues = analyse(gosuInputFile, context);

        issues.forEach(issue -> issue.createIssue(context, gosuInputFile));

        return new ArrayList<>(context.allIssues());
    }

    private String getRuleKey() {
        return this.rule.getAnnotation(Rule.class).key();
    }

    private List<de.friday.sonarqube.gosu.plugin.issues.Issue> analyse(InputFile inputFile, SensorContextTester context) {
        try {
            return new GosuFileParser(
                    inputFile,
                    context,
                    new UnitTestIndex(),
                    new FileLinesContextFactorySpy(context).createFor(inputFile)
            ).parse();
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to parse input file.");
        }
    }
}
