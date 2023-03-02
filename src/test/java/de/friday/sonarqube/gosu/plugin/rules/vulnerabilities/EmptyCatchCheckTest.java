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
package de.friday.sonarqube.gosu.plugin.rules.vulnerabilities;

import de.friday.test.support.rules.dsl.gosu.GosuIssueLocations;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static de.friday.test.support.rules.dsl.gosu.GosuRuleTestDsl.given;

class EmptyCatchCheckTest {

    @Test
    void findsNoIssuesWhenCatchIsNotEmpty() {
        given("EmptyCatchCheck/ok.gs")
                .whenCheckedAgainst(EmptyCatchCheck.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsIssuesWhenCatchIsEmpty() {
        given("EmptyCatchCheck/nok.gs")
                .whenCheckedAgainst(EmptyCatchCheck.class)
                .then()
                .issuesFound()
                .hasSizeEqualTo(2)
                .and()
                .areLocatedOn(
                        GosuIssueLocations.of(
                                Arrays.asList(9, 7, 9, 12),
                                Arrays.asList(11, 7, 11, 12)
                        )
                );
    }
}
