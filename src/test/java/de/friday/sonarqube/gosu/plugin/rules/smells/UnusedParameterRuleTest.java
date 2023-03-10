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
import static de.friday.test.support.rules.dsl.gosu.GosuRuleTestDsl.given;

class UnusedParameterRuleTest {

    @Test
    void findsNoIssuesWhenNoUnnecessaryParameterIsFound() {
        given("UnusedParameterRule/ok.gs")
                .whenCheckedAgainst(UnusedParameterRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsNoIssuesWhenUnnecessaryParameterIsFoundOnAbstractClass() {
        given("UnusedParameterRule/abstractClass.gs")
                .whenCheckedAgainst(UnusedParameterRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsIssuesWhenUnnecessaryParameterIsFound() {
        given("UnusedParameterRule/nok.gs")
                .whenCheckedAgainst(UnusedParameterRule.class)
                .then().issuesFound().hasSizeEqualTo(16);
    }

    @Test
    void findsIssuesWhenUnnecessaryParameterIsFoundOnFinalClass() {
        given("UnusedParameterRule/finalClass.gs")
                .whenCheckedAgainst(UnusedParameterRule.class)
                .then().issuesFound().hasSizeEqualTo(4);
    }

    @Test
    void findsIssuesWhenUnnecessaryParameterIsFoundOnEnhancements() {
        given("UnusedParameterRule/stringEnhancement.gsx")
                .whenCheckedAgainst(UnusedParameterRule.class)
                .then().issuesFound().hasSizeEqualTo(2);
    }
}
