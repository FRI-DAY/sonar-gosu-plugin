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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UnnecessaryImportRuleTest {

    @Test
    void findsNoIssuesWhenNoUnnecessaryImportIsFound() {
        given("UnnecessaryImportRule/ok.gs")
                .whenCheckedAgainst(UnnecessaryImportRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsIssuesWhenUnnecessaryImportIsFound() {
        given("UnnecessaryImportRule/nok.gs")
                .whenCheckedAgainst(UnnecessaryImportRule.class)
                .then().issuesFound().hasSizeEqualTo(7);
    }

    @Test
    void findsIssuesWhenUnnecessaryImportIsFoundAndAllowedExplicitImportNamespacesIsCustom() {
        given("UnnecessaryImportRule/nok.gs")
                .whenCheckedAgainst(UnnecessaryImportRule.class)
                .withRuleProperty("AllowedExplicitImportNamespaces", "entity.windowed.")
                .then().issuesFound().hasSizeEqualTo(6);
    }

    @Test
    void findsIssuesWhenUnnecessaryImportIsFoundOnClassWithInnerClass() {
        given("UnnecessaryImportRule/nokWithInnerClass.gs")
                .whenCheckedAgainst(UnnecessaryImportRule.class)
                .then().issuesFound().hasSizeEqualTo(1);
    }

    @Test
    void findsIssuesWhenUnnecessaryImportIsFoundOnClassAndVerifyUnderlying() {
        given("UnnecessaryImportRule/nokUnusedImport.gs")
                .whenCheckedAgainst(UnnecessaryImportRule.class)
                .then().issuesFound()
                .hasSizeEqualTo(2)
                .areLocatedOn(
                        GosuIssueLocations.of(
                                Arrays.asList(3, 6, 3, 26),
                                Arrays.asList(6, 6, 6, 25)
                        )
                );
    }
}
