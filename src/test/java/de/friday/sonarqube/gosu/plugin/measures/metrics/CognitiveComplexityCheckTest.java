package de.friday.sonarqube.gosu.plugin.measures.metrics;

import de.friday.sonarqube.gosu.plugin.checks.metrics.CognitiveComplexityCheck;
import de.friday.test.support.checks.dsl.gosu.GosuIssueLocations;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static de.friday.test.support.checks.dsl.gosu.GosuCheckTestDsl.given;

class CognitiveComplexityCheckTest {
    @Test
    void findsIssuesWhenGosuCodeHasHighCognitiveComplexity() {
        given("CognitiveComplexityCheck/CognitiveComplexity.gs")
                .whenCheckedAgainst(CognitiveComplexityCheck.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(12)
                .and()
                .areLocatedOn(
                        GosuIssueLocations.of(
                                Arrays.asList(4, 12, 4, 23),
                                Arrays.asList(7, 5, 7, 8),
                                Arrays.asList(8, 7, 8, 10),
                                Arrays.asList(9, 9, 9, 11),
                                Arrays.asList(17, 7, 17, 9),
                                Arrays.asList(17, 17, 17, 19),
                                Arrays.asList(24, 7, 24, 12),
                                Arrays.asList(26, 7, 26, 12),
                                Arrays.asList(27, 7, 27, 9),
                                Arrays.asList(31, 7, 31, 12),
                                Arrays.asList(35, 25, 35, 27),
                                Arrays.asList(35, 34, 35, 36)
                        )
                );
    }

    @Test
    void findsIssuesWhenGosuCodeHasExpressionsTooComplex() {
        given("CognitiveComplexityCheck/ExpressionsTooComplex.gs")
                .whenCheckedAgainst(CognitiveComplexityCheck.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(41);
    }

    @Test
    void findsIssuesWhenGosuCodeIsAboveComplexityThreshold() {
        given("CognitiveComplexityCheck/testAboveThreshold.gs")
                .whenCheckedAgainst(CognitiveComplexityCheck.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(12);
    }

    @Test
    void findsNoIssueWhenCognitiveComplexityIsBelowThreshold() {
        given("CognitiveComplexityCheck/testBelowThreshold.gs")
                .whenCheckedAgainst(CognitiveComplexityCheck.class)
                .then()
                .issuesFound()
                .areEmpty();
    }

    @Test
    void findsNoIssueWhenCognitiveComplexityIsEqualToThreshold() {
        given("CognitiveComplexityCheck/testWithThreshold.gs")
                .whenCheckedAgainst(CognitiveComplexityCheck.class)
                .then()
                .issuesFound()
                .areEmpty();
    }

    @Test
    void findsIssuesWhenConstructorsAndPropertiesAreTooComplex() {
        given("CognitiveComplexityCheck/constructorAndProperty.gs")
                .whenCheckedAgainst(CognitiveComplexityCheck.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(24);
    }

    @Test
    void findsNoIssuesWhenNestedIfsAreWithinThreshold() {
        given("CognitiveComplexityCheck/NestedIfStatements.gs")
                .whenCheckedAgainst(CognitiveComplexityCheck.class)
                .then()
                .issuesFound()
                .areEmpty();
    }
}
