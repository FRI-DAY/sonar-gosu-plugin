package de.friday.sonarqube.gosu.plugin.measures.metrics;

import de.friday.sonarqube.gosu.plugin.checks.metrics.CyclomaticComplexityCheck;
import org.junit.jupiter.api.Test;
import static de.friday.test.support.checks.dsl.gosu.GosuCheckTestDsl.given;

class CyclomaticComplexityCheckTest {

    @Test
    void findsIssuesWhenExpressionsAreTooComplex() {
        given("CyclomaticComplexityCheck/ExpressionsTooComplex.gs")
                .whenCheckedAgainst(CyclomaticComplexityCheck.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(48);
    }

    @Test
    void findsNoIssuesOnEqualsAndHashCodeFunctions() {
        given("CyclomaticComplexityCheck/equalsHashCodeTest.gs")
                .whenCheckedAgainst(CyclomaticComplexityCheck.class)
                .then()
                .issuesFound()
                .areEmpty();
    }

    @Test
    void findsNoIssuesWhenComplexityIsWithinThreshold() {
        given("CyclomaticComplexityCheck/testWithThreshold.gs")
                .whenCheckedAgainst(CyclomaticComplexityCheck.class)
                .then()
                .issuesFound()
                .areEmpty();
    }

    @Test
    void findsNoIssuesWhenComplexityIsBellowThreshold() {
        given("CyclomaticComplexityCheck/testBelowThreshold.gs")
                .whenCheckedAgainst(CyclomaticComplexityCheck.class)
                .then()
                .issuesFound()
                .areEmpty();
    }

    @Test
    void findsIssuesWhenComplexityIsAboveThreshold() {
        given("CyclomaticComplexityCheck/testAboveThreshold.gs")
                .whenCheckedAgainst(CyclomaticComplexityCheck.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(12);
    }

    @Test
    void findsIssuesWhenConstructorAndPropertiesComplexityAreAboveThreshold() {
        given("CyclomaticComplexityCheck/constructorAndProperty.gs")
                .whenCheckedAgainst(CyclomaticComplexityCheck.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(24);
    }
}
