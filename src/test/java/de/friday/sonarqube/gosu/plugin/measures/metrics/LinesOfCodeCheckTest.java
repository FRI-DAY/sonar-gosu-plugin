package de.friday.sonarqube.gosu.plugin.measures.metrics;

import de.friday.sonarqube.gosu.plugin.checks.metrics.LinesOfCodeCheck;
import org.junit.jupiter.api.Test;
import static de.friday.test.support.checks.dsl.gosu.GosuCheckTestDsl.given;

class LinesOfCodeCheckTest {
    @Test
    void findsIssuesWhenLinesOfCodeAreAboveFiveHundredLines() {
        given("LinesOfCodeCheck/lines501.gs")
                .whenCheckedAgainst(LinesOfCodeCheck.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(1);
    }

    @Test
    void findsNoIssuesWhenLinesOfCodeAreWithinThreshold() {
        given("LinesOfCodeCheck/lines500.gs")
                .whenCheckedAgainst(LinesOfCodeCheck.class)
                .then()
                .issuesFound()
                .areEmpty();
    }

    @Test
    void findsNoIssuesWhenLinesOfCodeAreBelowThreshold() {
        given("LinesOfCodeCheck/lines499.gs")
                .whenCheckedAgainst(LinesOfCodeCheck.class)
                .then()
                .issuesFound()
                .areEmpty();
    }
}
