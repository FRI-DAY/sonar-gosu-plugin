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
package de.friday.sonarqube.gosu.plugin.rules.smells;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static de.friday.test.support.rules.dsl.gosu.GosuRuleTestDsl.given;

class TooManyParamsRuleTest {

    @Test
    void findsNoIssuesWhenNumberOfParametersIsWithinThreshold() {
        given("TooManyParamsRule/ok.gs")
                .whenCheckedAgainst(TooManyParamsRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsIssuesWhenNumberOfParametersIsAboveThreshold() {
        given("TooManyParamsRule/nok.gs")
                .whenCheckedAgainst(TooManyParamsRule.class)
                .then().issuesFound().hasSizeEqualTo(27);
    }

    @Test
    void findsIssuesInConstructorWhenNumberOfParametersIsAboveThreshold() {
        given("TooManyParamsRule/nok.gs")
                .whenCheckedAgainst(TooManyParamsRule.class)
                .withRuleProperty("Max", "100")
                .then().issuesFound().hasSizeEqualTo(9);
    }

    @Test
    void findsNoIssuesInConstructorWhenNumberOfParametersIsAboveThreshold() {
        Map<String, String> ruleProperties = new HashMap<>();
        ruleProperties.put("Max", "100");
        ruleProperties.put("Constructor Max", "100");
        given("TooManyParamsRule/nok.gs")
                .whenCheckedAgainst(TooManyParamsRule.class)
                .withRuleProperties(ruleProperties)
                .then().issuesFound().areEmpty();
    }

}
