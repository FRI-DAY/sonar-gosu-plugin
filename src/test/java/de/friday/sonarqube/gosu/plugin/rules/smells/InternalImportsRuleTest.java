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

class InternalImportsRuleTest {

    @Test
    void findsNoIssuesWhenClassDoNotUsesGuidewireInternalImports() {
        given("InternalImportsRule/ok.gs")
                .whenCheckedAgainst(InternalImportsRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsIssuesWhenGosuCodeUsesGuidewireInternalImports() {
        given("InternalImportsRule/nok.gs")
                .whenCheckedAgainst(InternalImportsRule.class)
                .then().issuesFound().hasSizeEqualTo(6);
    }

    @Test
    void findsIssuesWhenEnhancementUsesGuidewireInternalImports() {
        given("InternalImportsRule/enhancementTest.gsx")
                .whenCheckedAgainst(InternalImportsRule.class)
                .then().issuesFound().hasSizeEqualTo(4);
    }

    @Test
    void findsIssuesWhenInterfaceUsesGuidewireInternalImports() {
        given("InternalImportsRule/interfaceTest.gs")
                .whenCheckedAgainst(InternalImportsRule.class)
                .then().issuesFound().hasSizeEqualTo(3);
    }
}
