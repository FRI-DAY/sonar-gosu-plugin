package de.friday.sonarqube.gosu.plugin.checks.metrics;

import com.google.inject.Inject;
import de.friday.sonarqube.gosu.antlr.GosuParser;
import de.friday.sonarqube.gosu.plugin.checks.AbstractCheckBase;
import de.friday.sonarqube.gosu.plugin.issues.GosuIssue;
import de.friday.sonarqube.gosu.plugin.measures.metrics.LinesOfCodeMetric;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = LinesOfCodeCheck.KEY)
public class LinesOfCodeCheck extends AbstractCheckBase {
    static final String KEY = "LinesOfCodeCheck";
    private static final int DEFAULT_METHOD_THRESHOLD = 500;
    @RuleProperty(
            key = "Max",
            description = "Maximum authorized lines in a file.",
            defaultValue = "" + DEFAULT_METHOD_THRESHOLD
    )
    private int max = DEFAULT_METHOD_THRESHOLD;

    private LinesOfCodeMetric metric;

    @Inject
    LinesOfCodeCheck(LinesOfCodeMetric metric) {
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
