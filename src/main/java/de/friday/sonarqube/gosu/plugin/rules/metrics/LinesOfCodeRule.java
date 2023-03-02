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
package de.friday.sonarqube.gosu.plugin.rules.metrics;

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.rules.BaseGosuRule;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import de.friday.sonarqube.gosu.plugin.measures.metrics.LinesOfCodeMetric;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = LinesOfCodeRule.KEY)
public class LinesOfCodeRule extends BaseGosuRule {
    static final String KEY = "LinesOfCodeRule";
    private static final int DEFAULT_METHOD_THRESHOLD = 500;
    @RuleProperty(
            key = "Max",
            description = "Maximum authorized lines in a file.",
            defaultValue = "" + DEFAULT_METHOD_THRESHOLD
    )
    private int max = DEFAULT_METHOD_THRESHOLD;

    private LinesOfCodeMetric metric;

    @Inject
    LinesOfCodeRule(LinesOfCodeMetric metric) {
        this.metric = metric;
    }

    @Override
    public void exitStart(GosuParser.StartContext ctx) {
        if (metric.getLinesOfCode() > max) {
            addIssue(new GosuIssue.GosuIssueBuilder(this)
                    .withMessage(messageBuilder(metric.getLinesOfCode()))
                    .build());
        }
    }

    private String messageBuilder(int lines) {
        return "This file has " + lines + " lines, which is greater than "
                + max + " authorized. Split it into smaller files.";
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
