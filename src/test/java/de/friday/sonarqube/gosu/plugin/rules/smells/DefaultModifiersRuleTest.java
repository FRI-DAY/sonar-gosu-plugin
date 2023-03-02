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

class DefaultModifiersRuleTest {

    @Test
    void findsNoIssuesWhenClassHasNoExplicitModifiers() {
        given("DefaultModifiersRule/ok.gs")
                .whenCheckedAgainst(DefaultModifiersRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsNoIssuesWhenAbstractClassHasNoExplicitModifiers() {
        given("DefaultModifiersRule/OkAbstractClass.gs")
                .whenCheckedAgainst(DefaultModifiersRule.class)
                .then().issuesFound().areEmpty();
    }


    @Test
    void findsNoIssuesWhenFinalInnerClassHasNoExplicitModifiersDefined() {
        given("DefaultModifiersRule/finalInnerClass.gs")
                .whenCheckedAgainst(DefaultModifiersRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsNoIssuesWhenEnumHasNoExplicitModifiersDefined() {
        given("DefaultModifiersRule/enumClassWithoutModifiers.gs")
                .whenCheckedAgainst(DefaultModifiersRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsNoIssuesWhenEnumHasNoExplicitPublicModifierDefined() {
        given("DefaultModifiersRule/enumClassWithoutPublic.gs")
                .whenCheckedAgainst(DefaultModifiersRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsNoIssuesWhenInterfaceHasNoExplicitModifiersDefined() {
        given("DefaultModifiersRule/interfaceClassWithoutModifiers.gs")
                .whenCheckedAgainst(DefaultModifiersRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsNoIssuesWhenInterfaceHasNoExplicitPublicModifierDefined() {
        given("DefaultModifiersRule/interfaceClassWithoutPublic.gs")
                .whenCheckedAgainst(DefaultModifiersRule.class)
                .then().issuesFound().areEmpty();
    }

    @Test
    void findsIssuesWhenClassHasExplicitModifiersDefined() {
        given("DefaultModifiersRule/nok.gs")
                .whenCheckedAgainst(DefaultModifiersRule.class)
                .then().issuesFound().hasSizeEqualTo(6);
    }

    @Test
    void findsIssuesWhenInterfaceHasExplicitModifiersDefined() {
        given("DefaultModifiersRule/interfaceClass.gs")
                .whenCheckedAgainst(DefaultModifiersRule.class)
                .then().issuesFound().hasSizeEqualTo(4);
    }

    @Test
    void findsIssuesWhenEnumHasExplicitModifiersDefined() {
        given("DefaultModifiersRule/enumClass.gs")
                .whenCheckedAgainst(DefaultModifiersRule.class)
                .then().issuesFound().hasSizeEqualTo(2);
    }

    @Test
    void findsIssuesWhenFinalClassHasExplicitModifiersDefined() {
        given("DefaultModifiersRule/finalClass.gs")
                .whenCheckedAgainst(DefaultModifiersRule.class)
                .then().issuesFound().hasSizeEqualTo(2);
    }
}
