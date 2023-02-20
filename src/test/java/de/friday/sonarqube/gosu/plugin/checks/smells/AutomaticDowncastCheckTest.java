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
package de.friday.sonarqube.gosu.plugin.checks.smells;

import org.junit.jupiter.api.Test;
import static de.friday.test.support.checks.dsl.gosu.GosuCheckTestDsl.given;

class AutomaticDowncastCheckTest {

    @Test
    void findsNoIssuesWhenNoUnnecessaryCastIsFound() {
        given("AutomaticDowncastCheck/ok.gs")
                .whenCheckedAgainst(AutomaticDowncastCheck.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsIssuesWhenUnnecessaryCastIsFound() {
        given("AutomaticDowncastCheck/simpleCast.gs")
                .whenCheckedAgainst(AutomaticDowncastCheck.class)
                .then().issuesFound().hasSizeEqualTo(1);
    }

    @Test
    void findsNoIssuesWhenUnnecessaryCastIsNotFoundOnSwitchStatements() {
        given("AutomaticDowncastCheck/complexSwitch.gs")
                .whenCheckedAgainst(AutomaticDowncastCheck.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsIssuesWhenUnnecessaryCastIsFoundOnSwitchStatements() {
        given("AutomaticDowncastCheck/simpleSwitch.gs")
                .whenCheckedAgainst(AutomaticDowncastCheck.class)
                .then().issuesFound().hasSizeEqualTo(2);
    }
}
