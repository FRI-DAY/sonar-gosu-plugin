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
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static de.friday.test.support.rules.dsl.gosu.GosuRuleTestDsl.given;

class TODOsCheckTest {

    @Test
    void findsNoIssuesWhenGosuCodeDoNotHaveTODOs() {
        given("TODOsCheck/ok.gs").whenCheckedAgainst(TODOsCheck.class).then().issuesFound().areEmpty();
    }

    @Test
    void findsIssuesWhenGosuCodeHasTODOs() {
        given("TODOsCheck/nok.gs")
                .whenCheckedAgainst(TODOsCheck.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(5)
                .and()
                .areLocatedOn(
                        GosuIssueLocations.of(
                                Arrays.asList(3, 1, 3, 28),
                                Arrays.asList(6, 1, 8, 4),
                                Arrays.asList(11, 4, 11, 15),
                                Arrays.asList(16, 3, 18, 6),
                                Arrays.asList(21, 1, 21, 18)
                        )
                );
    }
}
