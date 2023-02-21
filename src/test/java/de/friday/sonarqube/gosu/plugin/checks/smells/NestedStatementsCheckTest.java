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

import de.friday.test.support.checks.dsl.gosu.GosuIssueLocations;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static de.friday.test.support.checks.dsl.gosu.GosuCheckTestDsl.given;

class NestedStatementsCheckTest {

    @Test
    void findsNoIssuesWhenStatementsAreNotNested() {
        given("NestedStatementsCheck/ok.gs")
                .whenCheckedAgainst(NestedStatementsCheck.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsIssuesWhenStatementsAreNested() {
        given("NestedStatementsCheck/nok.gs")
                .whenCheckedAgainst(NestedStatementsCheck.class)
                .then()
                .issuesFound().hasSizeEqualTo(16)
                .and().areLocatedOn(
                        GosuIssueLocations.of(
                                Arrays.asList(10, 11, 10, 13),
                                Arrays.asList(7, 5, 7, 8),
                                Arrays.asList(8, 7, 8, 10),
                                Arrays.asList(9, 9, 9, 11),

                                Arrays.asList(24, 9, 24, 11),
                                Arrays.asList(21, 5, 21, 10),
                                Arrays.asList(23, 8, 23, 13),
                                Arrays.asList(28, 8, 28, 13),

                                Arrays.asList(39, 13, 39, 15),
                                Arrays.asList(35, 5, 35, 12),
                                Arrays.asList(36, 7, 36, 13),
                                Arrays.asList(38, 11, 38, 13),

                                Arrays.asList(43, 13, 43, 16),
                                Arrays.asList(35, 5, 35, 12),
                                Arrays.asList(36, 7, 36, 13),
                                Arrays.asList(38, 11, 38, 13)
                        )
                );
    }
}
