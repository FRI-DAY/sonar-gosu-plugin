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

import de.friday.test.support.rules.dsl.gosu.GosuIssueLocations;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static de.friday.test.support.rules.dsl.gosu.GosuRuleTestDsl.given;

class MagicNumbersRuleTest {

    @Test
    void findsNoIssuesWhenNoMagicNumbersAreFound() {
        given("MagicNumbersRule/ok.gs")
                .whenCheckedAgainst(MagicNumbersRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsIssuesWhenMagicNumbersAreFound() {
        given("MagicNumbersRule/nok.gs")
                .whenCheckedAgainst(MagicNumbersRule.class)
                .then().issuesFound().hasSizeEqualTo(8)
                .and().areLocatedOn(
                        GosuIssueLocations.of(
                                Arrays.asList(4, 68, 4, 71),
                                Arrays.asList(4, 61, 4, 65),
                                Arrays.asList(7, 8, 7, 9),
                                Arrays.asList(7, 12, 7, 13),
                                Arrays.asList(11, 20, 11, 22),
                                Arrays.asList(11, 15, 11, 17),
                                Arrays.asList(12, 30, 12, 34),
                                Arrays.asList(12, 24, 12, 27)
                        )
                );
    }

    @Test
    void findsIssuesWhenMagicNumbersAreFoundButPropertiesSet() {
        given("MagicNumbersRule/nok.gs")
                .whenCheckedAgainst(MagicNumbersRule.class)
                .withRuleProperty("Authorized numbers", "2,3,12")
                .then().issuesFound().hasSizeEqualTo(4);
    }
}
