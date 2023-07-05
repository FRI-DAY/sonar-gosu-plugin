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
import de.friday.test.support.rules.dsl.specification.IssueSpecification;
import de.friday.test.support.rules.dsl.specification.RuleSpecification;
import de.friday.test.support.rules.dsl.specification.SourceCodeFile;
import org.sonar.api.batch.sensor.issue.Issue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GosuRuleSpecification implements RuleSpecification {
    private SourceCodeFile sourceCodeFile;
    private Class<? extends BaseGosuRule> checkClass;

    private final Map<String, String> parameters = new HashMap<>();

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
    public RuleSpecification withParameter(String key, String value) {
        Objects.requireNonNull(key, "Param Key can not be null.");
        Objects.requireNonNull(value, "Param Value can not be null.");
        this.parameters.put(key, value);
        return this;
    }

    @Override
    public RuleSpecification withParameters(Map<String, String> parameters) {
        Objects.requireNonNull(parameters, "Map of params can not be null.");
        parameters.forEach(this::withParameter);
        return this;
    }

    @Override
    public IssueSpecification then() {
        final List<Issue> issues = executeRule();
        return new GosuIssuesSpecification(issues);
    }

    @Override
    public List<Issue> executeRule() {
        return new GosuRuleTestRunner(checkClass, sourceCodeFile, parameters).executeRule();
    }
}
