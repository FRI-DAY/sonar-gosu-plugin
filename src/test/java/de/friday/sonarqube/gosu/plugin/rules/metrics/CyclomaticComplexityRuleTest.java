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

import de.friday.sonarqube.gosu.plugin.rules.metrics.CyclomaticComplexityRule;
import org.junit.jupiter.api.Test;
import static de.friday.test.support.rules.dsl.gosu.GosuRuleTestDsl.given;

class CyclomaticComplexityRuleTest {

    @Test
    void findsIssuesWhenExpressionsAreTooComplex() {
        given("CyclomaticComplexityRule/ExpressionsTooComplex.gs")
                .whenCheckedAgainst(CyclomaticComplexityRule.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(48);
    }

    @Test
    void findsNoIssuesOnEqualsAndHashCodeFunctions() {
        given("CyclomaticComplexityRule/equalsHashCodeTest.gs")
                .whenCheckedAgainst(CyclomaticComplexityRule.class)
                .then()
                .issuesFound()
                .areEmpty();
    }

    @Test
    void findsNoIssuesWhenComplexityIsWithinThreshold() {
        given("CyclomaticComplexityRule/testWithThreshold.gs")
                .whenCheckedAgainst(CyclomaticComplexityRule.class)
                .then()
                .issuesFound()
                .areEmpty();
    }

    @Test
    void findsNoIssuesWhenComplexityIsBellowThreshold() {
        given("CyclomaticComplexityRule/testBelowThreshold.gs")
                .whenCheckedAgainst(CyclomaticComplexityRule.class)
                .then()
                .issuesFound()
                .areEmpty();
    }

    @Test
    void findsIssuesWhenComplexityIsAboveThreshold() {
        given("CyclomaticComplexityRule/testAboveThreshold.gs")
                .whenCheckedAgainst(CyclomaticComplexityRule.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(12);
    }

    @Test
    void findsIssuesWhenConstructorAndPropertiesComplexityAreAboveThreshold() {
        given("CyclomaticComplexityRule/constructorAndProperty.gs")
                .whenCheckedAgainst(CyclomaticComplexityRule.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(24);
    }
}
