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

import de.friday.sonarqube.gosu.plugin.rules.metrics.CognitiveComplexityRule;
import de.friday.test.support.rules.dsl.gosu.GosuIssueLocations;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static de.friday.test.support.rules.dsl.gosu.GosuRuleTestDsl.given;

class CognitiveComplexityRuleTest {
    @Test
    void findsIssuesWhenGosuCodeHasHighCognitiveComplexity() {
        given("CognitiveComplexityRule/CognitiveComplexity.gs")
                .whenCheckedAgainst(CognitiveComplexityRule.class)
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
        given("CognitiveComplexityRule/ExpressionsTooComplex.gs")
                .whenCheckedAgainst(CognitiveComplexityRule.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(41);
    }

    @Test
    void findsIssuesWhenGosuCodeIsAboveComplexityThreshold() {
        given("CognitiveComplexityRule/testAboveThreshold.gs")
                .whenCheckedAgainst(CognitiveComplexityRule.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(12);
    }

    @Test
    void findsNoIssueWhenCognitiveComplexityIsBelowThreshold() {
        given("CognitiveComplexityRule/testBelowThreshold.gs")
                .whenCheckedAgainst(CognitiveComplexityRule.class)
                .then()
                .issuesFound()
                .areEmpty();
    }

    @Test
    void findsNoIssueWhenCognitiveComplexityIsEqualToThreshold() {
        given("CognitiveComplexityRule/testWithThreshold.gs")
                .whenCheckedAgainst(CognitiveComplexityRule.class)
                .then()
                .issuesFound()
                .areEmpty();
    }

    @Test
    void findsIssuesWhenConstructorsAndPropertiesAreTooComplex() {
        given("CognitiveComplexityRule/constructorAndProperty.gs")
                .whenCheckedAgainst(CognitiveComplexityRule.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(24);
    }

    @Test
    void findsNoIssuesWhenNestedIfsAreWithinThreshold() {
        given("CognitiveComplexityRule/NestedIfStatements.gs")
                .whenCheckedAgainst(CognitiveComplexityRule.class)
                .then()
                .issuesFound()
                .areEmpty();
    }
}
