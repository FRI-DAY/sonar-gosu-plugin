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

import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.test.support.rules.dsl.specification.RuleSpecification;
import de.friday.test.support.rules.dsl.specification.IssueSpecification;
import de.friday.test.support.rules.dsl.specification.SourceCodeFile;
import java.util.List;
import java.util.Objects;
import org.sonar.api.batch.sensor.issue.Issue;

public class GosuRuleSpecification implements RuleSpecification {
    private SourceCodeFile sourceCodeFile;
    private Class<? extends BaseGosuRule> checkClass;

    public GosuRuleSpecification(String sourceCodeFileName) {
        this(new GosuSourceCodeFile(sourceCodeFileName));
    }

    public GosuRuleSpecification(SourceCodeFile sourceCodeFile) {
        this.sourceCodeFile = sourceCodeFile;
    }

    @Override
    public RuleSpecification given(SourceCodeFile sourceCodeFile) {
        Objects.requireNonNull(sourceCodeFile, "Source code file can not be null.");
        this.sourceCodeFile = sourceCodeFile;
        return this;
    }

    @Override
    public RuleSpecification given(String sourceCodeFileName) {
        Objects.requireNonNull(sourceCodeFileName, "Source code file name can not be null.");
        this.sourceCodeFile = new GosuSourceCodeFile(sourceCodeFileName);
        return this;
    }

    @Override
    public RuleSpecification whenCheckedAgainst(Class<? extends BaseGosuRule> ruleClass) {
        Objects.requireNonNull(ruleClass, "Check class can not be null.");
        this.checkClass = ruleClass;
        return this;
    }

    @Override
    public IssueSpecification then() {
        final List<Issue> issues = executeRule();
        return new GosuIssuesSpecification(issues);
    }

    @Override
    public List<Issue> executeRule() {
        return new GosuRuleTestRunner(checkClass, sourceCodeFile).executeRule();
    }
}
