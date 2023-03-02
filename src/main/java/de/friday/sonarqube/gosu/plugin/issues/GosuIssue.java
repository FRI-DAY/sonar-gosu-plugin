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
package de.friday.sonarqube.gosu.plugin.issues;

import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.utils.TextRangeUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;

public final class GosuIssue implements Issue {

    private TextRange textRange;
    private String message;
    private List<SecondaryIssue> secondaries;
    private RuleKey ruleKey;
    private int gap;

    private GosuIssue() {
    }

    @Override
    public void createIssue(SensorContext context, InputFile inputFile) {
        NewIssue issue = context.newIssue();
        NewIssueLocation location = getLocation(textRange, issue, inputFile).message(message);

        secondaries.stream().sorted().forEach(secondary -> {
            NewIssueLocation secondaryLocation = getLocation(secondary.range, issue, inputFile);
            if (secondary.message != null) {
                secondaryLocation.message(secondary.message);
            }
            issue.addLocation(secondaryLocation);
        });

        if (gap != -1) {
            issue.gap((double) gap);
        }

        issue.at(location).forRule(ruleKey).save();
    }

    @Override
    public RuleKey getRuleKey() {
        return ruleKey;
    }

    @Override
    public TextRange getPosition() {
        return textRange;
    }

    private NewIssueLocation getLocation(TextRange textRange, NewIssue issue, InputFile inputFile) {
        return textRange == null
                ? issue.newLocation().on(inputFile)
                : issue.newLocation().on(inputFile).at(textRange);
    }

    public static class GosuIssueBuilder {

        private TextRange textRange;
        private String message = "";
        private List<SecondaryIssue> secondaries = Collections.emptyList();
        private RuleKey ruleKey;
        private int gap = -1;

        public GosuIssueBuilder(BaseGosuRule rule) {
            this.ruleKey = rule.getRuleKey();
        }

        public GosuIssueBuilder withSecondaryIssues(Iterable<SecondaryIssue> secondaries) {
            List<SecondaryIssue> copy = new ArrayList<>();

            secondaries.forEach(secondary -> copy.add(SecondaryIssue.copyOf(secondary)));

            this.secondaries = copy;
            return this;
        }

        public GosuIssueBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public GosuIssueBuilder onPosition(int startOffset, int stopOffset, int startLine, int stopLine) {
            this.textRange = TextRangeUtil.fromPosition(startLine, startOffset, stopLine, stopOffset);
            return this;
        }

        public GosuIssueBuilder onContext(ParserRuleContext parserRuleContext) {
            this.textRange = TextRangeUtil.fromContext(parserRuleContext);
            return this;
        }

        public GosuIssueBuilder onTokenRange(Token start, Token end) {
            this.textRange = TextRangeUtil.fromTokens(start, end);
            return this;
        }

        public GosuIssueBuilder onToken(Token token) {
            this.textRange = TextRangeUtil.fromToken(token);
            return this;
        }

        public GosuIssueBuilder onTextRange(TextRange textRange) {
            this.textRange = textRange;
            return this;
        }

        public GosuIssueBuilder withGap(int gap) {
            this.gap = gap;
            return this;
        }

        public GosuIssue build() {
            GosuIssue gosuIssue = new GosuIssue();
            gosuIssue.secondaries = secondaries;
            gosuIssue.textRange = textRange;
            gosuIssue.message = message;
            gosuIssue.ruleKey = ruleKey;
            gosuIssue.gap = gap;
            return gosuIssue;
        }
    }
}
